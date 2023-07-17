package parserHardwaresimulation;

import com.fasterxml.jackson.databind.JsonNode;
import input.GenerateInputParametersFile;
import managementOFJsonNodeALL.JsonNodeALL;
import managementOfDockerfiles.HardwaresimulationDocker;
import output.GenerateOutputParametersFile;

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
        statsOutputPath = JsonNodeALL.getALL(input, "commonParameters.hardwaresimulation.statsOutputPath").asText();

        // Implementation of the parsing and simulation logic using Docker
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

}
