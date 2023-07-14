package org.generalInterfaceHardwaresimulation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import managementOFJsonNodeALL.JsonNodeALL;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import parserHardwaresimulation.Gem5Parser;
import parserHardwaresimulation.ParserInterface;
import parserHardwaresimulation.SniperParser;
import parserHardwaresimulation.ZsimParser;

import java.io.File;
import java.io.IOException;

/**
 * The main class for the general interface of hardware simulation.
 */
public class GeneralInterfaceHardwaresimulation {

    /**
     * Runs the simulation based on the command line arguments.
     *
     * @param args The command line arguments.
     */
    public void simulation(String[] args) {
        Options options = createOptions();

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("help")) {
                printHelp(options);
            } else if (cmd.hasOption("jsonFile")) {
                String jsonFilePath = cmd.getOptionValue("jsonFile");
                processJsonFile(jsonFilePath);
            } else {
                System.out.println("The 'jsonFile' option was not specified. Use the 'help' option for more information.");
            }
        } catch (ParseException e) {
            System.out.println("Error parsing the command line arguments: " + e.getMessage());
        }
    }

    /**
     * Processes the JSON file and performs actions based on the content.
     *
     * @param jsonFilePath The path to the JSON file.
     */
    private void processJsonFile(String jsonFilePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));

            if (JsonNodeALL.hasALL(rootNode, "commonParameters.hardwaresimulation.name")) {
                String hardwareSimulationName = JsonNodeALL.getALL(rootNode, "commonParameters.hardwaresimulation.name").asText();

                performActionsBasedOnSimulationName(hardwareSimulationName, rootNode);
            } else {
                System.out.println("The key 'commonParameters.hardwaresimulation.name' was not found in the JSON file.");
            }
        } catch (IOException e) {
            System.out.println("Error reading the JSON file: " + e.getMessage());
        }
    }

    /**
     * Performs different actions based on the hardware simulation name.
     *
     * @param hardwareSimulationName The name of the hardware simulation.
     * @param rootNode              The root node of the JSON tree.
     */
    private void performActionsBasedOnSimulationName(String hardwareSimulationName, JsonNode rootNode) {
        ParserInterface parserStrategy;
        switch (hardwareSimulationName) {
            case "gem5":
                parserStrategy = new Gem5Parser();
                break;
            case "sniper":
                parserStrategy = new SniperParser();
                break;
            case "zsim":
                parserStrategy = new ZsimParser();
                break;
            default:
                System.out.println("Invalid value for 'commonParameters.hardwaresimulation.name' in the JSON file.");
                return;
        }

        parserStrategy.parse(rootNode);
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
        GeneralInterfaceHardwaresimulation generalHardwaresimulation = new GeneralInterfaceHardwaresimulation();
        generalHardwaresimulation.simulation(args);
    }
}
