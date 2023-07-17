package input;

import com.fasterxml.jackson.databind.JsonNode;
import managementOFJsonNodeALL.JsonNodeALL;

/**
 * The ZsimInput class implements the GenerateInputParameters interface to generate input code
 * for the Zsim simulator based on the provided JSON data.
 */
public class ZsimInput extends GenerateInputParametersImplementation {

    /**

     Constructs a new instance of the ZsimInput class.

     @param parametersFormInputJSON The input JSON node.
     */
    public ZsimInput(JsonNode parametersFormInputJSON) {
        super(parametersFormInputJSON);

        // Calculate the cache sizes using the calculateCacheSize method
        l1dSize = calculateCacheSize(l1dSize);
        l1iSize = calculateCacheSize(l1iSize);
        l2Size = calculateCacheSize(l2Size);

        // Convert the frequency from GHz to megahertz using the convertToMegahertz method
        frequency = convertToMegahertz(frequency);
    }


    /**
     * Generates input code for the Zsim simulator based on the provided JSON data.
     *
     * @return The generated input code as a String.
     */
    @Override
    public String generateInputCode() {


        StringBuilder cfgCodeBuilder = new StringBuilder();


        cfgCodeBuilder.append("// Example zsim config file\n");
        cfgCodeBuilder.append("// Models a hypothetical Skylake-like 32-core CMP, 64MB shared LLC, mesh network, 6-channel DDR4\n\n");

        cfgCodeBuilder.append("sim = {\n");
        cfgCodeBuilder.append("    domains = 1;\n");
        cfgCodeBuilder.append("    phaseLength = 1000;\n");
        cfgCodeBuilder.append("    statsPhaseInterval = 10000;\n");
        cfgCodeBuilder.append("    //pinOptions = \"-inline 0\";\n");
        cfgCodeBuilder.append("    //gmMBytes = 8192;\n");
        cfgCodeBuilder.append("};\n\n");

        cfgCodeBuilder.append("sys = {\n");
        cfgCodeBuilder.append("    caches = {\n");
        cfgCodeBuilder.append("        l1d = {\n");
        cfgCodeBuilder.append("            array = {\n");
        cfgCodeBuilder.append("                type = \"SetAssoc\";\n");
        cfgCodeBuilder.append("                ways = " + l1dAssoc + ";\n");
        cfgCodeBuilder.append("            };\n");
        cfgCodeBuilder.append("            caches = 1;\n");
        cfgCodeBuilder.append("            latency = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "zsim.caches.l1d.latency")).append(";\n");
        cfgCodeBuilder.append("            size = " + l1dSize + ";\n");
        cfgCodeBuilder.append("        };\n");
        cfgCodeBuilder.append("        l1i = {\n");
        cfgCodeBuilder.append("            array = {\n");
        cfgCodeBuilder.append("                type = \"SetAssoc\";\n");
        cfgCodeBuilder.append("                ways = " + l1iAssoc + ";\n");
        cfgCodeBuilder.append("            };\n");
        cfgCodeBuilder.append("            caches = 1;\n");
        cfgCodeBuilder.append("            latency = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "zsim.caches.l1i.latency")).append(";\n");
        cfgCodeBuilder.append("            size = " + l1iSize + ";\n");
        cfgCodeBuilder.append("        };\n");
        cfgCodeBuilder.append("        l2 = {\n");
        cfgCodeBuilder.append("            array = {\n");
        cfgCodeBuilder.append("                type = \"SetAssoc\";\n");
        cfgCodeBuilder.append("                ways = " + l2Assoc + ";\n");
        cfgCodeBuilder.append("            };\n");
        cfgCodeBuilder.append("    	    type = \"Timing\";\n");
        cfgCodeBuilder.append("    	    mshrs = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "zsim.caches.l2.mshrs")).append(";\n");
        cfgCodeBuilder.append("            caches = 1;\n");
        cfgCodeBuilder.append("            latency = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "zsim.caches.l2.latency")).append(";\n");
        cfgCodeBuilder.append("            children = \"l1i|l1d\";\n");
        cfgCodeBuilder.append("            size = " + l2Size + ";\n");
        cfgCodeBuilder.append("        };\n");
        cfgCodeBuilder.append("        l3 = {\n");
        cfgCodeBuilder.append("            array = {\n");
        cfgCodeBuilder.append("                hash = \"H3\";\n");
        cfgCodeBuilder.append("                type = \"SetAssoc\";\n");
        cfgCodeBuilder.append("                ways = 16;\n");
        cfgCodeBuilder.append("            };\n");
        cfgCodeBuilder.append("    	    type = \"Timing\";\n");
        cfgCodeBuilder.append("    	    mshrs = 16;\n");
        cfgCodeBuilder.append("            banks = 32;\n");
        cfgCodeBuilder.append("            caches = 1;\n");
        cfgCodeBuilder.append("            latency = 27;\n");
        cfgCodeBuilder.append("            children = \"l2\";\n");
        cfgCodeBuilder.append("    	    size = 67108864;\n");
        cfgCodeBuilder.append("        };\n");
        cfgCodeBuilder.append("    };\n\n");

        cfgCodeBuilder.append("    cores = {\n");
        cfgCodeBuilder.append("        skylake = {\n");
        cfgCodeBuilder.append("            cores = 1;\n");
        cfgCodeBuilder.append("            dcache = \"l1d\";\n");
        cfgCodeBuilder.append("            icache = \"l1i\";\n");
        cfgCodeBuilder.append("            type = \"OOO\";\n");
        cfgCodeBuilder.append("        };\n");
        cfgCodeBuilder.append("    };\n\n");

        cfgCodeBuilder.append("    frequency = " + frequency + ";\n");
        cfgCodeBuilder.append("    lineSize = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "zsim.lineSize")).append(";\n");
        cfgCodeBuilder.append("    networkType = \"mesh\";\n");
        cfgCodeBuilder.append("    networkFile = \"network_32.mesh\";\n\n");

        cfgCodeBuilder.append("mem = {\n");
        cfgCodeBuilder.append("    addrMapping = \"rank:col:bank\";\n");
        cfgCodeBuilder.append("    splitAddrs = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "zsim.mem.splitAddrs")).append(";\n");
        cfgCodeBuilder.append("    closedPage = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "zsim.mem.closedPage")).append(";\n");
        cfgCodeBuilder.append("    controllerLatency = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "zsim.mem.controllerLatency")).append(";\n");
        cfgCodeBuilder.append("    controllers = ").append(JsonNodeALL.getALL(parametersFormInputJSON, "zsim.mem.controllers")).append(";\n");
        cfgCodeBuilder.append("    tech = \"DDR4-2400-CL17\";\n");
        cfgCodeBuilder.append("    type = \"DDR\";\n");
        cfgCodeBuilder.append("};\n\n");

        cfgCodeBuilder.append("};\n\n");

        cfgCodeBuilder.append("process0 = {\n");
        cfgCodeBuilder.append("    command = \"ls\";\n");
        cfgCodeBuilder.append("    //startFastForwarded = True;\n");
        cfgCodeBuilder.append("};\n");

        return cfgCodeBuilder.toString();
    }


}
