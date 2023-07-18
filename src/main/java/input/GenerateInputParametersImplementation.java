package input;

import com.fasterxml.jackson.databind.JsonNode;
import managementOFJsonNodeALL.JsonNodeALL;

/**

 This abstract class implements the GenerateInputParameters interface and provides common functionality

 for generating input parameters.
 */
public abstract class GenerateInputParametersImplementation implements GenerateInputParameters {

    /**

     The parametersFormInputJSON field stores the JSON node containing the input parameters.
     It represents the JSON object that contains the various input values for the simulation.
     */
    protected JsonNode parametersFormInputJSON;

    /**

     The l1dSize field stores the size of the L1 data cache as a string.
     It represents the size of the cache in kilobytes (KB).
     */
    protected String l1dSize;

    /**

     The l1dAssoc field stores the associativity of the L1 data cache as an integer.
     It represents the number of cache lines per set in the cache.
     */
    protected int l1dAssoc;

    /**

     The l1iSize field stores the size of the L1 instruction cache as a string.
     It represents the size of the cache in kilobytes (KB).
     */
    protected String l1iSize;

    /**

     The l1iAssoc field stores the associativity of the L1 instruction cache as an integer.
     It represents the number of cache lines per set in the cache.
     */
    protected int l1iAssoc;

    /**

     The l2Size field stores the size of the L2 cache as a string.
     It represents the size of the cache in kilobytes (KB).
     */
    protected String l2Size;

    /**

     The l2Assoc field stores the associativity of the L2 cache as an integer.
     It represents the number of cache lines per set in the cache.
     */
    protected int l2Assoc;

    /**

     The frequency field stores the frequency of the processor as a string.
     It represents the clock frequency in gigahertz (GHz).
     */
    protected String frequency;

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
