package parserHardwaresimulation;

import com.fasterxml.jackson.databind.JsonNode;
import input.GenerateInputParametersFile;
import managementOFJsonNodeALL.JsonNodeALL;
import managementOfDockerfiles.HardwaresimulationDocker;
import output.GenerateOutputParametersFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The ParserInterfaceImplementation class is an implementation of the ParserInterface.
 * It provides the logic to parse and run hardware simulations with Docker.
 */
public abstract class ParserInterfaceImplementation implements ParserInterface {
    /**
     * The instance of the HardwaresimulationDocker class for managing the hardware simulation.
     */
    protected HardwaresimulationDocker hardwaresimulation;


    /**
     * The instance of the HardwaresimulationDocker class for hosting the hardware simulation with bash.
     */
    protected HardwaresimulationDocker host;

    /**
     * The instance of the GenerateInputParametersFile class for generating input parameters file.
     */
    protected GenerateInputParametersFile generateInputParametersFile;

    /**
     * The instance of the GenerateOutputParametersFile class for generating output parameters file.
     */
    protected GenerateOutputParametersFile generateOutputParametersFile;

    /**
     * The ID of the container running the hardware simulation.
     */
    protected String containerId;

    /**
     * The path to the directory where the simulation statistics output will be stored.
     */
    protected String statsOutputPath;


    /**
     * The "binaryPath" variable stores the path to the binary file.
     */
    protected String binaryPath;

    /**
     * The "command" variable stores the command to be executed.
     */
    protected String command;

    /**
     * The "programPath" variable stores the path to the program.
     */
    protected String programPath;

    /**
     * The "fileName" variable stores the file name.
     */
    protected String fileName;

    /**
     * Constructor for the ParserInterfaceImplementation class.
     * Initializes the HardwaresimulationDocker objects
     * and the GenerateInputParametersFile and GenerateOutputParametersFile objects
     * for hardware simulation parsing.
     */
    ParserInterfaceImplementation() {
        // Create a new instance of the HardwaresimulationDocker class
        this.hardwaresimulation = new HardwaresimulationDocker();

        // Create a new instance of the HardwaresimulationDocker class for hosting the simulation with bash
        this.host = new HardwaresimulationDocker();

        // Create a new instance of the GenerateInputParametersFile class for generating input parameters file
        this.generateInputParametersFile = new GenerateInputParametersFile();

        // Create a new instance of the GenerateOutputParametersFile class for generating output parameters file
        this.generateOutputParametersFile = new GenerateOutputParametersFile();
    }

    /**
     * Initializes the hardware simulation with the given name.
     *
     * @param hardwaresimulationName the name of the hardware simulation
     */
    protected void init(String hardwaresimulationName) {
        hardwaresimulation.createHardwaresimulationContainer(hardwaresimulationName);
        containerId = hardwaresimulation.getContainerId();
        hardwaresimulation.startHardwaresimulationContainer(containerId);
    }

    /**
     * Parses the input string and runs the hardware simulation using Docker.
     *
     * @param input the input string
     */
    @Override
    public void parse(JsonNode input) {
        try {


            statsOutputPath = JsonNodeALL.getALL(input, "commonParameters.hardwaresimulation.statsOutputPath").asText();
            programPath = JsonNodeALL.getALL(input, "commonParameters.hardwaresimulation.programPath").asText();
            binaryPath = JsonNodeALL.getALL(input, "commonParameters.hardwaresimulation.binaryPath").asText();

            if (!isValidPath(programPath)) {
                System.err.println("Invalid programPath: " + programPath);
                System.exit(1);
            }

            if (!isValidPath(binaryPath)) {
                System.err.println("Invalid binaryPath: " + binaryPath);
                System.exit(1);
            }
            fileName = new File(programPath).getName();
            command = "./" + fileName.replaceAll("\\.c", "");
            // Implementation of the parsing and simulation logic using Docker

        } catch (NullPointerException e) {
            // Handle the case when the JsonNode is null
            System.err.println("Error: The \"commonParameters.hardwaresimulation\" section contains a null JsonNode. Please ensure that the input data is correctly formatted and all required fields are provided in the \"commonParameters.hardwaresimulation\" section.");
            System.exit(1);
        }
    }

    /**
     * Performs cleanup operations and terminates the hardware simulation.
     */
    protected void exit() {
        // Close the hardware simulation container
        host.closeDockerHardwaresimulation(containerId);

        // Delete the hardware simulation container
        host.deleteDockerHardwaresimulation(containerId);
    }

    /**
     * Checks if the given path is a valid and existing path in the filesystem.
     *
     * @param path The path to be checked.
     * @return {@code true} if the path is valid and exists in the filesystem, {@code false} otherwise.
     */
    private boolean isValidPath(String path) {
        // Use Paths.get to convert the path string to a Path object
        Path p = Paths.get(path);

        // Check if the path is absolute and exists in the filesystem
        return p.isAbsolute() && Files.exists(p);
    }

}
