package parserHardwaresimulation;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The ParserInterface defines the contract for hardware simulation parsers.
 * It handles the input, output, and execution of the hardware simulation.
 */
public interface ParserInterface {

   /**
    * Parses the input and performs the hardware simulation.
    *
    * @param input the input value for the hardware simulation
    */
   public void parse(JsonNode input);
}
