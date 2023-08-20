package gihs.zsim.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gihs.core.output.GenerateOutputParametersAbstract;
import gihs.core.managementOFJsonNodeALL.JsonUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * ZsimOutput class extends GenerateOutputParametersImplements and represents a class for generating Zsim output.
 */
public class ZsimOutput extends GenerateOutputParametersAbstract {
    /**
     * Mapping of parameter keys to output parameter names
     */
    private Map<String, String> parameterMap = new HashMap<>();

    /**
     * List of cache miss values for Cache Summary.Cache L1-D.
     */
    private List<String> cacheSummaryCacheL1DNumCacheMisses;
    /**
     * List of cache miss values for Cache Summary.Cache L1-I.
     */
    private List<String> cacheSummaryCacheL1INumCacheMisses;
    /**
     * List of cache miss values for Cache Summary.Cache L2.
     */
    private List<String> cacheSummaryCacheL2NumCacheMisses;

    /**
     * Initializes the parameter map with additional mappings.
     */
    public ZsimOutput() {
        parameterMap.put("root.skylake.skylake-0.instrs", "Instructions");
        parameterMap.put("root.skylake.skylake-0.cycles", "Cycles");
        parameterMap.put("root.contention.domain-0.time", "Time (ns)");
        parameterMap.put("root.time.init","HostNanoseconds");
        parameterMap.put("root.time.bound","HostNanoseconds");
        parameterMap.put("root.time.weave","HostNanoseconds");

        // The value of "Cache Summary.Cache L1-D.num cache accesses" is the sum of these keys: fhGETS fhGETX hGETS hGETX mGETS mGETXIM mGETXSM
        parameterMap.put("root.l1d.l1d-0.fhGETS", "Cache Summary.Cache L1-D.num cache accesses");
        parameterMap.put("root.l1d.l1d-0.fhGETX", "Cache Summary.Cache L1-D.num cache accesses");
        parameterMap.put("root.l1d.l1d-0.hGETS", "Cache Summary.Cache L1-D.num cache accesses");
        parameterMap.put("root.l1d.l1d-0.hGETX", "Cache Summary.Cache L1-D.num cache accesses");
        parameterMap.put("root.l1d.l1d-0.mGETS", "Cache Summary.Cache L1-D.num cache accesses");
        parameterMap.put("root.l1d.l1d-0.mGETXIM", "Cache Summary.Cache L1-D.num cache accesses");
        parameterMap.put("root.l1d.l1d-0.mGETXSM", "Cache Summary.Cache L1-D.num cache accesses");

        // The value of "Cache Summary.Cache L1-D.num cache misses" is the sum of these keys:  mGETS mGETXIM mGETXSM
        cacheSummaryCacheL1DNumCacheMisses = Arrays.asList(
                "root.l1d.l1d-0.mGETS",
                "root.l1d.l1d-0.mGETXIM",
                "root.l1d.l1d-0.mGETXSM"
        );


        // The value of "Cache Summary.Cache L1-I.num cache accesses" is the sum of these keys: fhGETS fhGETX hGETS hGETX mGETS mGETXIM mGETXSM
        parameterMap.put("root.l1i.l1i-0.fhGETS", "Cache Summary.Cache L1-I.num cache accesses");
        parameterMap.put("root.l1i.l1i-0.fhGETX", "Cache Summary.Cache L1-I.num cache accesses");
        parameterMap.put("root.l1i.l1i-0.hGETS", "Cache Summary.Cache L1-I.num cache accesses");
        parameterMap.put("root.l1i.l1i-0.hGETX", "Cache Summary.Cache L1-I.num cache accesses");
        parameterMap.put("root.l1i.l1i-0.mGETS", "Cache Summary.Cache L1-I.num cache accesses");
        parameterMap.put("root.l1i.l1i-0.mGETXIM", "Cache Summary.Cache L1-I.num cache accesses");
        parameterMap.put("root.l1i.l1i-0.mGETXSM", "Cache Summary.Cache L1-I.num cache accesses");


        // The value of "Cache Summary.Cache L1-I.num cache misses" is the sum of these keys:  mGETS mGETXIM mGETXSM
        cacheSummaryCacheL1INumCacheMisses = Arrays.asList(
                "root.l1i.l1i-0.mGETS",
                "root.l1i.l1i-0.mGETXIM",
                "root.l1i.l1i-0.mGETXSM"
        );

        // The value of "Cache Summary.Cache L2.num cache accesses" is the sum of these keys: hGETS hGETX mGETS mGETXIM mGETXSM
        parameterMap.put("root.l2.l2-0.hGETS", "Cache Summary.Cache L2.num cache accesses");
        parameterMap.put("root.l2.l2-0.hGETX", "Cache Summary.Cache L2.num cache accesses");
        parameterMap.put("root.l2.l2-0.mGETS", "Cache Summary.Cache L2.num cache accesses");
        parameterMap.put("root.l2.l2-0.mGETXIM", "Cache Summary.Cache L2.num cache accesses");
        parameterMap.put("root.l2.l2-0.mGETXSM", "Cache Summary.Cache L2.num cache accesses");


        // The value of "Cache Summary.Cache L2.num cache misses" is the sum of these keys:  mGETS mGETXIM mGETXSM
        cacheSummaryCacheL2NumCacheMisses = Arrays.asList(
                "root.l2.l2-0.mGETS",
                "root.l2.l2-0.mGETXIM",
                "root.l2.l2-0.mGETXSM"
        );

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
        ObjectNode resultStatsJson = objectMapper.createObjectNode();
        String outputResultJson = "";
        // Generate the initial JSON object
        ObjectNode jsonStatsFromSimulation = generateStatisticsJsonZsim(filePath);

        // Iterate over the parameterMap and update the resultJson with matching values from the jsonObject
        parameterMap.forEach((key, outputParameter) -> {
            if (JsonUtil.has(jsonStatsFromSimulation, key)) {
                JsonNode value = JsonUtil.get(jsonStatsFromSimulation, key);
                resultStatsJson.set(outputParameter, value);
            }
        });
        // Num cache misses
        addKeysWithSameValue(cacheSummaryCacheL1DNumCacheMisses, "Cache Summary.Cache L1-D.num cache misses",
                jsonStatsFromSimulation, resultStatsJson);
        addKeysWithSameValue(cacheSummaryCacheL1INumCacheMisses, "Cache Summary.Cache L1-I.num cache misses",
                jsonStatsFromSimulation, resultStatsJson);
        addKeysWithSameValue(cacheSummaryCacheL2NumCacheMisses, "Cache Summary.Cache L2.num cache misses",
                jsonStatsFromSimulation, resultStatsJson);
        addKeysWithSameValue(getKeysWithSameValue(parameterMap, "HostNanoseconds"),
                "HostNanoseconds", jsonStatsFromSimulation, resultStatsJson);

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

         try {
            // Create an ObjectWriter with indentation enabled
            ObjectWriter objectWriter = objectMapper.writer().with(SerializationFeature.INDENT_OUTPUT);
            outputResultJson = objectWriter.writeValueAsString(resultStatsJson);
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
    private ObjectNode generateStatisticsJsonZsim(String filePath) {
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
                    key = statistics.has(key) ? key + " " + i++ : key;

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
        // Create the newStatistics object to hold the transformed JSON
        ObjectNode newStatistics = objectMapper.createObjectNode();

        // Create block objects to hold different levels of nested JSON
        ObjectNode block = objectMapper.createObjectNode();
        ObjectNode innerBlock = objectMapper.createObjectNode();
        ObjectNode innerInnerBlock = objectMapper.createObjectNode();

        // Initialize variables to track current keys and levels
        String blockKey = "";
        String innerBlockKey = "";

        // Iterate over the fields in the input statistics object
        Iterator<Map.Entry<String, JsonNode>> iterator = statistics.fields();

        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            // Check the pattern of the key and organize the JSON structure accordingly
            if (key.matches(" [a-zA-Z].*")) {
                // Handling main block or element
                if (!blockKey.isEmpty()) {
                    // Handling nested blocks or elements
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
                // Handling inner block
                if (!innerBlockKey.isEmpty()) {
                    // Handling nested inner blocks
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
                // Handling outer block
                blockKey = key.trim();
                block = objectMapper.createObjectNode();
                innerBlock = objectMapper.createObjectNode();
                innerBlockKey = "";
                newStatistics.set(blockKey, block);
            } else if (key.matches("   [a-zA-Z].*")) {
                // Handling element in innermost block
                innerInnerBlock.set(keyWithoutNumber(key).trim(), value);
            }
        }

        return newStatistics;
    }


}