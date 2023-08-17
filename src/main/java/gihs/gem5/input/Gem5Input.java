package gihs.gem5.input;

import com.fasterxml.jackson.databind.JsonNode;
import gihs.core.input.GenerateInputParametersImplementation;
import gihs.core.managementOFJsonNodeALL.JsonNodeALL;

/**
 * The Gem5Input class implements the GenerateInputParameters interface to generate input code
 * for the Gem5 simulator based on the provided JSON data.
 */
public class Gem5Input extends GenerateInputParametersImplementation {
    /**
     * Constructs a new instance of the Gem5Input class.
     *
     * @param parametersFormInputJSON The input JSON node.
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
        StringBuilder cfgCodeBuilder = null;
        try {

        String memorySize = JsonNodeALL.getALL(parametersFormInputJSON, "gem5.memory.size").asText();
        String startingCoreType = JsonNodeALL.getALL(parametersFormInputJSON, "gem5.processor.starting_core_type").asText();
        String switchCoreType = JsonNodeALL.getALL(parametersFormInputJSON, "gem5.processor.switch_core_type").asText();
        String isa = JsonNodeALL.getALL(parametersFormInputJSON, "gem5.processor.isa").asText();


        cfgCodeBuilder = new StringBuilder();
        cfgCodeBuilder.append("from gem5.utils.requires import requires\n");
        cfgCodeBuilder.append("from gem5.components.boards.simple_board import SimpleBoard\n");
        cfgCodeBuilder.append("from gem5.components.memory.single_channel import SingleChannelDDR3_1600\n");
        cfgCodeBuilder.append("from gem5.components.cachehierarchies.ruby.mesi_two_level_cache_hierarchy import MESITwoLevelCacheHierarchy\n");
        cfgCodeBuilder.append("from gem5.components.processors.simple_switchable_processor import SimpleSwitchableProcessor\n");
        cfgCodeBuilder.append("from gem5.coherence_protocol import CoherenceProtocol\n");
        cfgCodeBuilder.append("from gem5.isas import ISA\n");
        cfgCodeBuilder.append("from gem5.components.processors.cpu_types import CPUTypes\n");
        cfgCodeBuilder.append("from gem5.resources.resource import CustomResource\n");
        cfgCodeBuilder.append("from gem5.simulate.simulator import Simulator\n\n");

        cfgCodeBuilder.append("requires(isa_required=ISA." + isa + ", coherence_protocol_required=CoherenceProtocol.MESI_TWO_LEVEL)\n\n");

        cfgCodeBuilder.append("cache_hierarchy = MESITwoLevelCacheHierarchy(\n");
        cfgCodeBuilder.append("    l1d_size=\"" + l1dSize + "\",\n");
        cfgCodeBuilder.append("    l1d_assoc=" + l1dAssoc + ",\n");
        cfgCodeBuilder.append("    l1i_size=\"" + l1iSize + "\",\n");
        cfgCodeBuilder.append("    l1i_assoc=" + l1iAssoc + ",\n");
        cfgCodeBuilder.append("    l2_size=\"" + l2Size + "\",\n");
        cfgCodeBuilder.append("    l2_assoc=" + l2Assoc + ",\n");
        cfgCodeBuilder.append("    num_l2_banks= 1 \n");
        cfgCodeBuilder.append(")\n\n");

        cfgCodeBuilder.append("memory = SingleChannelDDR3_1600(\"" + memorySize + "\")\n\n");

        cfgCodeBuilder.append("processor = SimpleSwitchableProcessor(\n");
        cfgCodeBuilder.append("    starting_core_type=CPUTypes." + startingCoreType + ",\n");
        cfgCodeBuilder.append("    switch_core_type=CPUTypes." + switchCoreType + ",\n");
        cfgCodeBuilder.append("    num_cores= 2 ,\n");
        cfgCodeBuilder.append("    isa=ISA." + isa + "\n");
        cfgCodeBuilder.append(")\n\n");

        cfgCodeBuilder.append("board = SimpleBoard(\n");
        cfgCodeBuilder.append("    clk_freq=\"" + frequency + "\",\n");
        cfgCodeBuilder.append("    processor=processor,\n");
        cfgCodeBuilder.append("    memory=memory,\n");
        cfgCodeBuilder.append("    cache_hierarchy=cache_hierarchy\n");
        cfgCodeBuilder.append(")\n\n");

        cfgCodeBuilder.append("binary = CustomResource(\"" + command.replaceAll("./", "") + "\")\n\n");

        cfgCodeBuilder.append("board.set_se_binary_workload(binary)\n\n");

        cfgCodeBuilder.append("simulator = Simulator(board=board)\n");
        cfgCodeBuilder.append("simulator.run()\n");
    } catch(
    NullPointerException e)

    {
        // Handle the case when the JsonNode is null
        System.err.println("Error: The \"gem5\" section contains a null JsonNode. Please ensure that the input data is correctly formatted and all required fields are provided in the \"gem5\" section.");
    }
        return cfgCodeBuilder.toString();
}
}
