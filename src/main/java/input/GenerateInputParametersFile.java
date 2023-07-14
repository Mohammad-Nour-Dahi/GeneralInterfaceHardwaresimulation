package input;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;

/**
 * The GenerateInputParametersFile class is responsible for generating input parameters file
 * using the provided GenerateInputParameters implementation and JSON data.
 */
public class GenerateInputParametersFile {

    /**
     * Generates an input parameters file based on the provided GenerateInputParameters implementation
     * and JSON data.
     *
     * @param generateInputParameters                       The implementation of GenerateInputParameters interface.
     * @param inputJsonData                                 The JSON data used to generate the input parameters.
     * @param generateHardwaresimulationInputParametersPath The path of the file where the input parameters will be written.
     */
    public void generateInputParameters(GenerateInputParameters generateInputParameters,
                                        JsonNode inputJsonData,
                                        String generateHardwaresimulationInputParametersPath) {
        String generateCode;

        try {
            // Load the JSON file

            generateCode = generateInputParameters.generateInputCode(inputJsonData);

            // Write the generated code to a file
            File pythonFile = new File(generateHardwaresimulationInputParametersPath);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(pythonFile))) {
                writer.write(generateCode);
                System.out.println("Input file was generated successfully.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}