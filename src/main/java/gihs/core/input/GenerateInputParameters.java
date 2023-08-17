package gihs.core.input;

/**
 * The GenerateInputParameters interface defines a contract for generating input code based on JSON data.
 */
public interface GenerateInputParameters {

    /**
     * Generates input code based on the provided JSON data.
     *
     * @return The generated input code as a String.
     */
     String generateInputCode();
}
