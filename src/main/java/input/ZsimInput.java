package input;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ZsimInput implements GenerateInputParameters {
    //TODO json to this methode
    @Override
    public String generateInputCode(JsonNode jsonData) {
        JsonNode cacheHierarchy =  jsonData.get("commonParameters").get("cache_hierarchy");
        String l1dSize = cacheHierarchy.get("l1d_size").asText().replaceAll("KiB","");
        int l1dAssoc = cacheHierarchy.get("l1d_assoc").asInt();
        String l1iSize = cacheHierarchy.get("l1i_size").asText().replaceAll("KiB","");
        int l1iAssoc = cacheHierarchy.get("l1i_assoc").asInt();
        String l2Size = cacheHierarchy.get("l2_size").asText().replaceAll("kB","");
        int l2Assoc = cacheHierarchy.get("l2_assoc").asInt();


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
        cfgCodeBuilder.append("                ways = "+l1dAssoc+";\n");
        cfgCodeBuilder.append("            };\n");
        cfgCodeBuilder.append("            caches = 1;\n");
        cfgCodeBuilder.append("            latency = 4;\n");
        cfgCodeBuilder.append("            size = "+l1dSize+"768;\n");
        cfgCodeBuilder.append("        };\n");
        cfgCodeBuilder.append("        l1i = {\n");
        cfgCodeBuilder.append("            array = {\n");
        cfgCodeBuilder.append("                type = \"SetAssoc\";\n");
        cfgCodeBuilder.append("                ways = "+l1iAssoc+";\n");
        cfgCodeBuilder.append("            };\n");
        cfgCodeBuilder.append("            caches = 1;\n");
        cfgCodeBuilder.append("            latency = 3;\n");
        cfgCodeBuilder.append("            size = "+l1iSize+"768;\n");
        cfgCodeBuilder.append("        };\n");
        cfgCodeBuilder.append("        l2 = {\n");
        cfgCodeBuilder.append("            array = {\n");
        cfgCodeBuilder.append("                type = \"SetAssoc\";\n");
        cfgCodeBuilder.append("                ways = "+l2Assoc+";\n");
        cfgCodeBuilder.append("            };\n");
        cfgCodeBuilder.append("    	    type = \"Timing\";\n");
        cfgCodeBuilder.append("    	    mshrs = 10;\n");
        cfgCodeBuilder.append("            caches = 1;\n");
        cfgCodeBuilder.append("            latency = 7;\n");
        cfgCodeBuilder.append("            children = \"l1i|l1d\";\n");
        cfgCodeBuilder.append("            size = 262144;\n");
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

        cfgCodeBuilder.append("    frequency = 2800;\n");
        cfgCodeBuilder.append("    lineSize = 64;\n");
        cfgCodeBuilder.append("    networkType = \"mesh\";\n");
        cfgCodeBuilder.append("    networkFile = \"network_32.mesh\";\n");


        cfgCodeBuilder.append("mem = {\n");
        cfgCodeBuilder.append("    addrMapping = \"rank:col:bank\";\n");
        cfgCodeBuilder.append("	splitAddrs = False;\n");
        cfgCodeBuilder.append("    closedPage = True;\n");
        cfgCodeBuilder.append("    controllerLatency = 40;\n");
        cfgCodeBuilder.append("    controllers = 6;\n");
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
