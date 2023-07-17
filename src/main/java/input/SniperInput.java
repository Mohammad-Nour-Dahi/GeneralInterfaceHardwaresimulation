package input;

import com.fasterxml.jackson.databind.JsonNode;
import managementOFJsonNodeALL.JsonNodeALL;

/**
 * The SniperInput class implements the GenerateInputParameters interface to generate input code
 * for the Sniper simulator based on the provided JSON data.
 */
public class SniperInput extends GenerateInputParametersImplementation {
    /**

     Constructs a new instance of the SniperInput class.

     @param parametersFormInputJSON The input JSON node.
     */
    public SniperInput(JsonNode parametersFormInputJSON) {
        super(parametersFormInputJSON);
    }

    /**
     * Generates input code for the Sniper simulator based on the provided JSON data.
     *
     * @return The generated input code as a String.
     */
    @Override
    public String generateInputCode() {

        // Add general section
        StringBuilder cfgCodeBuilder = new StringBuilder();
        cfgCodeBuilder.append("[general]\n");
        cfgCodeBuilder.append("enable_icache_modeling = true\n\n");

        // Add perf_model/core section
        cfgCodeBuilder.append("[perf_model/core]\n");
        cfgCodeBuilder.append("frequency = " + frequency.replaceAll("[^0-9]", "")).append("\n");
        cfgCodeBuilder.append("logical_cpus = ").append(parametersFormInputJSON.get("sniper").get("perf_model/core").get("logical_cpus").asInt()).append("\n");
        cfgCodeBuilder.append("type = ").append(parametersFormInputJSON.get("sniper").get("perf_model/core").get("type").asText()).append("\n");
        cfgCodeBuilder.append("core_model = nehalem \n\n");

        // Add perf_model/core/interval_timer section
        cfgCodeBuilder.append("[perf_model/core/interval_timer]\n");
        cfgCodeBuilder.append("dispatch_width = ").append(parametersFormInputJSON.get("sniper").get("perf_model/core/interval_timer").get("dispatch_width").asInt()).append("\n");
        cfgCodeBuilder.append("window_size = ").append(parametersFormInputJSON.get("sniper").get("perf_model/core/interval_timer").get("window_size").asInt()).append("\n");
        cfgCodeBuilder.append("num_outstanding_loadstores = ").append(parametersFormInputJSON.get("sniper").get("perf_model/core/interval_timer").get("num_outstanding_loadstores").asInt()).append("\n\n");

        // Add perf_model/sync section
        cfgCodeBuilder.append("[perf_model/sync]\n");
        cfgCodeBuilder.append("reschedule_cost = ").append(parametersFormInputJSON.get("sniper").get("perf_model/sync").get("reschedule_cost").asInt()).append("\n\n");

        // Add caching_protocol section
        cfgCodeBuilder.append("[caching_protocol]\n");
        cfgCodeBuilder.append("type = parametric_dram_directory_msi\n\n");

        // Add perf_model/branch_predictor section
        cfgCodeBuilder.append("[perf_model/branch_predictor]\n");
        cfgCodeBuilder.append("type = pentium_m \n");
        cfgCodeBuilder.append("mispredict_penalty = 8 \n\n");

        // Add perf_model/tlb section
        cfgCodeBuilder.append("[perf_model/tlb]\n");
        cfgCodeBuilder.append("penalty = 30 \n\n");

        // Add perf_model/itlb section
        cfgCodeBuilder.append("[perf_model/itlb]\n");
        cfgCodeBuilder.append("size = ").append(parametersFormInputJSON.get("sniper").get("perf_model/itlb").get("size").asInt()).append("\n");
        cfgCodeBuilder.append("associativity = ").append(parametersFormInputJSON.get("sniper").get("perf_model/itlb").get("associativity").asInt()).append("\n\n");

        // Add perf_model/dtlb section
        cfgCodeBuilder.append("[perf_model/dtlb]\n");
        cfgCodeBuilder.append("size = ").append(parametersFormInputJSON.get("sniper").get("perf_model/dtlb").get("size").asInt()).append("\n");
        cfgCodeBuilder.append("associativity = ").append(parametersFormInputJSON.get("sniper").get("perf_model/dtlb").get("associativity").asInt()).append("\n\n");

        // Add perf_model/stlb section
        cfgCodeBuilder.append("[perf_model/stlb]\n");
        cfgCodeBuilder.append("size = ").append(parametersFormInputJSON.get("sniper").get("perf_model/stlb").get("size").asInt()).append("\n");
        cfgCodeBuilder.append("associativity = ").append(parametersFormInputJSON.get("sniper").get("perf_model/stlb").get("associativity").asInt()).append("\n\n");

        // Add perf_model/cache section
        cfgCodeBuilder.append("[perf_model/cache]\n");
        cfgCodeBuilder.append("levels = 2 \n\n");

        // Add perf_model/l1_icache section
        cfgCodeBuilder.append("[perf_model/l1_icache]\n");
        cfgCodeBuilder.append("perfect = false\n");
        cfgCodeBuilder.append("cache_size = ").append(l1iSize.replaceAll("[^0-9]", "")).append("\n");
        cfgCodeBuilder.append("associativity = ").append(l1iAssoc).append("\n");
        cfgCodeBuilder.append("address_hash = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l1_icache.address_hash").asText()).append("\n");
        cfgCodeBuilder.append("replacement_policy = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l1_icache.replacement_policy").asText()).append("\n");
        cfgCodeBuilder.append("data_access_time = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l1_icache.data_access_time").asText()).append("\n");
        cfgCodeBuilder.append("tags_access_time = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l1_icache.tags_access_time").asText()).append("\n");
        cfgCodeBuilder.append("perf_model_type = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l1_icache.perf_model_type").asText()).append("\n");
        cfgCodeBuilder.append("writethrough = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l1_icache.writethrough").asText()).append("\n");
        cfgCodeBuilder.append("shared_cores = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l1_icache.shared_cores").asText()).append("\n");


        // Add perf_model/l1_dcache section
        cfgCodeBuilder.append("[perf_model/l1_dcache]\n");
        cfgCodeBuilder.append("perfect = false\n");
        cfgCodeBuilder.append("cache_size = ").append(l1dSize.replaceAll("[^0-9]", "")).append("\n");
        cfgCodeBuilder.append("associativity = ").append(l1dAssoc).append("\n");
        cfgCodeBuilder.append("address_hash = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l1_dcache.address_hash").asText()).append("\n");
        cfgCodeBuilder.append("replacement_policy = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l1_dcache.replacement_policy").asText()).append("\n");
        cfgCodeBuilder.append("data_access_time = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l1_dcache.data_access_time").asText()).append("\n");
        cfgCodeBuilder.append("tags_access_time = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l1_dcache.tags_access_time").asText()).append("\n");
        cfgCodeBuilder.append("perf_model_type = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l1_dcache.perf_model_type").asText()).append("\n");
        cfgCodeBuilder.append("writethrough = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l1_dcache.writethrough").asText()).append("\n");
        cfgCodeBuilder.append("shared_cores = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l1_dcache.shared_cores").asText()).append("\n");

        // Add perf_model/l2_cache section
        cfgCodeBuilder.append("[perf_model/l2_cache]\n");
        cfgCodeBuilder.append("perfect = false\n");
        cfgCodeBuilder.append("cache_size = ").append(l2Size.replaceAll("[^0-9]", "")).append("\n");
        cfgCodeBuilder.append("associativity = ").append(l2Assoc).append("\n");
        cfgCodeBuilder.append("address_hash = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l2_cache.address_hash").asText()).append("\n");
        cfgCodeBuilder.append("replacement_policy = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l2_cache.replacement_policy").asText()).append("\n");
        cfgCodeBuilder.append("data_access_time = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l2_cache.data_access_time").asText()).append("\n");
        cfgCodeBuilder.append("tags_access_time = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l2_cache.tags_access_time").asText()).append("\n");
        cfgCodeBuilder.append("writeback_time = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l2_cache.writeback_time").asText()).append("\n");
        cfgCodeBuilder.append("perf_model_type = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l2_cache.perf_model_type").asText()).append("\n");
        cfgCodeBuilder.append("writethrough = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l2_cache.writethrough").asText()).append("\n");
        cfgCodeBuilder.append("shared_cores = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l2_cache.shared_cores").asText()).append("\n");
        cfgCodeBuilder.append("dvfs_domain = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l2_cache.dvfs_domain").asText()).append("\n");
        cfgCodeBuilder.append("prefetcher = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "sniper.perf_model/l2_cache.prefetcher").asText()).append("\n\n");

        // Add perf_model/dram_directory section
        cfgCodeBuilder.append("[perf_model/dram_directory]\n");
        cfgCodeBuilder.append("total_entries = ").append(parametersFormInputJSON.get("sniper").get("perf_model/dram_directory").get("total_entries").asInt()).append("\n");
        cfgCodeBuilder.append("associativity = ").append(parametersFormInputJSON.get("sniper").get("perf_model/dram_directory").get("associativity").asInt()).append("\n");
        cfgCodeBuilder.append("directory_type = ").append(parametersFormInputJSON.get("sniper").get("perf_model/dram_directory").get("directory_type").asText()).append("\n\n");

        // Add perf_model/dram section
        cfgCodeBuilder.append("[perf_model/dram]\n");
        cfgCodeBuilder.append("num_controllers = -1\n");
        cfgCodeBuilder.append("controllers_interleaving = ").append(parametersFormInputJSON.get("sniper").get("perf_model/dram").get("controllers_interleaving").asInt()).append("\n");
        cfgCodeBuilder.append("latency = ").append(parametersFormInputJSON.get("sniper").get("perf_model/dram").get("latency").asInt()).append("\n");
        cfgCodeBuilder.append("per_controller_bandwidth = ").append(parametersFormInputJSON.get("sniper").get("perf_model/dram").get("per_controller_bandwidth").asDouble()).append("\n");
        cfgCodeBuilder.append("chips_per_dimm = ").append(parametersFormInputJSON.get("sniper").get("perf_model/dram").get("chips_per_dimm").asInt()).append("\n");
        cfgCodeBuilder.append("dimms_per_controller = ").append(parametersFormInputJSON.get("sniper").get("perf_model/dram").get("dimms_per_controller").asInt()).append("\n\n");

        // Add network section.get("sniper")
        cfgCodeBuilder.append("[network]\n");
        cfgCodeBuilder.append("memory_model_1 = bus\n");
        cfgCodeBuilder.append("memory_model_2 = bus\n\n");

        // Add network/bus section
        cfgCodeBuilder.append("[network/bus]\n");
        cfgCodeBuilder.append("bandwidth = 128 \n");
        cfgCodeBuilder.append("ignore_local_traffic = false\n\n");

        // Add clock_skew_minimization section
        cfgCodeBuilder.append("[clock_skew_minimization]\n");
        cfgCodeBuilder.append("scheme = barrier \n\n");

        // Add clock_skew_minimization/barrier section
        cfgCodeBuilder.append("[clock_skew_minimization/barrier]\n");
        cfgCodeBuilder.append("quantum = 100 \n\n");

        // Add dvfs section
        cfgCodeBuilder.append("[dvfs]\n");
        cfgCodeBuilder.append("transition_latency = ").append(parametersFormInputJSON.get("sniper").get("dvfs").get("transition_latency").asInt()).append("\n\n");

        // Add dvfs/simple section
        cfgCodeBuilder.append("[dvfs/simple]\n");
        cfgCodeBuilder.append("cores_per_socket = ").append(parametersFormInputJSON.get("sniper").get("dvfs/simple").get("cores_per_socket").asInt()).append("\n\n");

        // Add power section
        cfgCodeBuilder.append("[power]\n");
        cfgCodeBuilder.append("vdd = 1.0 \n");
        cfgCodeBuilder.append("technology_node = 22 \n");


        return cfgCodeBuilder.toString();
    }
}
