package gihs.zsim.parser;

import com.fasterxml.jackson.databind.JsonNode;
import gihs.core.hardwaresimulationManagementWithDocker.ParserAbstract;
import gihs.zsim.input.ZsimInput;
import gihs.zsim.output.ZsimOutput;

public class ZsimParser extends ParserAbstract {

    /**
     * Array of error messages that indicate various configuration issues.
     */
    protected static final String[] ERROR_MESSAGES = {
            "Bank size must be a multiple of line size",
            "Number of sets must be a power of two",
            "error: syntax error",
            "Unsupported line size",
            "can't connect more cores to it",
            "error"
    };

    @Override
    public void parse(JsonNode input) {
        super.parse(input);
        ERROR_MESSAGES[0] = "ni";

        String generateHardwaresimulationParameter = "zsim.cfg";
        String zsim_out = "zsim.out";
        init("zsim");

        generateInputParametersFile.generateInputParameters(new ZsimInput(input), "../resources/" + generateHardwaresimulationParameter);

        hardwaresimulation.inputFileTOContainer(containerId, "../resources/" + generateHardwaresimulationParameter, "/usr/local/src/zsim-plusplus/tests/");

        host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"echo", "0", "|", "sudo", "tee", "/proc/sys/kernel/randomize_va_space"}));

        host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"scons", "-j16"}));
        host.inputFileTOContainer(containerId, programPath, "/usr/local/src/zsim-plusplus/");
        String outputConsoleCompile = host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"gcc", "-static", "-std=c99", "-o", fileName.replaceAll("\\.c", ""), fileName}));
        handleOutputConsole(ERROR_MESSAGES,outputConsoleCompile);
        String outputConsole = host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"./build/opt/zsim", "tests/" + generateHardwaresimulationParameter}));

        handleOutputConsole(ERROR_MESSAGES,outputConsole);

        // host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"cat", "/usr/local/src/zsim-plusplus/zsim.out"}));


        hardwaresimulation.outputFileFromContainer(containerId, "/usr/local/src/zsim-plusplus/zsim.out", "../resources/" + zsim_out);
        generateOutputParametersFile.generateOutputParameters(new ZsimOutput(), "../resources/" + zsim_out, statsOutputPath + "/generatestatsOutputZsim.json");

        exit();

    }


}
