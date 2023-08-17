package gihs.sniper.parser;

import com.fasterxml.jackson.databind.JsonNode;
import gihs.core.parser.ParserAbstract;
import gihs.sniper.input.SniperInput;
import gihs.sniper.output.SniperOutput;

/**
 * The SniperParser class is responsible for parsing and running the Sniper hardware simulation with input and output
 */
public class SniperParser extends ParserAbstract {

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
        handleOutputConsole(outputConsole);

        host.outputFileFromContainer(containerId, "usr/local/src/sniper/sim.out", "../resources/"+SniperOut);

        generateOutputParametersFile.generateOutputParameters(new SniperOutput(), "../resources/"+SniperOut, statsOutputPath + "/generatestatsOutputSniper.json","\"HostNanoseconds\" : "+executionTime);

        exit();
    }
    /**
     * Handles the output console and performs error handling based on its content.
     *
     * @param outputConsole The output console string.
     */
    private void handleOutputConsole(String outputConsole) {
        if (outputConsole.contains("Invalid cache configuration")) {
            System.err.println("Invalid cache configuration encountered. Exiting the program.");
            exit();
            System.exit(1);
        }else if (outputConsole.contains("Caches of non-power of 2 size")) {
            System.err.println("Caches of non-power of 2 size encountered. Exiting the program.");
            exit();
            System.exit(1);
        }else if (outputConsole.contains("*** Configuration error ***")) {
            System.err.println("Configuration error encountered. Exiting the program.");
            exit();
            System.exit(1);
        }else if (outputConsole.contains("ERROR")) {
            System.err.println("ERROR encountered. Exiting the program.");
            exit();
            System.exit(1);
        }else if (outputConsole.contains("Error")) {
            System.err.println("ERROR encountered. Exiting the program.");
            exit();
            System.exit(1);
        }

    }

}
