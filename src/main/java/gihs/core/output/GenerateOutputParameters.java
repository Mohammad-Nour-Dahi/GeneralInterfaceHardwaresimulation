package gihs.core.output;

/**
 * The GenerateOutputParameters interface defines the contract for generating
 * JSON parameters based on a specified file path.
 */
public interface GenerateOutputParameters {

    /**
     * Generate a JSON parameter using the specified file path.
     * The generated JSON parameter should contain the mapping of simulation results
     * with the output parameters for the general interface.
     *
     * @param filePath The path to the file containing statistics data.
     * @return A formatted JSON string representing the statistics parameters.
     */
    String generateStatisticsParametersJson(String filePath);
}
