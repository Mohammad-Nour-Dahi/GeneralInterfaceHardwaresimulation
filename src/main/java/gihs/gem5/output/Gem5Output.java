package gihs.gem5.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gihs.core.output.GenerateOutputParametersAbstract;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Gem5Output class implements the GenerateOutputParameters interface
 * and provides functionality to generate statistics parameters in JSON format
 * based on a file containing simulation data.
 */
public class Gem5Output extends GenerateOutputParametersAbstract {
    private static final double NANOSECONDS_IN_SECOND = 1.0e9;
    private Map<String, String> parameterMap = new HashMap<>(Map.of(
            "simInsts", "Instructions",
            "board.processor.start0.core.numCycles", "Cycles",
            "simSeconds", "Time (ns)",
            "hostSeconds","HostNanoseconds"

    ));

    /**
     * Constructs a Gem5Output object and initializes the parameter map with additional entries.
     */
    public Gem5Output() {


        parameterMap.put("board.cache_hierarchy.ruby_system.l1_controllers0.L1Icache.m_demand_accesses", "Cache Summary.Cache L1-I.num cache accesses");
        parameterMap.put("board.cache_hierarchy.ruby_system.l1_controllers1.L1Icache.m_demand_accesses", "Cache Summary.Cache L1-I.num cache accesses");
        parameterMap.put("board.cache_hierarchy.ruby_system.l1_controllers0.L1Icache.m_demand_misses", "Cache Summary.Cache L1-I.num cache misses");
        parameterMap.put("board.cache_hierarchy.ruby_system.l1_controllers1.L1Icache.m_demand_misses", "Cache Summary.Cache L1-I.num cache misses");
        parameterMap.put("board.cache_hierarchy.ruby_system.l1_controllers0.L1Dcache.m_demand_accesses", "Cache Summary.Cache L1-D.num cache accesses");
        parameterMap.put("board.cache_hierarchy.ruby_system.l1_controllers1.L1Dcache.m_demand_accesses", "Cache Summary.Cache L1-D.num cache accesses");
        parameterMap.put("board.cache_hierarchy.ruby_system.l1_controllers0.L1Dcache.m_demand_misses", "Cache Summary.Cache L1-D.num cache misses");
        parameterMap.put("board.cache_hierarchy.ruby_system.l1_controllers1.L1Dcache.m_demand_misses", "Cache Summary.Cache L1-D.num cache misses");
        parameterMap.put("board.cache_hierarchy.ruby_system.l2_controllers.L2cache.m_demand_accesses", "Cache Summary.Cache L2.num cache accesses");
        parameterMap.put("board.cache_hierarchy.ruby_system.l2_controllers.L2cache.m_demand_misses", "Cache Summary.Cache L2.num cache misses");
    }

    /**
     * Generate a JSON string representing the statistics parameters based on the specified file path.
     *
     * @param filePath The path to the file containing statistics data.
     * @return A formatted JSON string representing the statistics parameters.
     */
    @Override
    public String generateStatisticsParametersJson(String filePath) {
        ObjectNode jsonOutputParameter = generateStatisticsJson(filePath);
        ObjectNode resultJson = objectMapper.createObjectNode();

        parameterMap.forEach((key, outputParameter) -> {
            if (jsonOutputParameter.has(key)) {
                JsonNode value = jsonOutputParameter.get(key);
                if (outputParameter.equals("Cycles") || outputParameter.equals("Instructions")) {
                    resultJson.put(outputParameter, value.asInt());
                } else {
                    resultJson.set(outputParameter, value);
                }
            }
        });

        // num cache misses = l1_controllers0.L1Icache.m_demand_misses + l1_controllers1.L1Icache.m_demand_misses
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "Cache Summary.Cache L1-I.num cache misses"), "Cache Summary.Cache L1-I.num cache misses", jsonOutputParameter, resultJson);
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "Cache Summary.Cache L1-D.num cache misses"), "Cache Summary.Cache L1-D.num cache misses", jsonOutputParameter, resultJson);
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "Cache Summary.Cache L2.num cache misses"), "Cache Summary.Cache L2.num cache misses", jsonOutputParameter, resultJson);

        calculateSimulationResultGem5AndZsim(parameterMap, jsonOutputParameter, resultJson);

        // Multiply the value of "Time (ns)" in resultJson by NANOSECONDS_IN_SECOND
        resultJson.put("Time (ns)", findPropertyValue(resultJson, "Time (ns)").asDouble() * NANOSECONDS_IN_SECOND);
        // Multiply the value of "HostNanoseconds" in resultJson by NANOSECONDS_IN_SECOND
        resultJson.put("HostNanoseconds", findPropertyValue(resultJson, "HostNanoseconds").asDouble() * NANOSECONDS_IN_SECOND);

        String outputResultJson = "";
        try {
            outputResultJson = objectMapper.writer().with(SerializationFeature.INDENT_OUTPUT).writeValueAsString(resultJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return outputResultJson;
    }

    /**
     * Generate a JSON string based on the statistics data in the specified file.
     *
     * @param filePath The path to the file containing statistics data.
     * @return A JSON string representing the statistics data.
     */
    private ObjectNode generateStatisticsJson(String filePath) {
        ObjectNode jsonOutputParameter = objectMapper.createObjectNode();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                int commentIndex = line.indexOf("#");
                if (commentIndex != -1) {
                    line = line.substring(0, commentIndex).trim();
                }
                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }
                String[] parts = line.split("\\s+", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    jsonOutputParameter.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonOutputParameter;
    }

    /**
     * Adds keys with the same value to the given ObjectNode and stores the sum of values under the targetValue in the resultObject.
     *
     * @param keys         The keys with the same value from the parameterMap.
     * @param targetValue  The key to store the sum of values in the resultObject.
     * @param jsonObject   The ObjectNode from which the values are retrieved.
     * @param resultObject The ObjectNode where the result is stored.
     */
    @Override
    protected void addKeysWithSameValue(List<String> keys, String targetValue, ObjectNode jsonObject, ObjectNode resultObject) {
        ObjectMapper objectMapper = new ObjectMapper();
        int sum = keys.stream()
                .map(e -> jsonObject.get(e).asInt())
                .mapToInt(Integer::intValue)
                .sum();

        resultObject.set(targetValue, objectMapper.valueToTree(sum));
    }


}
