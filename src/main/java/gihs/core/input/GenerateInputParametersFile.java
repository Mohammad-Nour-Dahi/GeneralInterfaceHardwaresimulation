package input;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
     * @param generateHardwaresimulationInputParametersPath The path of the file where the input parameters will be written.
     */
    public void generateInputParameters(GenerateInputParameters generateInputParameters,
                                        String generateHardwaresimulationInputParametersPath) {
        String generateCode;

        try {
            // Load the JSON file

            generateCode = generateInputParameters.generateInputCode();

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