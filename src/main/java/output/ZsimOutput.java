package output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import managementOFJsonNodeALL.JsonNodeALL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * ZsimOutput class extends GenerateOutputParametersImplements and represents a class for generating Zsim output.
 */
public class ZsimOutput extends GenerateOutputParametersImplements {

    // Mapping of parameter keys to output parameter names
    private Map<String, String> parameterMap = new HashMap<>(Map.of(
            "root.skylake.skylake-0.instrs", "Instructions",
            "root.skylake.skylake-0.cycles", "Cycles",
            "root.contention.domain-0.time", "Time (ns)",
            "root.skylake.skylake-0.mispredBranches", "Branch predictor stats.misprediction rate"
    ));

    /**
     *
     * Initializes the parameter map with additional mappings.
     */
    public ZsimOutput() {
        parameterMap.put("board.cache_hierarchy.ruby_system.l1_controllers1.L1Icache.m_demand_accesses", "Cache Summary.Cache L1-I.num cache accesses");
        parameterMap.put("board.cache_hierarchy.ruby_system.l1_controllers0.L1Icache.m_demand_misses", "Cache Summary.Cache L1-I.num cache misses");
        parameterMap.put("root.l1i.l1i-0.mGETS", "Cache Summary.Cache L1-I.num cache misses");
        parameterMap.put("board.cache_hierarchy.ruby_system.l1_controllers0.L1Dcache.m_demand_accesses", "Cache Summary.Cache L1-D.num cache accesses");
        parameterMap.put("board.cache_hierarchy.ruby_system.l1_controllers1.L1Dcache.m_demand_accesses", "Cache Summary.Cache L1-D.num cache accesses");
        parameterMap.put("root.l1d.l1d-0.mGETS", "Cache Summary.Cache L1-D.num cache misses");
        parameterMap.put("board.cache_hierarchy.ruby_system.l1_controllers1.L1Dcache.m_demand_misses", "Cache Summary.Cache L1-D.num cache misses");
        parameterMap.put("board.cache_hierarchy.ruby_system.l2_controllers.L2cache.m_demand_accesses", "Cache Summary.Cache L2.num cache accesses");
        parameterMap.put("root.l2.l2-0.mGETS", "Cache Summary.Cache L2.num cache misses");
    }

    /**
     * Generate a JSON parameter that uses a specified file path.
     * The generated JSON parameter should contain the mapping of simulation results
     * with the output parameters for the general interface.
     *
     * @param filePath The path to the file containing statistics data.
     * @return A formatted JSON string representing the statistics parameters.
     */
    @Override
    public String generateStatisticsParametersJson(String filePath) {
        ObjectNode resultJson= objectMapper.createObjectNode();
        String outputResultJson = "";
        // Generate the initial JSON object
        JsonNode jsonObject = generateStatisticsJsonZsim(filePath);

        // Iterate over the parameterMap and update the resultJson with matching values from the jsonObject
        parameterMap.forEach((key, outputParameter) -> {
            if (JsonNodeALL.hasALL(jsonObject, key)) {
                JsonNode value = JsonNodeALL.getALL(jsonObject, key);
                resultJson.set(outputParameter, value);
            }
        });


        try {
            // Create an ObjectWriter with indentation enabled
            ObjectWriter objectWriter = objectMapper.writer().with(SerializationFeature.INDENT_OUTPUT);
            outputResultJson = objectWriter.writeValueAsString(resultJson);
        } catch (JsonProcessingException e) {
            System.err.println("Error writing JSON: " + e.getMessage());
            e.printStackTrace();
        }
        return outputResultJson;
    }

    /**
     * Generates a JSON object containing statistics data based on the given file path.
     *
     * @param filePath The path to the file containing statistics data.
     * @return A JSON object representing the statistics data.
     */
    private JsonNode generateStatisticsJsonZsim(String filePath) {
        // Create the initial statistics and newStatistics objects
        ObjectNode statistics = objectMapper.createObjectNode();
        ObjectNode newStatistics = objectMapper.createObjectNode();

        int i = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check for comments and remove them
                int commentIndex = line.indexOf("#");
                if (commentIndex != -1) {
                    line = line.substring(0, commentIndex);
                }
                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }

                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String key = parts[0];
                    String value = parts[1].trim();
                    // Check if the key already exists in the statistics object, and add an index if necessary
                    key = statistics.has(key) ? key + i++ : key;

                    // Parse the value and add it to the statistics object
                    statistics.set(key, parseValue(value));
                }
            }

            // Generate the newStatistics object using the statistics object
            newStatistics = getZsimJson(statistics);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        return newStatistics;
    }

    /**
     * Generates a modified JSON object for Zsim statistics based on the given statistics object.
     *
     * @param statistics The original statistics object.
     * @return A modified JSON object representing Zsim statistics.
     */
    private ObjectNode getZsimJson(ObjectNode statistics) {
        // Create the newStatistics object
        ObjectNode newStatistics = objectMapper.createObjectNode();

        // Iterate over the fields in the statistics object
        Iterator<Map.Entry<String, JsonNode>> iterator = statistics.fields();
        ObjectNode block = objectMapper.createObjectNode();
        ObjectNode innerBlock = objectMapper.createObjectNode();
        ObjectNode innerInnerBlock = objectMapper.createObjectNode();
        String blockKey = "";
        String innerBlockKey = "";


        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            // Check the pattern of the key and organize the JSON structure accordingly
            if (key.matches(" [a-zA-Z].*")) {
                if (!blockKey.isEmpty()) {
                    if (value.isNull()) {
                        innerBlock = objectMapper.createObjectNode();
                        innerInnerBlock = objectMapper.createObjectNode();
                        innerBlockKey = key.trim();
                    }
                    if (!value.isNull()) {
                        block.set(key.trim(), value);
                    } else {
                        block.set(innerBlockKey.trim(), innerBlock);
                    }
                } else {
                    newStatistics.set(key.trim(), value);
                }
            } else if (key.matches("  [a-zA-Z].*")) {
                if (!innerBlockKey.isEmpty()) {
                    if (value.isNull()) {
                        innerInnerBlock = objectMapper.createObjectNode();

                    }
                    if (!value.isNull()) {
                        innerBlock.set(key.trim(), value);
                    } else {
                        innerBlock.set(key.trim(), innerInnerBlock);
                    }
                } else {
                    innerInnerBlock.set(key.trim(), value);
                }
            } else if (key.matches("[a-zA-Z].*")) {
                blockKey = key.trim();
                block = objectMapper.createObjectNode();
                innerBlock = objectMapper.createObjectNode();
                innerBlockKey = "";
                newStatistics.set(blockKey, block);
            } else if (key.matches("   [a-zA-Z].*")) {
                innerInnerBlock.set(key.trim(), value);
            }
        }

        return newStatistics;
    }

}