package input;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The GenerateInputParameters interface defines a contract for generating input code based on JSON data.
 */
public interface GenerateInputParameters {

    /**
     * Generates input code based on the provided JSON data.
     *
     * @param jsonData The JSON data to generate input code from.
     * @return The generated input code as a String.
     */
    String generateInputCode(JsonNode jsonData);
}
