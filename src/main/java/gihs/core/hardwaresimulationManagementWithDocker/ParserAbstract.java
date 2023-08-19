package gihs.core.hardwaresimulationManagementWithDocker;

import com.fasterxml.jackson.databind.JsonNode;
import gihs.core.input.GenerateInputParametersFile;
import gihs.core.managementOFJsonNodeALL.JsonUtil;
import gihs.core.managementOfDockerfiles.HardwaresimulationDocker;
import gihs.core.output.GenerateOutputParametersFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The ParserInterfaceImplementation class is an implementation of the ParserInterface.
 * It provides the logic to parse and run hardware simulations with Docker.
 */
public abstract class ParserAbstract implements ParserInterface {
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
    protected ParserAbstract() {
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



            statsOutputPath = JsonUtil.get(input, "commonParameters.hardwaresimulation.statsOutputPath").asText();
            programPath = JsonUtil.get(input, "commonParameters.hardwaresimulation.programPath").asText();
            binaryPath = JsonUtil.get(input, "commonParameters.hardwaresimulation.binaryPath").asText();

            if (isInvalidPath(programPath)) {
                throw new NullPointerException("Invalid programPath :" +programPath);
            }

            if (isInvalidPath(binaryPath)) {
                throw new NullPointerException("Invalid binaryPath: "+binaryPath);
            }
            fileName = new File(programPath).getName();
            command = "./" + fileName.replaceAll("\\.c", "");
            // Implementation of the parsing and simulation logic using Docker


    }

    /**
     * Performs cleanup operations and terminates the hardware simulation.
     */
    protected void exit() {
        // Close the hardware simulation container
        hardwaresimulation.closeDockerHardwaresimulation();

        // Delete the hardware simulation container
        hardwaresimulation.deleteDockerHardwaresimulation();
    }

    /**
     * Checks if the given path is invalid and the existing path is not in the filesystem.
     *
     * @param path The path to be checked.
     * @return {@code true} If the path is invalid and exists not in the filesystem, {@code false} otherwise.
     */
    private boolean isInvalidPath(String path) {
        Path p = null;
        // Use Paths.get to convert the path string to a Path object
       try {
            p = Paths.get(path);
       }catch (InvalidPathException e){
           System.err.println(e);
           return true;

       }

        // Check if the path is absolute and exists in the filesystem
        return !(p.isAbsolute() && Files.exists(p));
    }

}
