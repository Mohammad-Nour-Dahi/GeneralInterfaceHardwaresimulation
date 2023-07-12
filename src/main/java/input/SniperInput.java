package input;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SniperInput implements GenerateInputParameters {
    @Override
    public String generateInputCode(JsonNode jsonData) {
        JsonNode cacheHierarchy = jsonData.get("commonParameters").get("cache_hierarchy");
        String l1dSize = cacheHierarchy.get("l1d_size").asText();
        int l1dAssoc = cacheHierarchy.get("l1d_assoc").asInt();
        String l1iSize = cacheHierarchy.get("l1i_size").asText();
        int l1iAssoc = cacheHierarchy.get("l1i_assoc").asInt();
        String l2Size = cacheHierarchy.get("l2_size").asText();
        int l2Assoc = cacheHierarchy.get("l2_assoc").asInt();
        // Add general section
        StringBuilder cfgCodeBuilder = new StringBuilder();
        cfgCodeBuilder.append("[general]\n");
        cfgCodeBuilder.append("enable_icache_modeling = true\n\n");

// Add perf_model/core section
        cfgCodeBuilder.append("[perf_model/core]\n");
        cfgCodeBuilder.append("frequency = ").append(jsonData.get("commonParameters").get("board").get("clk_freq").asText().replaceAll("GHz","")).append("\n");
        cfgCodeBuilder.append("logical_cpus = ").append(jsonData.get("sniper").get("perf_model/core").get("logical_cpus").asInt()).append("\n");
        cfgCodeBuilder.append("type = ").append(jsonData.get("sniper").get("perf_model/core").get("type").asText()).append("\n");
        cfgCodeBuilder.append("core_model = ").append(jsonData.get("sniper").get("perf_model/core").get("core_model").asText()).append("\n\n");

// Add perf_model/core/interval_timer section
        cfgCodeBuilder.append("[perf_model/core/interval_timer]\n");
        cfgCodeBuilder.append("dispatch_width = ").append(jsonData.get("sniper").get("perf_model/core/interval_timer").get("dispatch_width").asInt()).append("\n");
        cfgCodeBuilder.append("window_size = ").append(jsonData.get("sniper").get("perf_model/core/interval_timer").get("window_size").asInt()).append("\n");
        cfgCodeBuilder.append("num_outstanding_loadstores = ").append(jsonData.get("sniper").get("perf_model/core/interval_timer").get("num_outstanding_loadstores").asInt()).append("\n\n");

// Add perf_model/sync section
        cfgCodeBuilder.append("[perf_model/sync]\n");
        cfgCodeBuilder.append("reschedule_cost = ").append(jsonData.get("sniper").get("perf_model/sync").get("reschedule_cost").asInt()).append("\n\n");

// Add caching_protocol section
        cfgCodeBuilder.append("[caching_protocol]\n");
        cfgCodeBuilder.append("type = parametric_dram_directory_msi\n\n");

// Add perf_model/branch_predictor section
        cfgCodeBuilder.append("[perf_model/branch_predictor]\n");
        cfgCodeBuilder.append("type = ").append(jsonData.get("sniper").get("perf_model/branch_predictor").get("type").asText()).append("\n");
        cfgCodeBuilder.append("mispredict_penalty = ").append(jsonData.get("sniper").get("perf_model/branch_predictor").get("mispredict_penalty").asInt()).append("\n\n");

// Add perf_model/tlb section
        cfgCodeBuilder.append("[perf_model/tlb]\n");
        cfgCodeBuilder.append("penalty = ").append(jsonData.get("sniper").get("perf_model/tlb").get("penalty").asInt()).append("\n\n");

// Add perf_model/itlb section
        cfgCodeBuilder.append("[perf_model/itlb]\n");
        cfgCodeBuilder.append("size = ").append(jsonData.get("sniper").get("perf_model/itlb").get("size").asInt()).append("\n");
        cfgCodeBuilder.append("associativity = ").append(jsonData.get("sniper").get("perf_model/itlb").get("associativity").asInt()).append("\n\n");

// Add perf_model/dtlb section
        cfgCodeBuilder.append("[perf_model/dtlb]\n");
        cfgCodeBuilder.append("size = ").append(jsonData.get("sniper").get("perf_model/dtlb").get("size").asInt()).append("\n");
        cfgCodeBuilder.append("associativity = ").append(jsonData.get("sniper").get("perf_model/dtlb").get("associativity").asInt()).append("\n\n");

// Add perf_model/stlb section
        cfgCodeBuilder.append("[perf_model/stlb]\n");
        cfgCodeBuilder.append("size = ").append(jsonData.get("sniper").get("perf_model/stlb").get("size").asInt()).append("\n");
        cfgCodeBuilder.append("associativity = ").append(jsonData.get("sniper").get("perf_model/stlb").get("associativity").asInt()).append("\n\n");

// Add perf_model/cache section
        cfgCodeBuilder.append("[perf_model/cache]\n");
        cfgCodeBuilder.append("levels = ").append(jsonData.get("sniper").get("perf_model/cache").get("levels").asInt()).append("\n\n");

// Add perf_model/l1_icache section
        cfgCodeBuilder.append("[perf_model/l1_icache]\n");
        cfgCodeBuilder.append("perfect = false\n");
        cfgCodeBuilder.append("cache_size = ").append(l1iSize.replaceAll("KiB","")).append("\n");
        cfgCodeBuilder.append("associativity = ").append(l1iAssoc).append("\n");
        cfgCodeBuilder.append("address_hash = mask\n");
        cfgCodeBuilder.append("replacement_policy = lru\n");
        cfgCodeBuilder.append("data_access_time = 4\n");
        cfgCodeBuilder.append("tags_access_time = 1\n");
        cfgCodeBuilder.append("perf_model_type = parallel\n");
        cfgCodeBuilder.append("writethrough = 0\n");
        cfgCodeBuilder.append("shared_cores = 1\n\n");

// Add perf_model/l1_dcache section
        cfgCodeBuilder.append("[perf_model/l1_dcache]\n");
        cfgCodeBuilder.append("perfect = false\n");
        cfgCodeBuilder.append("cache_size = ").append(l1dSize.replaceAll("KiB","")).append("\n");
        cfgCodeBuilder.append("associativity = ").append(l1dAssoc).append("\n");
        cfgCodeBuilder.append("address_hash = mask\n");
        cfgCodeBuilder.append("replacement_policy = lru\n");
        cfgCodeBuilder.append("data_access_time = 4\n");
        cfgCodeBuilder.append("tags_access_time = 1\n");
        cfgCodeBuilder.append("perf_model_type = parallel\n");
        cfgCodeBuilder.append("writethrough = 0\n");
        cfgCodeBuilder.append("shared_cores = 1\n\n");

// Add perf_model/l2_cache section
        cfgCodeBuilder.append("[perf_model/l2_cache]\n");
        cfgCodeBuilder.append("perfect = false\n");
        cfgCodeBuilder.append("cache_size = ").append(l2Size.replaceAll("kB","")).append("\n");
        cfgCodeBuilder.append("associativity = ").append(l2Assoc).append("\n");
        cfgCodeBuilder.append("address_hash = mask\n");
        cfgCodeBuilder.append("replacement_policy = lru\n");
        cfgCodeBuilder.append("data_access_time = 12\n");
        cfgCodeBuilder.append("tags_access_time = 3\n");
        cfgCodeBuilder.append("writeback_time = 50\n");
        cfgCodeBuilder.append("perf_model_type = parallel\n");
        cfgCodeBuilder.append("writethrough = 0\n");
        cfgCodeBuilder.append("shared_cores = 2\n");
        cfgCodeBuilder.append("dvfs_domain = global\n");
        cfgCodeBuilder.append("prefetcher = none\n\n");

// Add perf_model/dram_directory section
        cfgCodeBuilder.append("[perf_model/dram_directory]\n");
        cfgCodeBuilder.append("total_entries = ").append(jsonData.get("sniper").get("perf_model/dram_directory").get("total_entries").asInt()).append("\n");
        cfgCodeBuilder.append("associativity = ").append(jsonData.get("sniper").get("perf_model/dram_directory").get("associativity").asInt()).append("\n");
        cfgCodeBuilder.append("directory_type = ").append(jsonData.get("sniper").get("perf_model/dram_directory").get("directory_type").asText()).append("\n\n");

// Add perf_model/dram section
        cfgCodeBuilder.append("[perf_model/dram]\n");
        cfgCodeBuilder.append("num_controllers = -1\n");
        cfgCodeBuilder.append("controllers_interleaving = ").append(jsonData.get("sniper").get("perf_model/dram").get("controllers_interleaving").asInt()).append("\n");
        cfgCodeBuilder.append("latency = ").append(jsonData.get("sniper").get("perf_model/dram").get("latency").asInt()).append("\n");
        cfgCodeBuilder.append("per_controller_bandwidth = ").append(jsonData.get("sniper").get("perf_model/dram").get("per_controller_bandwidth").asDouble()).append("\n");
        cfgCodeBuilder.append("chips_per_dimm = ").append(jsonData.get("sniper").get("perf_model/dram").get("chips_per_dimm").asInt()).append("\n");
        cfgCodeBuilder.append("dimms_per_controller = ").append(jsonData.get("sniper").get("perf_model/dram").get("dimms_per_controller").asInt()).append("\n\n");

// Add network section.get("sniper")
        cfgCodeBuilder.append("[network]\n");
        cfgCodeBuilder.append("memory_model_1 = bus\n");
        cfgCodeBuilder.append("memory_model_2 = bus\n\n");

// Add network/bus section
        cfgCodeBuilder.append("[network/bus]\n");
        cfgCodeBuilder.append("bandwidth = ").append(jsonData.get("sniper").get("network/bus").get("bandwidth").asInt()).append("\n");
        cfgCodeBuilder.append("ignore_local_traffic = false\n\n");

// Add clock_skew_minimization section
        cfgCodeBuilder.append("[clock_skew_minimization]\n");
        cfgCodeBuilder.append("scheme = ").append(jsonData.get("sniper").get("clock_skew_minimization").get("scheme").asText()).append("\n\n");

// Add clock_skew_minimization/barrier section
        cfgCodeBuilder.append("[clock_skew_minimization/barrier]\n");
        cfgCodeBuilder.append("quantum = ").append(jsonData.get("sniper").get("clock_skew_minimization/barrier").get("quantum").asInt()).append("\n\n");

// Add dvfs section
        cfgCodeBuilder.append("[dvfs]\n");
        cfgCodeBuilder.append("transition_latency = ").append(jsonData.get("sniper").get("dvfs").get("transition_latency").asInt()).append("\n\n");

// Add dvfs/simple section
        cfgCodeBuilder.append("[dvfs/simple]\n");
        cfgCodeBuilder.append("cores_per_socket = ").append(jsonData.get("sniper").get("dvfs/simple").get("cores_per_socket").asInt()).append("\n\n");

// Add power section
        cfgCodeBuilder.append("[power]\n");
        cfgCodeBuilder.append("vdd = ").append(jsonData.get("sniper").get("power").get("vdd").asDouble()).append("\n");
        cfgCodeBuilder.append("technology_node = ").append(jsonData.get("sniper").get("power").get("technology_node").asInt()).append("\n");


        return cfgCodeBuilder.toString();
    }
}
