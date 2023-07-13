package parserHardwaresimulation;

import com.fasterxml.jackson.databind.JsonNode;
import input.SniperInput;
import output.SniperOutput;

/**
 * The SniperParser class is responsible for parsing and running the Sniper hardware simulation with input and output
 */
public class SniperParser extends ParserInterfaceImplementation {

    /**
     * Parses the input and runs the Sniper hardware simulation using Docker
     *
     * @param input the input value
     */
    @Override
    public void parse(JsonNode input) {
        String SniperCfg = "SniperSilvermont.cfg";
        String SniperOut = "SniperSilvermont.out";
        super.parse(input);
        init("sniper");

        generateInputParametersFile.generateInputParameters(new SniperInput(), input, "../resources/"+SniperCfg);

        host.inputFileTOContainer(containerId, "../resources/"+SniperCfg, "usr/local/src/sniper/config/");

        host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"./run-sniper", "-c", "SniperSilvermont", "/bin/ls"}));
        // host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"cat", "sim.out"}));


        host.outputFileFromContainer(containerId, "usr/local/src/sniper/sim.out", "../resources/"+SniperOut);

        generateOutputParametersFile.generateOutputParameters(new SniperOutput(), "../resources/"+SniperOut, statsOutputPath + "/generatestatsOutputSniper.json");

        exit();
    }
}
