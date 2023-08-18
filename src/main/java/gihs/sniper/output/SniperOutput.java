package gihs.sniper.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gihs.core.output.GenerateOutputParametersAbstract;
import gihs.core.managementOFJsonNodeALL.JsonUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The SniperOutput class extends GenerateOutputParametersImplements and represents a class for generating Sniper output.
 * and provides methods to generate statistics parameters in JSON format
 * based on a given file path.
 */
public class SniperOutput extends GenerateOutputParametersAbstract {
    // Map of parameter names to be included in the output JSON
    private Map<String, String> parameterMap = new HashMap<>();



    /**
     * Constructs a SniperOutput object and initializes the parameter map with additional entries.
     */
    public SniperOutput(){
        parameterMap.put("Time (ns)","Time (ns)");
        parameterMap.put("Instructions","Instructions");
        parameterMap.put("Cycles","Cycles");
        parameterMap.put("IPC","IPC");
        parameterMap.put("Cache Summary.Cache L1-I.num cache misses","Cache Summary.Cache L1-I.num cache misses");
        parameterMap.put("Cache Summary.Cache L1-D.num cache accesses","Cache Summary.Cache L1-D.num cache accesses");
        parameterMap.put("Cache Summary.Cache L2.num cache misses","Cache Summary.Cache L2.num cache misses");
        parameterMap.put("Cache Summary.Cache L1-I.num cache accesses", "Cache Summary.Cache L1-I.num cache accesses");
        parameterMap.put("Cache Summary.Cache L1-D.num cache misses", "Cache Summary.Cache L1-D.num cache misses");
        parameterMap.put("Cache Summary.Cache L2.num cache accesses","Cache Summary.Cache L2.num cache accesses");
        parameterMap.put("Cache Summary.Cache L2.miss rate","Cache Summary.Cache L2.miss rate");
        parameterMap.put("Cache Summary.Cache L1-D.miss rate", "Cache Summary.Cache L1-D.miss rate");
        parameterMap.put("Cache Summary.Cache L1-I.miss rate","Cache Summary.Cache L1-I.miss rate");
        parameterMap.put("Cache Summary.Cache L2.mpki", "Cache Summary.Cache L2.mpki");
        parameterMap.put("Cache Summary.Cache L1-D.mpki","Cache Summary.Cache L1-D.mpki");
        parameterMap.put( "Cache Summary.Cache L1-I.mpki", "Cache Summary.Cache L1-I.mpki");

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
        JsonNode generateHardwaresimulationOutputParametersJson = objectMapper.createObjectNode();
        ObjectNode resultJson = objectMapper.createObjectNode();
        String outputResultJson = "";
        String generateHardwaresimulationOutputParameters = generateStatisticsParametersJsonSniper(filePath);
        try {
            generateHardwaresimulationOutputParametersJson = objectMapper.readTree(generateHardwaresimulationOutputParameters);
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
        }


        JsonNode finalGenerateHardwaresimulationOutputParametersJson = generateHardwaresimulationOutputParametersJson;
        // Iterate through the parameterMap and add parameters to the resultJson
        parameterMap.forEach((key, outputParameter) -> {
            if (JsonUtil.has(finalGenerateHardwaresimulationOutputParametersJson,outputParameter)) {
                JsonNode value = JsonUtil.get(finalGenerateHardwaresimulationOutputParametersJson,key);
                //Adds a percent symbol (%) to the value of a parameter that ends with "rate".
                if (outputParameter.endsWith("rate")) {
                    String valueWithPercent = value.asText() + "%";
                    resultJson.put(outputParameter, valueWithPercent);
                } else {
                    resultJson.set(outputParameter, value);
                }
            }
        });


        try {
            // Convert the resultJson to a formatted JSON string
            ObjectWriter objectWriter = objectMapper.writer().with(SerializationFeature.INDENT_OUTPUT);
            outputResultJson = objectWriter.writeValueAsString(resultJson);
        } catch (JsonProcessingException e) {
            System.err.println("Error writing JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return outputResultJson;
    }

    /**
     * Generates the statistics parameters in JSON format specific to Sniper
     * based on the given file path.
     *
     * @param filePath the path of the file containing the statistics data
     * @return the JSON representation of the Sniper-specific statistics parameters
     */
    private String generateStatisticsParametersJsonSniper(String filePath) {
        ObjectNode statistics = objectMapper.createObjectNode();
        ObjectNode newStatistics = objectMapper.createObjectNode();

        int i = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("|") && !line.contains("Core 0")) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 2) {
                        String key = parts[0];
                        String value = parts[1].trim();
                        // Check if the key already exists in the statistics object, and add an index if necessary
                        key = statistics.has(key) ? key + i++ : key;
                        statistics.set(key, parseValue(value));
                    }
                }
            }

            newStatistics = getSniperJson(statistics);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(newStatistics);
        } catch (JsonProcessingException e) {
            System.err.println("Error writing JSON: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generates the Sniper-specific statistics parameters in JSON format based on the given statistics.
     *
     * @param statistics The statistics object containing the raw data.
     * @return The JSON representation of the Sniper-specific statistics parameters.
     */
    private ObjectNode getSniperJson(ObjectNode statistics) {
        ;
        ObjectNode newStatistics = objectMapper.createObjectNode();

        Iterator<Map.Entry<String, JsonNode>> iterator = statistics.fields();
        ObjectNode block = objectMapper.createObjectNode();
        ObjectNode innerBlock = objectMapper.createObjectNode();
        String key = null;
        String blockKey = "";
        String innerBlockKey = "";

        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            key = keyWithoutNumber(entry.getKey());
            JsonNode value = entry.getValue();

            if (key.matches("  [a-zA-Z].*")) {
                if (blockKey.length() != 0) {
                    if (value.toString().equals("null")) {
                        innerBlock = objectMapper.createObjectNode();
                        innerBlockKey = key.trim();
                    }
                    if (innerBlockKey.length() == 0) {
                        block.set(key.trim(), value);
                    } else {
                        block.set(innerBlockKey.trim(), innerBlock);
                    }
                } else {
                    newStatistics.set(key.trim(), value);
                }
            } else if (key.matches("[a-zA-Z].*")) {
                blockKey = key.trim();
                block = objectMapper.createObjectNode();
                innerBlock = objectMapper.createObjectNode();
                innerBlockKey = "";
                newStatistics.set(blockKey, block);
            } else if (key.matches("    [a-zA-Z].*")) {
                innerBlock.set(key.trim(), value);
            }
        }
        return newStatistics;
    }


}
