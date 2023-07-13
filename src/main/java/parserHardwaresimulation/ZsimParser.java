package parserHardwaresimulation;

import com.fasterxml.jackson.databind.JsonNode;
import input.ZsimInput;
import output.ZsimOutput;

public class ZsimParser extends ParserInterfaceImplementation {


    @Override
    public void parse(JsonNode input) {
        super.parse(input);

        String generateHardwaresimulationParameter = "zsim.cfg";
        String zsim_out = "zsim.out";
        init("zsim");

        generateInputParametersFile.generateInputParameters(new ZsimInput(), input, "../resources/" + generateHardwaresimulationParameter);

        hardwaresimulation.inputFileTOContainer(containerId, "../resources/" + generateHardwaresimulationParameter, "/usr/local/src/zsim-plusplus/tests/");

        host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"echo", "0", "|", "sudo", "tee", "/proc/sys/kernel/randomize_va_space"}));

        host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"scons", "-j16"}));

        host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"./build/opt/zsim", "tests/" + generateHardwaresimulationParameter}));

        // host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"cat", "/usr/local/src/zsim-plusplus/zsim.out"}));


        hardwaresimulation.outputFileFromContainer(containerId,"/usr/local/src/zsim-plusplus/zsim.out","../resources/" + zsim_out);
        generateOutputParametersFile.generateOutputParameters(new ZsimOutput(), "../resources/" + zsim_out, statsOutputPath + "/generatestatsOutputZsim.json");

        exit();

    }


}
