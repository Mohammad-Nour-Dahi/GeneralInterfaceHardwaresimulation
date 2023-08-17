package gihs.sniper.input;

import com.fasterxml.jackson.databind.JsonNode;
import gihs.core.input.GenerateInputParametersAbstract;

/**
 * The SniperInput class implements the GenerateInputParameters interface to generate input code
 * for the Sniper simulator based on the provided JSON data.
 */
public class SniperInput extends GenerateInputParametersAbstract {
    /**
     * Constructs a new instance of the SniperInput class.
     *
     * @param parametersFormInputJSON The input JSON node.
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
        StringBuilder cfgCodeBuilder = null;
        try {

            // Add general section
            cfgCodeBuilder = new StringBuilder();
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
            cfgCodeBuilder.append("dispatch_width = 2\n");
            cfgCodeBuilder.append("window_size = 32\n");
            cfgCodeBuilder.append("num_outstanding_loadstores = 10\n\n");

            // Add perf_model/sync section
            cfgCodeBuilder.append("[perf_model/sync]\n");
            cfgCodeBuilder.append("reschedule_cost =1000\n\n");

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
            cfgCodeBuilder.append("address_hash = mask\n");
            cfgCodeBuilder.append("replacement_policy = lru\n");
            cfgCodeBuilder.append("data_access_time = 4\n");
            cfgCodeBuilder.append("tags_access_time = 1\n");
            cfgCodeBuilder.append("perf_model_type = parallel\n");
            cfgCodeBuilder.append("writethrough = 0\n");
            cfgCodeBuilder.append("shared_cores =1\n");


            // Add perf_model/l1_dcache section
            cfgCodeBuilder.append("[perf_model/l1_dcache]\n");
            cfgCodeBuilder.append("perfect = false\n");
            cfgCodeBuilder.append("cache_size = ").append(l1dSize.replaceAll("[^0-9]", "")).append("\n");
            cfgCodeBuilder.append("associativity = ").append(l1dAssoc).append("\n");
            cfgCodeBuilder.append("address_hash = mask\n");
            cfgCodeBuilder.append("replacement_policy = lru\n");
            cfgCodeBuilder.append("data_access_time = 4\n");
            cfgCodeBuilder.append("tags_access_time = 1\n");
            cfgCodeBuilder.append("perf_model_type = parallel\n");
            cfgCodeBuilder.append("writethrough = 0\n");
            cfgCodeBuilder.append("shared_cores = 1\n");

            // Add perf_model/l2_cache section
            cfgCodeBuilder.append("[perf_model/l2_cache]\n");
            cfgCodeBuilder.append("perfect = false\n");
            cfgCodeBuilder.append("cache_size = ").append(l2Size.replaceAll("[^0-9]", "")).append("\n");
            cfgCodeBuilder.append("associativity = ").append(l2Assoc).append("\n");
            cfgCodeBuilder.append("address_hash = mask\n");
            cfgCodeBuilder.append("replacement_policy = lru\n");
            cfgCodeBuilder.append("data_access_time = 12\n");
            cfgCodeBuilder.append("tags_access_time = 3\n");
            cfgCodeBuilder.append("writeback_time = 50\n");
            cfgCodeBuilder.append("perf_model_type = parallel\n");
            cfgCodeBuilder.append("writethrough =0\n");
            cfgCodeBuilder.append("shared_cores =2\n");
            cfgCodeBuilder.append("dvfs_domain =global\n");
            cfgCodeBuilder.append("prefetcher =none\n\n");

            // Add perf_model/dram_directory section
            cfgCodeBuilder.append("[perf_model/dram_directory]\n");
            cfgCodeBuilder.append("total_entries = ").append(parametersFormInputJSON.get("sniper").get("perf_model/dram_directory").get("total_entries").asInt()).append("\n");
            cfgCodeBuilder.append("associativity = ").append(parametersFormInputJSON.get("sniper").get("perf_model/dram_directory").get("associativity").asInt()).append("\n");
            cfgCodeBuilder.append("directory_type = full_map\n\n");

            // Add perf_model/dram section
            cfgCodeBuilder.append("[perf_model/dram]\n");
            cfgCodeBuilder.append("num_controllers = -1\n");
            cfgCodeBuilder.append("controllers_interleaving =4\n");
            cfgCodeBuilder.append("latency =45\n");
            cfgCodeBuilder.append("per_controller_bandwidth =12.8\n");
            cfgCodeBuilder.append("chips_per_dimm =8 \n");
            cfgCodeBuilder.append("dimms_per_controller =4 \n\n");

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
            cfgCodeBuilder.append("transition_latency =2000\n\n");

            // Add dvfs/simple section
            cfgCodeBuilder.append("[dvfs/simple]\n");
            cfgCodeBuilder.append("cores_per_socket = 1\n\n");

            // Add power section
            cfgCodeBuilder.append("[power]\n");
            cfgCodeBuilder.append("vdd = 1.0 \n");
            cfgCodeBuilder.append("technology_node = 22 \n");
        } catch (NullPointerException e) {
            // Handle the case when the JsonNode is null
            System.err.println("Error: The \"sniper\" section contains a null JsonNode. Please ensure that the input data is correctly formatted and all required fields are provided in the \"sniper\" section.");
        }
        return cfgCodeBuilder.toString();
    }
}
