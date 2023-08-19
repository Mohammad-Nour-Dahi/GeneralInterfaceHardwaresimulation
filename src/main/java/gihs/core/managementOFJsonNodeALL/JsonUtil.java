package gihs.core.managementOFJsonNodeALL;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;

/**
 * A utility class for managing JSON nodes.
 */
public class JsonUtil {

    /**
     * Checks if the given JSON node has all the keys in the specified key path.
     *
     * @param data    the JSON node to check
     * @param keyPath the key path to search for
     * @return true if the JSON node has all the keys in the key path, false otherwise
     */
    public static boolean has(JsonNode data, String keyPath) {
        String[] keys = keyPath.split("\\.");

        JsonNode currentData = data;
        for (String key : keys) {
            if (currentData.isObject() && currentData.has(key)) {
                currentData = currentData.get(key);
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieves the JSON node at the specified key path from the given JSON node.
     *
     * @param data    the JSON node to retrieve from
     * @param keyPath the key path to search for
     * @return the JSON node at the specified key path, or null if the key path is not found
     */
    public static JsonNode get(JsonNode data, String keyPath) {
        String[] keys = keyPath.split("\\.");

        JsonNode currentNode = data;
        for (String key : keys) {
            if (currentNode.isObject() && currentNode.has(key)) {
                currentNode = currentNode.get(key);
            }
        }
        return currentNode;
    }



    /**
     * Checks if the JSON object contains all the required parameters with non-null values.
     *
     * @param jsonObject         The JSON object to be checked.
     * @param requiredParameters The array of required parameters.
     * @return True if all required parameters have non-null values, otherwise false.
     */
    public static void validateJsonInput(JsonNode jsonObject , String [] requiredParameters) {
        Arrays.stream(requiredParameters).reduce(true, (acc, parameter) -> acc && containsParameterWithNonNullValue(jsonObject, parameter), (acc1, acc2) -> acc1 && acc2);
    }

    /**
     * Checks if a specific parameter in the JSON object has a non-null value.
     *
     * @param jsonObject The JSON object.
     * @param parameter  The parameter.
     * @return True if the parameter exists and has a non-null value, otherwise false.
     */
    private static boolean containsParameterWithNonNullValue(JsonNode jsonObject, String parameter) {
        boolean  containsParameterWithNonNullValue = has(jsonObject, parameter) && !get(jsonObject, parameter).isNull();
        if (!containsParameterWithNonNullValue) throw new ArithmeticException("the " + parameter +" either does not exist or has a null value" );
        return containsParameterWithNonNullValue;
    }
}
