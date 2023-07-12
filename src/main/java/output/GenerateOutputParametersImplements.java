package output;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * GenerateOutputParametersImplements is a class that implements the GenerateOutputParameters interface.
 * It provides basic functionality for generating output parameters in JSON format.
 */
public class GenerateOutputParametersImplements implements GenerateOutputParameters {
    protected ObjectMapper objectMapper;



    /**
     * Default constructor for GenerateOutputParametersImplements.
     * Initializes the ObjectMapper, jsonObject, and resultJson.
     */
    GenerateOutputParametersImplements() {
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
    public String generateStatisticsParametersJson(String filePath) {
        return null;
    }

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
}