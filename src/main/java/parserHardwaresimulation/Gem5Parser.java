package parserHardwaresimulation;

import com.fasterxml.jackson.databind.JsonNode;
import input.Gem5Input;
import output.Gem5Output;

/**
 * The Gem5Parser class is responsible for parsing and running the Gem5 hardware simulation with input and output.
 */
public class Gem5Parser extends ParserInterfaceImplementation {

    /**
     * Parses the input and runs the Gem5 hardware simulation using Docker.
     *
     * @param input the input value
     */
    @Override
    public void parse(JsonNode input) {
        super.parse(input);
        String generateHardwaresimulationInputParametersPath = "../resources/generateGem5Parameter.py";

        String gem5Stats = "gem5Stats.txt";

        init("gem5");


        generateInputParametersFile.generateInputParameters(new Gem5Input(), input, generateHardwaresimulationInputParametersPath);

        host.inputFileTOContainer(containerId, generateHardwaresimulationInputParametersPath, "usr/local/src/gem5/configs/learning_gem5/part1/");

        host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"build/X86/gem5.opt", "--stats-file=" + gem5Stats, "configs/learning_gem5/part1/generateGem5Parameter.py"}));
        //host.outputFromHardwaresimulationConsole( hardwaresimulation.command(new String[]{"cat", "m5out/" + gem5Stats}));



        host.outputFileFromContainer(containerId, "usr/local/src/gem5/m5out/" + gem5Stats, "../resources/" + gem5Stats);

        generateOutputParametersFile.generateOutputParameters(new Gem5Output(), "../resources/" + gem5Stats, statsOutputPath +"/generatestatsOutputGem5.json");

        exit();

    }
}
