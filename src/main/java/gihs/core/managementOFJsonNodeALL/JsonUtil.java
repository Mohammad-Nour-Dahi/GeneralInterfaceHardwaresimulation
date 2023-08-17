package gihs.core.managementOFJsonNodeALL;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A utility class for managing JSON nodes.
 */
public class JsonUtil {

    /**
     * Checks if the given JSON node has all the keys in the specified key path.
     *
     * @param data     the JSON node to check
     * @param keyPath  the key path to search for
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
     * @param data     the JSON node to retrieve from
     * @param keyPath  the key path to search for
     * @return the JSON node at the specified key path, or null if the key path is not found
     */
    public static JsonNode get(JsonNode data, String keyPath) {
        String[] keys = keyPath.split("\\.");

        JsonNode currentNode = data;
        for (String key : keys) {
            if (currentNode.isObject() && currentNode.has(key)) {
                currentNode = currentNode.get(key);
            } else {
                return null; // Key path not found
            }
        }
        return currentNode;
    }
}
