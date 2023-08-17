package gihs.core.managementOfDockerfiles;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CopyArchiveFromContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the HardwaresimulationDocker class.
 *
 */
public class HardwaresimulationDocker {
    private DockerClient dockerClient;
    private String containerId;

    /**
     * Constructor for the HardwaresimulationDocker class.
     */
    public HardwaresimulationDocker() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
        dockerClient = DockerClientImpl.getInstance(config, httpClient);
    }

    /**
     * Builds the hardwaresimulation image with the given name.
     *
     * @param imageName the name of the hardwaresimulation image
     */
    private void hardwaresimulationbuildImage(String imageName) {
        BuildImageCmd buildImageCmd = dockerClient.buildImageCmd(new File("../resources/dockerfiles/" + imageName + "/Dockerfile"))
                .withTags(Collections.singleton(imageName));

        BuildImageResultCallback callback = new BuildImageResultCallback() {
            @Override
            public void onNext(BuildResponseItem item) {
                super.onNext(item);
                if (item != null && item.getStream() != null) {
                    System.out.println(item.getStream()); // Display build process output
                }
            }
        };

        // Start the build
        buildImageCmd.exec(callback).awaitImageId();

        // Check if the image was created successfully
        InspectImageResponse imageResponse = dockerClient.inspectImageCmd(imageName).exec();
        if (imageResponse.getId() != null) {
            System.out.println("Image created: " + imageName);
        } else {
            System.err.println("Error creating image: " + imageName);
        }
    }

    /**
     * Creates the hardwaresimulation container with the given image name.
     *
     * @param imageName the name of the hardwaresimulation image
     */
    public void createHardwaresimulationContainer(String imageName) {
        if (!isImageExists(imageName)) {
            hardwaresimulationbuildImage(imageName);
        }
        CreateContainerResponse containerResponse = dockerClient.createContainerCmd(imageName)
                .withTty(true) // Enable TTY mode
                .withHostConfig(HostConfig.newHostConfig().withPrivileged(true))
                .exec();
        containerId = containerResponse.getId();

    }

    /**
     * Checks if the image with the given name exists.
     *
     * @param imageName the name of the image
     * @return true if the image exists, false otherwise
     */
    private boolean isImageExists(String imageName) {
        try {
            List<Image> images = dockerClient.listImagesCmd().exec();
            for (Image image : images) {
                if (Arrays.asList(image.getRepoTags()).contains(imageName + ":latest")) {
                    return true; // Image found
                }
            }
        } catch (InternalServerErrorException e) {
            // Handle internal server error
            System.err.println("An error message would be generated while inspecting the Docker image : " + e.getMessage());
            e.printStackTrace();
        }
        return false; // Image not found
    }

    /**
     * Starts the hardwaresimulation container with the given container ID.
     *
     * @param containerId the ID of the container
     */
    public void startHardwaresimulationContainer(String containerId) {
        dockerClient.startContainerCmd(containerId).exec();
    }

    /**
     * Executes a command in the container with the given commands.
     *
     * @param commands the commands to execute
     * @return the execution ID of the command
     */
    public String command(String[] commands) {
        return dockerClient.execCreateCmd(containerId)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withCmd(commands)
                .exec()
                .getId();
    }


    /**
     * Retrieves the output of the command from the hardware simulation console with the specified execution ID.
     *
     * @param execId The execution ID of the command.
     * @return The output of the command as a string.
     */
    public String outputFromHardwaresimulationConsole(String execId) {
        try {
            StringBuilder outputBuffer = new StringBuilder();

            // Start the command execution and capture the output
            dockerClient.execStartCmd(execId).exec(new ExecStartResultCallback() {
                @Override
                public void onNext(Frame item) {
                    if (item != null && item.getPayload() != null) {
                        outputBuffer.append(new String(item.getPayload()));
                    }
                    super.onNext(item);
                }
            }).awaitCompletion();

            // Get the complete output as a string
            String output = outputBuffer.toString();

            // Print the command output
            System.out.println("Command output: " + output);
            return output;
        } catch (InterruptedException e) {
            // Handle the interruption
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Copies a file from the container to the host.
     *
     * @param containerId       the ID of the container
     * @param containerFilePath the path of the file in the container
     * @param hostFilePath      the path of the file on the host
     */
    public void outputFileFromContainer(String containerId, String containerFilePath, String hostFilePath) {
        try {
            CopyArchiveFromContainerCmd copyFromCmd = dockerClient.copyArchiveFromContainerCmd(containerId, containerFilePath);

            try (InputStream inputStream = copyFromCmd.exec()) {
                try (TarArchiveInputStream tarInput = new TarArchiveInputStream(inputStream)) {
                    // Skip the header to reach the actual file data
                    tarInput.getNextTarEntry();

                    try (OutputStream outputStream = new FileOutputStream(hostFilePath)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = tarInput.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }

                        System.out.println("File copied successfully from container to host.");
                    } catch (IOException e) {
                        System.err.println("Error while copying the file to host: " + e.getMessage());
                    }
                } catch (IOException e) {
                    System.err.println("Error reading the tar archive: " + e.getMessage());
                }
            } catch (IOException e) {
                System.err.println("Error executing the copy command: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
    /**
     * Copies a file from the host to the container.
     *
     * @param containerId       the ID of the container
     * @param hostFilePath      the path of the file on the host
     * @param containerFilePath the path of the file in the container
     */
    public void inputFileTOContainer(String containerId, String hostFilePath, String containerFilePath) {
        dockerClient.copyArchiveToContainerCmd(containerId)
                .withHostResource(hostFilePath)
                .withRemotePath(containerFilePath)
                .exec();

        System.out.println("File successfully copied from host system to container.");
    }

    /**
     * Gets the container ID.
     *
     * @return the container ID
     */
    public String getContainerId() {
        return containerId;
    }

    /**
     * Stops the Docker hardwaresimulation container
     *
     *
     */
    public void closeDockerHardwaresimulation() {
        StopContainerCmd stopCmd = dockerClient.stopContainerCmd(containerId);
        stopCmd.withTimeout(0).exec();
    }

    /**
     * Deletes the Docker hardwaresimulation container
     *
     *
     */
    public void deleteDockerHardwaresimulation() {
        RemoveContainerCmd removeCmd = dockerClient.removeContainerCmd(containerId);
        removeCmd.withForce(true).exec();
    }
}
