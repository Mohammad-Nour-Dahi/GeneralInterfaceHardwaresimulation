package gihs.zsim.parser;

import com.fasterxml.jackson.databind.JsonNode;
import gihs.core.parser.ParserAbstract;
import gihs.zsim.input.ZsimInput;
import gihs.zsim.output.ZsimOutput;

public class ZsimParser extends ParserAbstract {


    @Override
    public void parse(JsonNode input) {
        super.parse(input);

        String generateHardwaresimulationParameter = "zsim.cfg";
        String zsim_out = "zsim.out";
        init("zsim");

        generateInputParametersFile.generateInputParameters(new ZsimInput(input), "../resources/" + generateHardwaresimulationParameter);

        hardwaresimulation.inputFileTOContainer(containerId, "../resources/" + generateHardwaresimulationParameter, "/usr/local/src/zsim-plusplus/tests/");

        host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"echo", "0", "|", "sudo", "tee", "/proc/sys/kernel/randomize_va_space"}));

        host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"scons", "-j16"}));
        host.inputFileTOContainer(containerId, programPath, "/usr/local/src/zsim-plusplus/");
        String outputConsoleCompile = host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"gcc", "-static", "-std=c99", "-o", fileName.replaceAll("\\.c", ""), fileName}));
        handleOutputConsole(outputConsoleCompile);
        String outputConsole = host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"./build/opt/zsim", "tests/" + generateHardwaresimulationParameter}));

        handleOutputConsole(outputConsole);

        // host.outputFromHardwaresimulationConsole(hardwaresimulation.command(new String[]{"cat", "/usr/local/src/zsim-plusplus/zsim.out"}));


        hardwaresimulation.outputFileFromContainer(containerId, "/usr/local/src/zsim-plusplus/zsim.out", "../resources/" + zsim_out);
        generateOutputParametersFile.generateOutputParameters(new ZsimOutput(), "../resources/" + zsim_out, statsOutputPath + "/generatestatsOutputZsim.json");

        exit();

    }

    /**
     * Handles the output console and performs error handling based on its content.
     *
     * @param outputConsole The output console string.
     */
    private void handleOutputConsole(String outputConsole) {
        if (outputConsole.contains("Bank size must be a multiple of line size")) {
            System.err.println("Bank size must be a multiple of line size encountered. Exiting the program.");
            exit();
            System.exit(1);
        } else if (outputConsole.contains("Number of sets must be a power of two")) {
            System.err.println("Number of sets must be a power of two encountered. Exiting the program.");
            exit();
            System.exit(1);
        } else if (outputConsole.contains("error: syntax error")) {
            System.err.println("Syntax error encountered. Exiting the program.");
            exit();
            System.exit(1);
        } else if (outputConsole.contains("Unsupported line size")) {
            System.err.println("Unsupported line size encountered. Exiting the program.");
            exit();
            System.exit(1);
        } else if (outputConsole.contains("can't connect more cores to it")) {
            System.err.println("Can't connect more cores to it encountered. Exiting the program.");
            exit();
            System.exit(1);
        } else if (outputConsole.contains("error")) {
            System.err.println("error encountered. Exiting the program.");
            exit();
            System.exit(1);
        }
    }


}
