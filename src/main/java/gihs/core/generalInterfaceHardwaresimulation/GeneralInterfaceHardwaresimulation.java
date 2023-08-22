package gihs.core.generalInterfaceHardwaresimulation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gihs.core.hardwaresimulationManagementWithDocker.ParserInterface;
import gihs.core.managementOFJsonNodeALL.JsonUtil;
import gihs.gem5.parser.Gem5Parser;
import gihs.sniper.parser.SniperParser;
import gihs.zsim.parser.ZsimParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The main class for the general interface of hardware simulation.
 */
public class GeneralInterfaceHardwaresimulation {

    /**
     * Array of required parameters for the general interface.
     * Each parameter is specified in a hierarchical format, representing its location in the JSON structure.
     */
    private static final String[] requiredParameters = {
            // Common Parameters
            "commonParameters.hardwaresimulation.name",
            "commonParameters.hardwaresimulation.programPath",
            "commonParameters.hardwaresimulation.binaryPath",
            "commonParameters.hardwaresimulation.statsOutputPath",
            "commonParameters.cache_hierarchy.l1d_size",
            "commonParameters.cache_hierarchy.l1d_assoc",
            "commonParameters.cache_hierarchy.l1i_size",
            "commonParameters.cache_hierarchy.l1i_assoc",
            "commonParameters.cache_hierarchy.l2_size",
            "commonParameters.cache_hierarchy.l2_assoc",
            "commonParameters.board.frequency",

            // Gem5 Parameters
            "gem5.memory.size",
            "gem5.processor.starting_core_type",
            "gem5.processor.switch_core_type",
            "gem5.processor.isa",

            // Sniper Parameters
            "sniper.perf_model/core.logical_cpus",
            "sniper.perf_model/core.type",
            "sniper.perf_model/itlb.size",
            "sniper.perf_model/itlb.associativity",
            "sniper.perf_model/dtlb.size",
            "sniper.perf_model/dtlb.associativity",
            "sniper.perf_model/stlb.size",
            "sniper.perf_model/stlb.associativity",
            "sniper.perf_model/dram_directory.total_entries",
            "sniper.perf_model/dram_directory.associativity",

            // ZSim Parameters
            "zsim.lineSize",
            "zsim.caches.l1d.latency",
            "zsim.caches.l1i.latency",
            "zsim.caches.l2.mshrs",
            "zsim.caches.l2.latency",
            "zsim.mem.splitAddrs",
            "zsim.mem.closedPage",
            "zsim.mem.controllerLatency",
            "zsim.mem.controllers"
    };


    /**
     * Runs the simulation based on the provided command line arguments.
     * Executes various actions depending on the hardware simulation name.
     *
     * @param optionsCommandLine The command line arguments.
     *                           Possible options include:
     *                           -help             Display help
     *                           -jsonFile <arg>   Path to the JSON file
     */
    public static void simulationRun(String[] optionsCommandLine) {
        String jsonFilePath;
        JsonNode jsonFileRootNode = null;
        try {
            jsonFilePath = getJsonFilePath(optionsCommandLine);
            jsonFileRootNode = getJsonFileRootNode(jsonFilePath);
            JsonUtil.validateJsonInput(jsonFileRootNode, requiredParameters);
        } catch (ArithmeticException e) {
            System.err.println("error: " + e.getMessage());
            return; // Exit the method if validation fails
        }

        String hardwareSimulationName = getHardwareSimulationName(jsonFileRootNode);

        Map<String, ParserInterface> parserMap = new HashMap<>();
        parserMap.put("gem5", new Gem5Parser());
        parserMap.put("sniper", new SniperParser());
        parserMap.put("zsim", new ZsimParser());

        // Get the parser strategy from the map based on the hardware simulation name.
        ParserInterface parserStrategy = parserMap.get(hardwareSimulationName);

        // Check if a valid parser strategy was found.
        if (parserStrategy != null) {
            // Call the 'parse' method on the selected parser strategy object, passing in the 'rootNode'.
           try {
               parserStrategy.parse(jsonFileRootNode);
           }catch (NullPointerException e) {
               System.err.println(e);
           }
        } else {
            // If the hardware simulation name is not recognized, print an error message.
            System.out.println("Invalid value for 'commonParameters.hardwaresimulation.name' in the JSON file.");
        }
    }

    /**
     * Retrieves the path to the JSON file from the command line options.
     *
     * @param inputOptions The command line arguments.
     * @return The path to the JSON file, or null if not specified.
     */
    private static String getJsonFilePath(String[] inputOptions) {
        String jsonFilePath = null;
        Options options = createOptions();

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, inputOptions);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (cmd.hasOption("help")) {

                    printHelp(options);

            } else if (cmd.hasOption("jsonFile")) {
                jsonFilePath = cmd.getOptionValue("jsonFile");

            } else {

                throw new ArithmeticException("\""+ cmd.getArgList().get(0)+"\" is not recognized as an option. Use \"help\" for more information.");


        }

        return jsonFilePath;
    }

    /**
     * Retrieves the root JSON node from the JSON file.
     *
     * @param jsonFilePath The path to the JSON file.
     * @return The root JSON node, or null if there was an error reading the file.
     */
    private static JsonNode getJsonFileRootNode(String jsonFilePath) {
        JsonNode rootNode = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            rootNode = objectMapper.readTree(new File(jsonFilePath));
        } catch (NullPointerException | IOException e) {
            System.out.println("Error reading the JSON file: " + e.getMessage());
        }
        return rootNode;
    }

    /**
     * Retrieves the hardware simulation name from the JSON configuration.
     *
     * @param rootNode The root JSON node of the configuration.
     * @return The hardware simulation name, or null if the key is not found.
     */
    private static String getHardwareSimulationName(JsonNode rootNode) {
        String hardwareSimulationName = null;

        // Check if the JSON node contains the specified key
        if (JsonUtil.has(rootNode, "commonParameters.hardwaresimulation.name")) {
            // Retrieve the hardware simulation name from the JSON node
            hardwareSimulationName = JsonUtil.get(rootNode, "commonParameters.hardwaresimulation.name").asText();
        } else {
            // Key not found, print an error message
            System.out.println("The key 'commonParameters.hardwaresimulation.name' was not found in the JSON file.");
        }

        return hardwareSimulationName;
    }


    /**
     * Creates the command line options for the program.
     * Possible options include:
     *                                -help             Display help
     *                                -jsonFile <arg>   Path to the JSON file
     *
     * @return the Options object containing the command line options
     */
    private static Options createOptions() {
        Options options = new Options();
        options.addOption("jsonFile", true, "Path to the JSON file");
        options.addOption("help", false, "Display help");
        return options;
    }

    /**
     * Prints the help message for the program.
     *
     * @param options the Options object containing the command line options
     */
    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        String readmeContent = "";

        try {
            File readmeFile = new File("../resources/README.md");
            readmeContent = FileUtils.readFileToString(readmeFile, "UTF-8");
        } catch (IOException e) {
            try {
                throw new Exception("Error reading README file: " + e.getMessage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            // Stop further processing since there's an error
        }

        formatter.printHelp("General Interface Hardwaresimulation", readmeContent, options, "");
        System.exit(1);
    }

    /**
     * The main method of the program.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Capture the start time
        long startTime = System.nanoTime();

        GeneralInterfaceHardwaresimulation.simulationRun(args);

        // Capture the end time
        long endTime = System.nanoTime();

        // Calculate the execution time (difference between end time and start time)
        long executionTime = endTime - startTime;

        // Output the execution time
        System.out.println("Execution time: " + executionTime + " nanoseconds");

    }
}
