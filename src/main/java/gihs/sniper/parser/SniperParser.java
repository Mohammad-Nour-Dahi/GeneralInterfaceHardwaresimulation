package gihs.sniper.parser;

import com.fasterxml.jackson.databind.JsonNode;
import gihs.core.hardwaresimulationManagementWithDocker.ParserAbstract;
import gihs.sniper.input.SniperInput;
import gihs.sniper.output.SniperOutput;

/**
 * The SniperParser class is responsible for parsing and running the Sniper hardware simulation with input and output
 */
public class SniperParser extends ParserAbstract {

    /**
     * Array of error messages that indicate various configuration issues.
     */
    private static final String[] ERROR_MESSAGES = {
            "Invalid cache configuration",
            "Caches of non-power of 2 size",
            "*** Configuration error ***",
            "Error",
            "ERROR"
    };


    /**
     * Parses the input and runs the Sniper hardware simulation using Docker
     *
     * @param input the input value
     */
    @Override
    public void parse(JsonNode input) {
        super.parse(input);
        String SniperCfg = "SniperSilvermont.cfg";
        String SniperOut = "SniperSilvermont.out";

        init("sniper");

        generateInputParametersFile.generateInputParameters(new SniperInput(input), "../resources/"+SniperCfg);

        host.inputFileTOContainer(containerId, "../resources/"+SniperCfg, "usr/local/src/sniper/config/");
        host.inputFileTOContainer(containerId, binaryPath, "/usr/local/src/sniper/");
        // Capture the start time
        long startTime = System.nanoTime();
        String outputConsole=  host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"./run-sniper", "-c", "SniperSilvermont", command}));
        // Capture the end time
        long endTime = System.nanoTime();

        // Calculate the execution time (difference between end time and start time)
        long executionTime = endTime - startTime;
        // host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"cat", "sim.out"}));
        handleOutputConsole(ERROR_MESSAGES,outputConsole);

        host.outputFileFromContainer(containerId, "usr/local/src/sniper/sim.out", "../resources/"+SniperOut);

        generateOutputParametersFile.generateOutputParameters(new SniperOutput(), "../resources/"+SniperOut, statsOutputPath + "/generatestatsOutputSniper.json","\"HostNanoseconds\" : "+executionTime);

        exit();
    }

}
