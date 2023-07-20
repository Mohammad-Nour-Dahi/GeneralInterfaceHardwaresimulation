package input;

import com.fasterxml.jackson.databind.JsonNode;
import managementOFJsonNodeALL.JsonNodeALL;

/**
 * The Gem5Input class implements the GenerateInputParameters interface to generate input code
 * for the Gem5 simulator based on the provided JSON data.
 */
public class Gem5Input extends GenerateInputParametersImplementation {
    /**

     Constructs a new instance of the Gem5Input class.

     @param parametersFormInputJSON The input JSON node.
     */
    public Gem5Input(JsonNode parametersFormInputJSON) {
        super(parametersFormInputJSON);
    }

    /**
     * Generates input code for the Gem5 simulator based on the provided JSON data.
     *
     * @return The generated input code as a String.
     */
    @Override
    public String generateInputCode() {

        String memorySize = JsonNodeALL.getALL(parametersFormInputJSON, "gem5.memory.size").asText();
        String startingCoreType = JsonNodeALL.getALL(parametersFormInputJSON, "gem5.processor.starting_core_type").asText();
        String switchCoreType = JsonNodeALL.getALL(parametersFormInputJSON, "gem5.processor.switch_core_type").asText();
        String isa = JsonNodeALL.getALL(parametersFormInputJSON, "gem5.processor.isa").asText();


        StringBuilder pythonCodeBuilder = new StringBuilder();
        pythonCodeBuilder.append("from gem5.utils.requires import requires\n");
        pythonCodeBuilder.append("from gem5.components.boards.simple_board import SimpleBoard\n");
        pythonCodeBuilder.append("from gem5.components.memory.single_channel import SingleChannelDDR3_1600\n");
        pythonCodeBuilder.append("from gem5.components.cachehierarchies.ruby.mesi_two_level_cache_hierarchy import MESITwoLevelCacheHierarchy\n");
        pythonCodeBuilder.append("from gem5.components.processors.simple_switchable_processor import SimpleSwitchableProcessor\n");
        pythonCodeBuilder.append("from gem5.coherence_protocol import CoherenceProtocol\n");
        pythonCodeBuilder.append("from gem5.isas import ISA\n");
        pythonCodeBuilder.append("from gem5.components.processors.cpu_types import CPUTypes\n");
        pythonCodeBuilder.append("from gem5.resources.resource import CustomResource\n");
        pythonCodeBuilder.append("from gem5.simulate.simulator import Simulator\n\n");

        pythonCodeBuilder.append("requires(isa_required=ISA." + isa + ", coherence_protocol_required=CoherenceProtocol.MESI_TWO_LEVEL)\n\n");

        pythonCodeBuilder.append("cache_hierarchy = MESITwoLevelCacheHierarchy(\n");
        pythonCodeBuilder.append("    l1d_size=\"" + l1dSize + "\",\n");
        pythonCodeBuilder.append("    l1d_assoc=" + l1dAssoc + ",\n");
        pythonCodeBuilder.append("    l1i_size=\"" + l1iSize + "\",\n");
        pythonCodeBuilder.append("    l1i_assoc=" + l1iAssoc + ",\n");
        pythonCodeBuilder.append("    l2_size=\"" + l2Size + "\",\n");
        pythonCodeBuilder.append("    l2_assoc=" + l2Assoc + ",\n");
        pythonCodeBuilder.append("    num_l2_banks= 1 \n");
        pythonCodeBuilder.append(")\n\n");

        pythonCodeBuilder.append("memory = SingleChannelDDR3_1600(\"" + memorySize + "\")\n\n");

        pythonCodeBuilder.append("processor = SimpleSwitchableProcessor(\n");
        pythonCodeBuilder.append("    starting_core_type=CPUTypes." + startingCoreType + ",\n");
        pythonCodeBuilder.append("    switch_core_type=CPUTypes." + switchCoreType + ",\n");
        pythonCodeBuilder.append("    num_cores= 2 ,\n");
        pythonCodeBuilder.append("    isa=ISA." + isa + "\n");
        pythonCodeBuilder.append(")\n\n");

        pythonCodeBuilder.append("board = SimpleBoard(\n");
        pythonCodeBuilder.append("    clk_freq=\"" + frequency + "\",\n");
        pythonCodeBuilder.append("    processor=processor,\n");
        pythonCodeBuilder.append("    memory=memory,\n");
        pythonCodeBuilder.append("    cache_hierarchy=cache_hierarchy\n");
        pythonCodeBuilder.append(")\n\n");

        pythonCodeBuilder.append("binary = CustomResource(\"" + command.replaceAll("./","") + "\")\n\n");

        pythonCodeBuilder.append("board.set_se_binary_workload(binary)\n\n");

        pythonCodeBuilder.append("simulator = Simulator(board=board)\n");
        pythonCodeBuilder.append("simulator.run()\n");

        return pythonCodeBuilder.toString();
    }
}
