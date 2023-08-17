package gihs.core.output;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gihs.core.managementOFJsonNodeALL.JsonNodeALL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * GenerateOutputParametersImplements is an abstract class that implements the GenerateOutputParameters interface.
 * It provides basic functionality for generating output parameters in JSON format.
 */
public abstract class GenerateOutputParametersImplements implements GenerateOutputParameters {
    /**
     * The objectMapper field is an instance of the ObjectMapper class from the Jackson library,
     * which is used for converting Java objects to JSON strings.
     */
    protected ObjectMapper objectMapper;


    /**
     * Default constructor for GenerateOutputParametersImplements.
     * Initializes the ObjectMapper.
     */
    protected GenerateOutputParametersImplements() {
        objectMapper = new ObjectMapper();


    }

    /**
     * Generate a JSON parameter that uses a specified file path.
     * This method is overridden by subclasses to generate specific statistics parameters.
     *
     * @param filePath The path to the file containing statistics data.
     * @return A formatted JSON string representing the statistics parameters.
     */
    @Override
    public abstract String generateStatisticsParametersJson(String filePath);

    /**
     * Parse a value into a JsonNode based on its data type.
     * The value can be empty, a percentage, a decimal, or an integer.
     *
     * @param value The value to be parsed.
     * @return A JsonNode representing the parsed value.
     */
    protected JsonNode parseValue(String value) {
        if (value.isEmpty()) {
            return objectMapper.valueToTree(null);
        } else if (value.endsWith("%")) {
            return objectMapper.valueToTree(Double.parseDouble(value.substring(0, value.length() - 1)));
        } else if (value.contains(".")) {
            return objectMapper.valueToTree(Double.parseDouble(value));
        } else {
            return objectMapper.valueToTree(Integer.parseInt(value));
        }
    }

    /**
     * Removes the numeric part from a key if it contains any.
     *
     * @param key The key from which to remove the numeric part.
     * @return The key without the numeric part.
     */
    protected String keyWithoutNumber(String key) {
        if (key.matches(".*\\s\\d+.*")) {
            return key.replaceFirst(" \\d+", "");
        } else {
            return key;
        }
    }

    /**
     * Get the keys from the parameter map that have the same target value.
     *
     * @param parameterMap The parameter map to search in.
     * @param targetValue  The target value to match.
     * @return A list of keys with the same target value.
     */
    protected List<String> getKeysWithSameValue(Map<String, String> parameterMap, String targetValue) {
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
     * Adds keys with the same value to the given ObjectNode and stores the sum of values under the targetValue in the resultObject.
     *
     * @param keys         The keys with the same value from the parameterMap.
     * @param targetValue  The key to store the sum of values in the resultObject.
     * @param jsonObject   The ObjectNode from which the values are retrieved.
     * @param resultObject The ObjectNode where the result is stored.
     */
    protected void addKeysWithSameValue(List<String> keys, String targetValue, ObjectNode jsonObject, ObjectNode resultObject) {
        ObjectMapper objectMapper = new ObjectMapper();
        int sum = keys.stream()
                .map(e -> JsonNodeALL.getALL(jsonObject, e).asInt())
                .mapToInt(Integer::intValue)
                .sum();

        resultObject.set(targetValue, objectMapper.valueToTree(sum));
    }

    /**
     * Calculate the MPKI (Misses Per Thousand Instructions) based on the instruction count and cache misses.
     *
     * @param instructionsKey The key for the instruction count in the result JSON.
     * @param numMissesKey    The key for the cache misses in the result JSON.
     * @param mpkiKey         The key for the calculated MPKI in the result JSON.
     * @param resultJson      The ObjectNode representing the result JSON.
     */
    protected void calculateMPKI(String instructionsKey, String numMissesKey, String mpkiKey, ObjectNode resultJson) {

            int numInstructions = findPropertyValue(resultJson,instructionsKey).asInt();
            int numCacheMisses = findPropertyValue(resultJson,numMissesKey).asInt();

            double mpki = ((double) numCacheMisses / numInstructions) * 1000;

            resultJson.put(mpkiKey, roundToTwoDecimals(mpki));

    }

    /**
     * Calculate the cache miss rate based on the cache accesses and cache misses values.
     *
     * @param cacheAccessesKey The key for cache accesses in the result JSON.
     * @param cacheMissesKey   The key for cache misses in the result JSON.
     * @param rateKey          The key for the calculated miss rate in the result JSON.
     * @param resultJson       The ObjectNode representing the result JSON.
     */
    protected void calculateRate(String cacheAccessesKey, String cacheMissesKey, String rateKey, ObjectNode resultJson) {
        resultJson.put(rateKey, roundToTwoDecimals(divideNumbers(cacheMissesKey, cacheAccessesKey, resultJson)*100) + "%");

    }
    /**
     * Divides two numbers and returns the result.
     *
     * @param numeratorKey   The key to retrieve the numerator value from the JSON object.
     * @param denominatorKey The key to retrieve the denominator value from the JSON object.
     * @param resultJson     The JSON object containing the values.
     * @return The result of dividing the numerator by the denominator. If the denominator is 0, returns 0.00.
     */
    protected double divideNumbers(String numeratorKey, String denominatorKey, ObjectNode resultJson) {

        double result = 0.00;

        double numerator = findPropertyValue(resultJson, numeratorKey).asDouble();
        double denominator = findPropertyValue(resultJson, denominatorKey).asDouble();

        if (denominator != 0) {
            result = numerator / denominator;

        }

        return result;
    }

    /**
     * Rounds a double value to two decimal places.
     *
     * @param value The value to be rounded.
     * @return The rounded value.
     */
    protected double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    /**
     * Finds the value of a property in the given JSON object.
     *
     * @param resultStatsJson The JSON object to search in.
     * @param propertyToFind  The property to find the value of.
     * @return The value of the property as a JsonNode, or null if not found.
     */
    protected JsonNode findPropertyValue(ObjectNode resultStatsJson, String propertyToFind) {
        JsonNode valueNode = resultStatsJson.get(propertyToFind);
        if (valueNode != null && valueNode.isValueNode()) {
            return valueNode;
        }
        return null;
    }

    /**
     * Calculates simulation results for the Gem5 and Zsim simulations based on the given parameter map and statistics JSON.
     *
     * @param parameterMap            The parameter map containing the keys and target values to consider.
     * @param jsonStatsFromSimulation The JSON object containing the simulation statistics.
     * @param resultStatsJson         The JSON object to store the calculated results.
     */
    protected void calculateSimulationResultGem5AndZsim(Map<String, String> parameterMap, ObjectNode jsonStatsFromSimulation, ObjectNode resultStatsJson) {
        // Num cache accesses
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "Cache Summary.Cache L1-I.num cache accesses"),
                "Cache Summary.Cache L1-I.num cache accesses", jsonStatsFromSimulation, resultStatsJson);
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "Cache Summary.Cache L1-D.num cache accesses"),
                "Cache Summary.Cache L1-D.num cache accesses", jsonStatsFromSimulation, resultStatsJson);
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "Cache Summary.Cache L2.num cache accesses"),
                "Cache Summary.Cache L2.num cache accesses", jsonStatsFromSimulation, resultStatsJson);

        // Calculate cache miss rates
        calculateRate("Cache Summary.Cache L2.num cache accesses", "Cache Summary.Cache L2.num cache misses",
                "Cache Summary.Cache L2.miss rate", resultStatsJson);
        calculateRate("Cache Summary.Cache L1-D.num cache accesses", "Cache Summary.Cache L1-D.num cache misses",
                "Cache Summary.Cache L1-D.miss rate", resultStatsJson);
        calculateRate("Cache Summary.Cache L1-I.num cache accesses", "Cache Summary.Cache L1-I.num cache misses",
                "Cache Summary.Cache L1-I.miss rate", resultStatsJson);

        // Calculate MPKI
        calculateMPKI("Instructions", "Cache Summary.Cache L2.num cache misses", "Cache Summary.Cache L2.mpki",
                resultStatsJson);
        calculateMPKI("Instructions", "Cache Summary.Cache L1-D.num cache misses", "Cache Summary.Cache L1-D.mpki",
                resultStatsJson);
        calculateMPKI("Instructions", "Cache Summary.Cache L1-I.num cache misses", "Cache Summary.Cache L1-I.mpki",
                resultStatsJson);

        // Calculate IPC
        resultStatsJson.put("IPC", roundToTwoDecimals(divideNumbers("Instructions", "Cycles", resultStatsJson)));
    }


}