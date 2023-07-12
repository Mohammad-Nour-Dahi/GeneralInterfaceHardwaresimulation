package output;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * The Gem5Output class implements the GenerateOutputParameters interface
 * and provides functionality to generate statistics parameters in JSON format
 * based on a file containing simulation data.
 */
public class Gem5Output implements GenerateOutputParameters {

    private Map<String, String> parameterMap = new HashMap<>(Map.of(
            "simInsts", "Instructions",
            "board.processor.start0.core.numCycles", "Cycles",
            "simSeconds", "Time (ns)",
            "board.processor.switch0.core.branchPred.condPredicted", "Branch predictor stats.num correct",
            "board.processor.switch1.core.branchPred.condPredicted", "Branch predictor stats.num correct",
            "board.processor.switch0.core.branchPred.condIncorrect", "Branch predictor stats.num incorrect",
            "board.processor.switch1.core.branchPred.condIncorrect", "Branch predictor stats.num incorrect",
            "board.processor.start0.core.mmu.itb.rdAccesses", "TLB Summary.I-TLB.num accesses",
            "board.processor.start0.core.mmu.itb.wrAccesses", "TLB Summary.I-TLB.num accesses",
            "board.processor.start1.core.mmu.itb.rdAccesses", "TLB Summary.I-TLB.num accesses"
    ));

    /**
     * Constructs a Gem5Output object and initializes the parameter map with additional entries.
     */
    public Gem5Output() {
        parameterMap.put("board.processor.start1.core.mmu.itb.wrAccesses", "TLB Summary.I-TLB.num accesses");
        parameterMap.put("board.processor.switch0.core.mmu.itb.rdMisses", "TLB Summary.I-TLB.num misses");
        parameterMap.put("board.processor.switch0.core.mmu.itb.wrMisses", "TLB Summary.I-TLB.num misses");
        parameterMap.put("board.processor.switch1.core.mmu.itb.rdMisses", "TLB Summary.I-TLB.num misses");
        parameterMap.put("board.processor.switch1.core.mmu.itb.wrMisses", "TLB Summary.I-TLB.num misses");
        parameterMap.put("board.processor.start0.core.mmu.dtb.rdAccesses", "TLB Summary.D-TLB.num accesses");
        parameterMap.put("board.processor.start0.core.mmu.dtb.wrAccesses", "TLB Summary.D-TLB.num accesses");
        parameterMap.put("board.processor.start1.core.mmu.dtb.rdAccesses", "TLB Summary.D-TLB.num accesses");
        parameterMap.put("board.processor.start1.core.mmu.dtb.wrAccesses", "TLB Summary.D-TLB.num accesses");
        parameterMap.put("board.processor.switch0.core.mmu.dtb.rdMisses", "TLB Summary.D-TLB.num misses");
        parameterMap.put("board.processor.switch0.core.mmu.dtb.wrMisses", "TLB Summary.D-TLB.num misses");
        parameterMap.put("board.processor.switch1.core.mmu.dtb.rdMisses", "TLB Summary.D-TLB.num misses");
        parameterMap.put("board.processor.switch1.core.mmu.dtb.wrMisses", "TLB Summary.D-TLB.num misses");
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
        String outputResultJson = "";
        String jsonOutputParamater = generateStatisticsJson(filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonObject = objectMapper.createObjectNode();
        ObjectNode resultJson = objectMapper.createObjectNode();

        try {
            jsonObject = (ObjectNode) objectMapper.readTree(jsonOutputParamater);
        } catch (Exception e) {
            e.printStackTrace();
        }
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "Branch predictor stats.num correct"), jsonObject);
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "Branch predictor stats.num incorrect"), jsonObject);
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "TLB Summary.I-TLB.num accesses"), jsonObject);
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "TLB Summary.I-TLB.num misses"), jsonObject);
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "TLB Summary.D-TLB.num accesses"), jsonObject);
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "TLB Summary.D-TLB.num misses"), jsonObject);
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "Cache Summary.Cache L1-I.num cache accesses"), jsonObject);
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "Cache Summary.Cache L1-D.num cache accesses"), jsonObject);
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "Cache Summary.Cache L1-I.num cache misses"), jsonObject);
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "Cache Summary.Cache L1-D.num cache misses"), jsonObject);

        ObjectNode finalJsonObject = jsonObject;
        parameterMap.forEach((key, outputParameter) -> {
            if (finalJsonObject.has(key)) {
                JsonNode value = finalJsonObject.get(key);
                resultJson.set(outputParameter, value);
            }
        });

        calculateMissRate("Cache Summary.Cache L2.num cache accesses", "Cache Summary.Cache L2.num cache misses",
                "Cache Summary.Cache L2.miss rate", resultJson);
        calculateMissRate("Cache Summary.Cache L1-D.num cache accesses", "Cache Summary.Cache L1-D.num cache misses",
                "Cache Summary.Cache L1-D.miss rate", resultJson);
        calculateMissRate("Cache Summary.Cache L1-I.num cache accesses", "Cache Summary.Cache L1-I.num cache misses",
                "Cache Summary.Cache L1-I.miss rate", resultJson);

        calculateMPKI("Instructions", "Cache Summary.Cache L2.num cache misses", "Cache Summary.Cache L2.mpki", resultJson);
        calculateMPKI("Instructions", "Cache Summary.Cache L1-D.num cache misses", "Cache Summary.Cache L1-D.mpki", resultJson);
        calculateMPKI("Instructions", "Cache Summary.Cache L1-I.num cache misses", "Cache Summary.Cache L1-I.mpki", resultJson);


        ObjectWriter objectWriter = objectMapper.writer().with(SerializationFeature.INDENT_OUTPUT);


        try {
            outputResultJson = objectWriter.writeValueAsString(resultJson);
        } catch (Exception e) {
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
    private String generateStatisticsJson(String filePath) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

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
                    jsonBuilder.append("\"").append(key).append("\": \"").append(value).append("\",");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (jsonBuilder.length() > 1) {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1); // Remove the trailing comma
        }

        jsonBuilder.append("}");

        return jsonBuilder.toString();
    }

    /**
     * Add keys with the same value to the given ObjectNode.
     *
     * @param keys       The keys with the same value.
     * @param jsonObject The ObjectNode to add the keys to.
     */
    private void addKeysWithSameValue(List<String> keys, ObjectNode jsonObject) {
        ObjectMapper objectMapper = new ObjectMapper();
        int sum = keys.stream()
                .map(e -> jsonObject.get(e).asInt())
                .mapToInt(Integer::intValue)
                .sum();

        jsonObject.set(keys.get(1), objectMapper.valueToTree(sum));
    }

    /**
     * Adjust the values in the result JSON by performing specific transformations.
     *
     * @param resultJson The ObjectNode representing the result JSON.
     * @return The adjusted ObjectNode.
     */
    private ObjectNode adjustTheValues(ObjectNode resultJson) {
        adjustValue(resultJson, "Time (ns)", "" + (int) (getOldValue(resultJson, "Time (ns)").asDouble() * 1000000000));
        return resultJson;
    }

    /**
     * Adjust the value of a property in the result JSON.
     *
     * @param resultJson     The ObjectNode representing the result JSON.
     * @param propertyToFind The property to find and adjust the value.
     * @param newValue       The new value for the property.
     */
    private void adjustValue(ObjectNode resultJson, String propertyToFind, String newValue) {
        JsonNode valueNode = resultJson.findValue(propertyToFind);
        if (valueNode != null) {
            if (valueNode.isValueNode()) {
                // Change the value
                resultJson.put(propertyToFind, newValue);
            } else {
                // Case: Value is a complex object (e.g., another ObjectNode or an array)
                // Add additional adjustments if necessary.
            }
        }
    }

    /**
     * Get the old value of a property from the result JSON.
     *
     * @param resultJson     The ObjectNode representing the result JSON.
     * @param propertyToFind The property to find and get the old value.
     * @return The old value as a JsonNode, or null if not found.
     */
    private JsonNode getOldValue(ObjectNode resultJson, String propertyToFind) {
        JsonNode valueNode = resultJson.get(propertyToFind);
        if (valueNode != null && valueNode.isValueNode()) {
            return valueNode;
        }
        return null;
    }

    /**
     * Get the keys from the parameter map that have the same target value.
     *
     * @param parameterMap The parameter map to search in.
     * @param targetValue  The target value to match.
     * @return A list of keys with the same target value.
     */
    private List<String> getKeysWithSameValue(Map<String, String> parameterMap, String targetValue) {
        List<String> keysWithSameValue = new ArrayList<>();

        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value.equals(targetValue)) {
                keysWithSameValue.add(key);
            }
        }

        return keysWithSameValue;
    }

    /**
     * Calculate the cache miss rate based on the cache accesses and cache misses values.
     *
     * @param cacheAccessesKey The key for cache accesses in the result JSON.
     * @param cacheMissesKey   The key for cache misses in the result JSON.
     * @param missRateKey      The key for the calculated miss rate in the result JSON.
     * @param resultJson       The ObjectNode representing the result JSON.
     */
    private void calculateMissRate(String cacheAccessesKey, String cacheMissesKey, String missRateKey, ObjectNode resultJson) {
        JsonNode numCacheAccessesNode = resultJson.get(cacheAccessesKey);
        JsonNode numCacheMissesNode = resultJson.get(cacheMissesKey);

        if (numCacheMissesNode != null && numCacheAccessesNode != null) {
            int numCacheMisses = numCacheMissesNode.asInt();
            int numCacheAccesses = numCacheAccessesNode.asInt();

            double missRate = (double) numCacheMisses / numCacheAccesses * 100.0;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String formattedMissRate = decimalFormat.format(missRate);

            resultJson.put(missRateKey, formattedMissRate + "%");
        }
    }

    /**
     * Calculate the MPKI (Misses Per Thousand Instructions) based on the instruction count and cache misses.
     *
     * @param instructionsKey The key for the instruction count in the result JSON.
     * @param numMissesKey    The key for the cache misses in the result JSON.
     * @param mpkiKey         The key for the calculated MPKI in the result JSON.
     * @param resultJson      The ObjectNode representing the result JSON.
     */
    private void calculateMPKI(String instructionsKey, String numMissesKey, String mpkiKey, ObjectNode resultJson) {
        JsonNode instructionsNode = resultJson.get(instructionsKey);
        JsonNode numMissesNode = resultJson.get(numMissesKey);

        if (instructionsNode != null && numMissesNode != null) {
            int numInstructions = instructionsNode.asInt();
            int numCacheMisses = numMissesNode.asInt();

            double mpki = ((double) numCacheMisses / numInstructions) * 1000;

            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String formattedMPKI = decimalFormat.format(mpki);

            resultJson.put(mpkiKey, formattedMPKI);
        }
    }
}
