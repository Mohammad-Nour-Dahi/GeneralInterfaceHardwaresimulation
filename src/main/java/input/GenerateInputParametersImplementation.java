package input;

import com.fasterxml.jackson.databind.JsonNode;
import managementOFJsonNodeALL.JsonNodeALL;

/**

 This abstract class implements the GenerateInputParameters interface and provides common functionality

 for generating input parameters.
 */
public abstract class GenerateInputParametersImplementation implements GenerateInputParameters {

    protected JsonNode parametersFormInputJSON;
    protected String l1dSize;
    protected int l1dAssoc;
    protected String l1iSize;
    protected int l1iAssoc;
    protected String l2Size;
    protected int l2Assoc;

    protected  String frequency;

    /**
     * Constructs a GenerateInputParametersImplementation object with the specified JSON input.
     *
     * @param parametersFormInputJSON The JSON input containing the parameters for generating input.
     */
    GenerateInputParametersImplementation(JsonNode parametersFormInputJSON) {
        this.parametersFormInputJSON = parametersFormInputJSON;
        JsonNode cacheHierarchy = parametersFormInputJSON.get("commonParameters").get("cache_hierarchy");
        this.l1dSize = cacheHierarchy.get("l1d_size").asText();
        this.l1dAssoc = cacheHierarchy.get("l1d_assoc").asInt();
        this.l1iSize = cacheHierarchy.get("l1i_size").asText();
        this.l1iAssoc = cacheHierarchy.get("l1i_assoc").asInt();
        this.l2Size = cacheHierarchy.get("l2_size").asText();
        this.l2Assoc = cacheHierarchy.get("l2_assoc").asInt();
        this.frequency = JsonNodeALL.getALL(parametersFormInputJSON,"commonParameters.board.frequency").asText();
    }


    /**
     * Generates input code based on the provided JSON data.
     *
     * @return The generated input code as a String.
     */
    @Override
    public  abstract String generateInputCode() ;

    /**
     * Calculates the cache size in bytes based on the given cache size in kilobytes.
     *
     * @param cacheSize The cache size in kilobytes.
     * @return The cache size in bytes.
     */
    protected  String calculateCacheSize(String cacheSize) {
        int sizeInKB = Integer.parseInt(cacheSize.replaceAll("[^0-9]", ""));
        int sizeInBytes = sizeInKB * 1024;
        return Integer.toString(sizeInBytes);
    }

    /**
     * Converts the frequency value from gigahertz to megahertz.
     *
     * @param frequency The frequency value in gigahertz.
     * @return The frequency value in megahertz.
     */
    protected String convertToMegahertz(String frequency) {
        int gigahertz = 0;
        try {
            String gigahertzValue = frequency.replaceAll("[^0-9]", "");
            gigahertz = Integer.parseInt(gigahertzValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        int megahertz = gigahertz * 1000;
        return String.valueOf(megahertz);
    }



}

