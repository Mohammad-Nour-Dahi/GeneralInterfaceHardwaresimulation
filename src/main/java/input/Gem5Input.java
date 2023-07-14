package input;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
/**
 * The Gem5Input class implements the GenerateInputParameters interface to generate input code
 * for the Gem5 simulator based on the provided JSON data.
 */
public class Gem5Input implements GenerateInputParameters {

    /**
     * Generates input code for the Gem5 simulator based on the provided JSON data.
     *
     * @param jsonData The JSON data used to generate the input code.
     * @return The generated input code as a String.
     */
    @Override
    public String generateInputCode(JsonNode jsonData) {
        JsonNode cacheHierarchy =  jsonData.get("commonParameters").get("cache_hierarchy");
        String l1dSize = cacheHierarchy.get("l1d_size").asText();
        int l1dAssoc = cacheHierarchy.get("l1d_assoc").asInt();
        String l1iSize = cacheHierarchy.get("l1i_size").asText();
        int l1iAssoc = cacheHierarchy.get("l1i_assoc").asInt();
        String l2Size = cacheHierarchy.get("l2_size").asText();
        int l2Assoc = cacheHierarchy.get("l2_assoc").asInt();
        int numL2Banks = jsonData.get("gem5").get("cache_hierarchy").get("num_l2_banks").asInt();

        ObjectNode memory = (ObjectNode) jsonData.get("gem5").get("memory");
        String memorySize = memory.get("size").asText();

        ObjectNode processor = (ObjectNode) jsonData.get("gem5").get("processor");
        String startingCoreType = processor.get("starting_core_type").asText();
        String switchCoreType = processor.get("switch_core_type").asText();
        int numCores = processor.get("num_cores").asInt();
        String isa = processor.get("isa").asText();

        ObjectNode board = (ObjectNode) jsonData.get("commonParameters").get("board");
        String clkFreq = board.get("clk_freq").asText();

        ObjectNode binary = (ObjectNode) jsonData.get("gem5").get("binary");
        String binaryName = binary.get("name").asText();

        StringBuilder pythonCodeBuilder = new StringBuilder();
        pythonCodeBuilder.append("from gem5.utils.requires import requires\n");
        pythonCodeBuilder.append("from gem5.components.boards.simple_board import SimpleBoard\n");
        pythonCodeBuilder.append("from gem5.components.memory.single_channel import SingleChannelDDR3_1600\n");
        pythonCodeBuilder.append("from gem5.components.cachehierarchies.ruby.mesi_two_level_cache_hierarchy import MESITwoLevelCacheHierarchy\n");
        pythonCodeBuilder.append("from gem5.components.processors.simple_switchable_processor import SimpleSwitchableProcessor\n");
        pythonCodeBuilder.append("from gem5.coherence_protocol import CoherenceProtocol\n");
        pythonCodeBuilder.append("from gem5.isas import ISA\n");
        pythonCodeBuilder.append("from gem5.components.processors.cpu_types import CPUTypes\n");
        pythonCodeBuilder.append("from gem5.resources.resource import Resource\n");
        pythonCodeBuilder.append("from gem5.simulate.simulator import Simulator\n\n");

        pythonCodeBuilder.append("requires(isa_required=ISA." + isa + ", coherence_protocol_required=CoherenceProtocol.MESI_TWO_LEVEL)\n\n");

        pythonCodeBuilder.append("cache_hierarchy = MESITwoLevelCacheHierarchy(\n");
        pythonCodeBuilder.append("    l1d_size=\"" + l1dSize + "\",\n");
        pythonCodeBuilder.append("    l1d_assoc=" + l1dAssoc + ",\n");
        pythonCodeBuilder.append("    l1i_size=\"" + l1iSize + "\",\n");
        pythonCodeBuilder.append("    l1i_assoc=" + l1iAssoc + ",\n");
        pythonCodeBuilder.append("    l2_size=\"" + l2Size + "\",\n");
        pythonCodeBuilder.append("    l2_assoc=" + l2Assoc + ",\n");
        pythonCodeBuilder.append("    num_l2_banks=" + numL2Banks + "\n");
        pythonCodeBuilder.append(")\n\n");

        pythonCodeBuilder.append("memory = SingleChannelDDR3_1600(\"" + memorySize + "\")\n\n");

        pythonCodeBuilder.append("processor = SimpleSwitchableProcessor(\n");
        pythonCodeBuilder.append("    starting_core_type=CPUTypes." + startingCoreType + ",\n");
        pythonCodeBuilder.append("    switch_core_type=CPUTypes." + switchCoreType + ",\n");
        pythonCodeBuilder.append("    num_cores=" + numCores + ",\n");
        pythonCodeBuilder.append("    isa=ISA." + isa + "\n");
        pythonCodeBuilder.append(")\n\n");

        pythonCodeBuilder.append("board = SimpleBoard(\n");
        pythonCodeBuilder.append("    clk_freq=\"" + clkFreq + "\",\n");
        pythonCodeBuilder.append("    processor=processor,\n");
        pythonCodeBuilder.append("    memory=memory,\n");
        pythonCodeBuilder.append("    cache_hierarchy=cache_hierarchy\n");
        pythonCodeBuilder.append(")\n\n");

        pythonCodeBuilder.append("binary = Resource(\"" + binaryName + "\")\n\n");

        pythonCodeBuilder.append("board.set_se_binary_workload(binary)\n\n");

        pythonCodeBuilder.append("simulator = Simulator(board=board)\n");
        pythonCodeBuilder.append("simulator.run()\n");

        return pythonCodeBuilder.toString();
    }
}
