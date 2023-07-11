# GeneralInterfaceHardwaresimulation
 - A general interface is being developed to facilitate the exchange of hardware simulations within a specific class.
 - A hardware simulation is a simulation in which hardware components are virtually replicated to simulate their behavior, functionality, and performance. In this process, a software-based representation of the hardware is created, allowing the analysis, testing, or modeling of the behavior and interactions of real hardware.








## Figure: General Interface to Class of Hardware Simulations
![Figure: General Interface to Class of Hardware Simulations](/images/GeneralInterfaceToClassOfHardwaresimulations.png)

The figure presents a high-level representation of a generic interface designed specifically for a class of hardware simulations. In the center of the figure is the co-simulation, which serves as the main component. This co-sim is surrounded by various hardware simulations including [ZSim](https://github.com/dzhang50/zsim-plusplus), [gem5](https://github.com/gem5/gem5) and [Sniper](https://github.com/snipersim/snipersim).





# Installation



- [Docker](https://www.docker.com/get-started/) installieren



```
git clone https://github.com/Mohammad-Nour-Dahi/GeneralInterfaceHardwaresimulation.git 
```

## Installing Java and Maven

To run this project, you need to have Java and Maven installed on your system. Follow the instructions below to install both components:

### Installing Java

1. Go to the official Java website: [https://www.oracle.com/java/](https://www.oracle.com/java/).
2. Download the latest version of Java for your operating system.
3. Run the installation wizard and follow the instructions to install Java on your system.
4. After installation, verify the Java version to ensure the installation was successful. Open a terminal or command prompt and enter the command `java -version`. You should see the installed Java version displayed.

### Installing Maven

1. Go to the official Maven website: [https://maven.apache.org/](https://maven.apache.org/).
2. Download the latest version of Maven.
3. Extract the downloaded archive to a location of your choice.
4. Add the `bin` directory of the extracted Maven package to the `PATH` environment variable.
5. Verify the Maven installation by running the command `mvn -version` in a terminal or command prompt. You should see the installed Maven version displayed.


# Execution
- ## Creating an Input File

To execute the program, you need to create an input file in JSON format that contains the input parameters for the execution.

Follow the steps below to create the input file:

1. Open a text editor or any JSON editor of your choice.
2. Create a new file and save it with a `.json` extension (e.g., `input.json`).
3. Copy the JSON code below and paste it into the file

```json
{
  "commonParameters": {
    "hardwaresimulation": {
      "name": "gem5",
      "statsOutputPath": "D:/outputStats"
    },
    "cache_hierarchy": {
      "l1d_size": "32KiB",
      "l1d_assoc": 8,
      "l1i_size": "32KiB",
      "l1i_assoc": 8,
      "l2_size": "256kB",
      "l2_assoc": 16
    },
    "board": {
      "clk_freq": "3GHz"
    }
  },
  "gem5": {
    "cache_hierarchy": {
      "num_l2_banks": 1
    },
    "memory": {
      "size": "2GiB"
    },
    "processor": {
      "starting_core_type": "TIMING",
      "switch_core_type": "O3",
      "num_cores": 2,
      "isa": "X86"
    },
    "binary": {
      "name": "x86-hello64-static"
    }
  },
  "sniper": {
    "perf_model/cache": {
      "levels": 2
    },
    "perf_model/core": {
      "logical_cpus": 1,
      "type": "interval",
      "core_model": "nehalem"
    },
    "perf_model/core/interval_timer": {
      "dispatch_width": 2,
      "window_size": 32,
      "num_outstanding_loadstores": 10
    },
    "perf_model/sync": {
      "reschedule_cost": 1000
    },
    "perf_model/branch_predictor": {
      "type": "pentium_m",
      "mispredict_penalty": 8
    },
    "perf_model/tlb": {
      "penalty": 30
    },
    "perf_model/itlb": {
      "size": 48,
      "associativity": 48
    },
    "perf_model/dtlb": {
      "size": 48,
      "associativity": 48
    },
    "perf_model/stlb": {
      "size": 128,
      "associativity": 4
    },
    "perf_model/l1_icache": {
      "perfect": false,
      "address_hash": "mask",
      "replacement_policy": "lru",
      "data_access_time": 4,
      "tags_access_time": 1,
      "perf_model_type": "parallel",
      "writethrough": 0,
      "shared_cores": 1
    },
    "perf_model/l1_dcache": {
      "perfect": false,
      "address_hash": "mask",
      "replacement_policy": "lru",
      "data_access_time": 4,
      "tags_access_time": 1,
      "perf_model_type": "parallel",
      "writethrough": 0,
      "shared_cores": 1
    },
    "perf_model/l2_cache": {
      "perfect": false,
      "address_hash": "mask",
      "replacement_policy": "lru",
      "data_access_time": 12,
      "tags_access_time": 3,
      "writeback_time": 50,
      "perf_model_type": "parallel",
      "writethrough": 0,
      "shared_cores": 2,
      "dvfs_domain": "global",
      "prefetcher": "none"
    },
    "perf_model/dram_directory": {
      "total_entries": 1048576,
      "associativity": 16,
      "directory_type": "full_map"
    },
    "perf_model/dram": {
      "num_controllers": -1,
      "controllers_interleaving": 4,
      "latency": 45,
      "per_controller_bandwidth": 12.8,
      "chips_per_dimm": 8,
      "dimms_per_controller": 4
    },
    "network": {
      "memory_model_1": "bus",
      "memory_model_2": "bus"
    },
    "network/bus": {
      "bandwidth": 128,
      "ignore_local_traffic": false
    },
    "clock_skew_minimization": {
      "scheme": "barrier"
    },
    "clock_skew_minimization/barrier": {
      "quantum": 100
    },
    "dvfs": {
      "transition_latency": 2000
    },
    "dvfs/simple": {
      "cores_per_socket": 1
    },
    "power": {
      "vdd": 1.0,
      "technology_node": 22
    }
  },
  "zsim": {
    "lineSize": 64,
    "caches": {
      " l1d": {
        "latency": 4
      },
      " l1i": {
        "latency": 3
      },
      " l2": {
        "type": "Timing",
        "mshrs": 10,
        "latency": 4,
        "children": "l1i|l1d"
      },
      " l3": {
        "latency": 27,
        "banks": 32,
        "children": "l2",
        "mshrs": 16
      }
    },
    "cores": {
      "skylake": {
        "cores": 1,
        "dcache": "l1d",
        "icache": "l1i",
        "type": "OOO"
      }

    },
    "mem": {
      "splitAddrs" : false,
      "closedPage" :true,
      "controllerLatency" : 40,
      "controllers" : 6

    }
  }
}

```



#### To run the program, execute the following command in the terminal or command prompt:
```bash
mvn exec:java -Dexec.mainClass=org.example.GeneralInterfaceHardwaresimulation -Dexec.args="-jsonFile "Path to the input.json"" 
```

Make sure you have Maven installed on your system before running the command. This command will execute the Java main class `GeneralInterfaceHardwaresimulation.GeneralInterfaceHardwaresimulation` and provide the JSON input file path as the argument.

Replace `"Path to the input.json"` with the actual file path of your JSON input file.






- ## Output

After the execution, an `output.json` file will be generated. This file contains the simulation results.

The path to the output file is specified in the input file under the `statsOutputPath` parameter:

```json
{
  "commonParameters": {
    "hardwaresimulation": {
      "name": "gem5",
      "statsOutputPath": "D:/outputStats"
    },
    // ...
  },
  // ...
}
