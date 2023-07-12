package input;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface GenerateInputParameters {

    String generateInputCode(JsonNode jsonData);

}
