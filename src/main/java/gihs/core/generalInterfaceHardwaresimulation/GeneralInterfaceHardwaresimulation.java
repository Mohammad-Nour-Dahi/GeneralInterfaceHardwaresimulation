package gihs.core.generalInterfaceHardwaresimulation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gihs.core.managementOFJsonNodeALL.JsonUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import gihs.gem5.parser.Gem5Parser;
import gihs.core.hardwaresimulationManagementWithDocker.ParserInterface;
import gihs.sniper.parser.SniperParser;
import gihs.zsim.parser.ZsimParser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The main class for the general interface of hardware simulation.
 */
public class GeneralInterfaceHardwaresimulation {

    /**
     * Runs the simulation based on the command line arguments.
     * Performs different actions based on the hardware simulation name.
     * @param optionsCommandLine The command line arguments.
     *
     */
    public void simulation(String[] optionsCommandLine) {
        String jsonFilePath = getJsonFilePath(optionsCommandLine);
        JsonNode jsonFileRootNode = getJsonFileRootNode(jsonFilePath);

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
            parserStrategy.parse(jsonFileRootNode);
        } else {
            // If the hardware simulation name is not recognized, print an error message.
            System.out.println("Invalid value for 'commonParameters.hardwaresimulation.name' in the JSON file.");
        }
    }

    private String getJsonFilePath(String[] inputOptions) {
        String jsonFilePath = null;
        Options options = createOptions();

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, inputOptions);

            if (cmd.hasOption("help")) {
                printHelp(options);
            } else if (cmd.hasOption("jsonFile")) {
                jsonFilePath = cmd.getOptionValue("jsonFile");

            } else {
                System.out.println("The 'jsonFile' option was not specified. Use the 'help' option for more information.");
            }
        } catch (ParseException e) {
            System.out.println("Error parsing the command line arguments: " + e.getMessage());
        }
        return jsonFilePath;
    }

    /**
     * Processes the JSON file and performs actions based on the content.
     *
     * @param jsonFilePath The path to the JSON file.
     */
    private JsonNode getJsonFileRootNode(String jsonFilePath) {
        JsonNode rootNode = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            rootNode = objectMapper.readTree(new File(jsonFilePath));
        } catch (IOException e) {
            System.out.println("Error reading the JSON file: " + e.getMessage());
        }
        return rootNode;
    }

    /**
     * Retrieves the hardware simulation name from the JSON node.
     *
     * @param rootNode The root node of the JSON data.
     * @return The hardware simulation name, or null if not found.
     */
    private String getHardwareSimulationName(JsonNode rootNode) {
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
     *
     * @return the Options object containing the command line options
     */
    private Options createOptions() {
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
    private void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        String readmeContent = "";

        try {
            File readmeFile = new File("../resources/README.md");
            readmeContent = FileUtils.readFileToString(readmeFile, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        formatter.printHelp("General Interface Hardwaresimulation", readmeContent, options, "");
    }

    /**
     * The main method of the program.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Capture the start time
        long startTime = System.nanoTime();

        GeneralInterfaceHardwaresimulation generalHardwaresimulation = new GeneralInterfaceHardwaresimulation();
        generalHardwaresimulation.simulation(args);

        // Capture the end time
        long endTime = System.nanoTime();

        // Calculate the execution time (difference between end time and start time)
        long executionTime = endTime - startTime;

        // Output the execution time
        System.out.println("Execution time: " + executionTime + " nanoseconds");

    }
}
