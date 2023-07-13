# generalInterfaceHardwaresimulation
 - A general interface is being developed to facilitate the exchange of hardware simulations within a specific class.
 - A hardware simulation is a simulation in which hardware components are virtually replicated to simulate their behavior, functionality, and performance. In this process, a software-based representation of the hardware is created, allowing the analysis, testing, or modeling of the behavior and interactions of real hardware.








## Figure: General Interface to Class of Hardware Simulations
![Figure: General Interface to Class of Hardware Simulations](/images/GeneralInterfaceToClassOfHardwaresimulations.png)


The figure presents a high-level representation of a generic interface designed specifically for a class of hardware simulations. In the center of the figure, various hardware simulations such as [ZSim](https://github.com/dzhang50/zsim-plusplus), [gem5](https://github.com/gem5/gem5) and [Sniper](https://github.com/snipersim/snipersim). are depicted, which are supported by the interface.







# Installation



- [Docker](https://www.docker.com/get-started/) installieren



```
git clone https://github.com/Mohammad-Nour-Dahi/generalInterfaceHardwaresimulation.git 
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

## Creating an Input File
 
- To execute the program, you need to create an input file in JSON format that contains the input parameters for the execution. An example file can be found at src/test/java as [HardwaresimulationParameter.json](src/test/java/HardwaresimulationParameter.json). You can use this file to run the generic interface.


### Running the Program

To run the program, please follow these steps:

1. Open a terminal or command prompt.
2. Navigate to the `GeneralInterfaceHardwaresimulation` directory.
3. Run the following command to install all the required dependencies and build the project:
```bash
mvn install
```
Once the project is successfully built, you can execute the program using the following command:

```bash
mvn exec:java -Dexec.mainClass=org.generalInterfaceHardwaresimulation.GeneralInterfaceHardwaresimulation -Dexec.args="-jsonFile \"<Path to the input.json>\""
 
```

Make sure you have Maven installed on your system before running the command. This command will execute the Java main class `org.generalInterfaceHardwaresimulation.GeneralInterfaceHardwaresimulation` and provide the JSON input file path as the argument.

Replace `"<Path to the input.json>"` with the actual file path of your JSON input file.

If you want to use the [HardwaresimulationParameter.json](src/test/java/HardwaresimulationParameter.json) file located at src/test/java, you can execute the command as follows:

```bash 
mvn exec:java -Dexec.mainClass=org.generalInterfaceHardwaresimulation.GeneralInterfaceHardwaresimulation -Dexec.args="-jsonFile \".\src\test\java\HardwaresimulationParameter.json\""

```


 ## Output

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
