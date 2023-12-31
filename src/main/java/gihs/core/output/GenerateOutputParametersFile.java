package gihs.core.output;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class generates an output file containing sorted JSON output parameters.
 */
public class GenerateOutputParametersFile {

    /**
     * Generates the output parameters file.
     *
     * @param generateOutputParameters               The instance of GenerateOutputParameters used for generating statistics parameters.
     * @param generateHardwareSimulationOutputParametersPath The path to the hardware simulation output parameters file.
     * @param generateJsonPath                       The path to save the generated JSON output parameters file.
     */
    public void generateOutputParameters(GenerateOutputParameters generateOutputParameters, String generateHardwareSimulationOutputParametersPath, String generateJsonPath) {
        String jsonOutputParameter = generateOutputParameters.generateStatisticsParametersJson(generateHardwareSimulationOutputParametersPath);
        String sortedJsonOutputParameter = sortJsonOutputParameter(jsonOutputParameter);
        System.out.println("---------- outputStats ----------\n" + sortedJsonOutputParameter +"\n---------- END ----------");
        saveJsonToFile(sortedJsonOutputParameter, generateJsonPath);
    }

    /**
     * Sorts the JSON output parameters alphabetically by keys.
     *
     * @param jsonOutputParameter The JSON output parameter to be sorted.
     * @return The sorted JSON output parameter.
     */
    private String sortJsonOutputParameter(String jsonOutputParameter) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode jsonObject = objectMapper.readValue(jsonOutputParameter, ObjectNode.class);
            Map<String, JsonNode> sortedFields = new TreeMap<>(Comparator.naturalOrder());
            jsonObject.fields().forEachRemaining(entry -> sortedFields.put(entry.getKey(), entry.getValue()));
            ObjectWriter objectWriter = objectMapper.writer().with(SerializationFeature.INDENT_OUTPUT);
            return objectWriter.writeValueAsString(sortedFields);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Saves the JSON output parameter to a file.
     *
     * @param json     The JSON output parameter.
     * @param filePath The path to save the file.
     */
    private void saveJsonToFile(String json, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(json);
            fileWriter.close();
            System.out.println("The output file was successfully created at path: " + filePath);
        } catch (IOException e) {
            System.out.println(e.getMessage() + ": " + filePath);
        }
    }

    /**
     * Generates output parameters using the given GenerateOutputParameters object and file paths.
     *
     * @param generateOutputParameters      The GenerateOutputParameters object used to generate statistics parameters.
     * @param generateHardwareSimulationOutputParametersPath The file path for generating hardware simulation output parameters.
     * @param generateJsonPath              The file path for generating the JSON file.
     * @param hostNanoseconds               The value of host nanoseconds to be added to the JSON.
     */
    public void generateOutputParameters(GenerateOutputParameters generateOutputParameters, String generateHardwareSimulationOutputParametersPath, String generateJsonPath, String hostNanoseconds) {
        // Generate statistics parameters in JSON format
        String jsonOutputParameter = generateOutputParameters.generateStatisticsParametersJson(generateHardwareSimulationOutputParametersPath);

        // Add the value of host nanoseconds and sort the JSON output parameters
        String sortedJsonOutputParameter = sortJsonOutputParameter(jsonOutputParameter.replaceFirst("}", ", " + hostNanoseconds + "\n}"));

        // Print the sorted JSON output parameters
        System.out.println("---------- outputStats ----------\n" + sortedJsonOutputParameter + "\n---------- END ----------");

        // Save the sorted JSON output parameters to the specified file path
        saveJsonToFile(sortedJsonOutputParameter, generateJsonPath);
    }

}
