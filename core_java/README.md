# Core Java Apps
This project category consists of the two Java applications below

1. [Java Grep App](./grep)
2. [Stock Quote App](./stockquote)



# Introduction
The Java Grep application is a command-line tool that recursively searches files for lines matching a given regex pattern and writes the 
results to an output file. This project demonstrates core Java concepts, including file handling, regular expressions, recursion, and 
the use of Java Streams for efficient data processing.

The application also utilizes loggers to track the execution flow, log errors, and provide insights into the application’s behavior during 
runtime. This ensures better debugging and monitoring.

The application was developed using Java 8, with IntelliJ IDEA as the development environment. For containerization, 
Docker was employed to facilitate deployment.

# Quick Start
How to use your apps? 

You have two ways to run the application:

1) Using the JAR file
2) Using Docker

    ### You will have to provide 3 Required Entries:

    - regex: A special text string to describe the search pattern.
    - rootPath: The root directory path where the search will be performed.
    - outFile: The name of the output file where results will be saved.

    ### Option 1: Run the Application Using the JAR File
    - Step 1: Compile and Package the Java Application

        All required dependencies and classes are already included. Run:

        ```mvn clean compile package```

    - Step 2: Run the JAR File
    
        After packaging, you will find the JAR file in the target directory. Run it using the following command:

        ```java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp .*Romeo.*Juliet.* ./data ./out/grep.txt```

        This example searches for lines containing both 'Romeo' and 'Juliet' inside ./data and saves the output to ./out/text.txt.

    ### Option 2: Run the Application Using Docker

    - Step 1: Pull the Docker Image (if needed)

        If you haven't built the image locally, pull it from Docker Hub:

        ```docker pull ${docker_user}/grep:latest```

    - Step 2: Run the Application in a Docker Container

        ```
        docker run --rm \
        -v "$(pwd)/data:/data" -v "$(pwd)/out:/out" \
        ${docker_user}/grep:latest ".*Romeo.*Juliet.*" /data /out/text.txt
        ```

        This runs the app inside a container, searching for 'Romeo' and 'Juliet' inside data/, and saving the results in out/text.txt.

    - Step 3: Verify the Output

        Once the application finishes, check the results:

        ```cat out/text.txt```


# Implemenation

## Pseudocode
 `process` method pseudocode.

    matchedLines = []
    for file in listFilesRecursively(rootDir)
    for line in readLines(file)
        if containsPattern(line)
            matchedLines.add(line)
    writeToFile(matchedLines)

## Performance Issue

The application uses too much memory when handling large files because it stores all matching lines in memory before writing them to a 
file. To fix this, we can use a streaming approach, writing each matched line directly to the output file instead of storing it in a list. 
In other words, we will need to return a stream in our methods rather than lists.

# Test
The application was tested manually using the following approach:

1) Sample Data Creation:
    - Created sample text files containing various patterns to simulate data.

2) Regex Query Testing:
    - Ran the application with different regex queries to ensure it correctly identifies matching patterns.

3) Output Verification:
    - Verified that the output file contained the correct matches as per the regex queries.

4) Edge Case Testing:
    - Tested edge cases such as:
        - Empty files.
        - Non-existent directories to ensure proper error handling.

5) Logging for Functionality:
    - Used loggers to track and verify the application's functionality and behavior during execution.

# Deployment

To simplify distribution, the application is containerized using Docker. Here is how I did it:

### Step 1: Create a Dockerfile

Create a Dockerfile that defines how your Java application will run inside a Docker container:

    cat > Dockerfile << EOF
    FROM openjdk:8-alpine
    COPY target/grep*.jar /usr/local/app/grep/lib/grep.jar
    ENTRYPOINT ["java","-jar","/usr/local/app/grep/lib/grep.jar"]
    EOF

- **FROM openjdk:8-alpine**: Uses a lightweight Java 8 image.

- **COPY target/grep\*.jar /usr/local/app/grep/lib/grep.jar**: Copies the compiled JAR file into the container.

- **ENTRYPOINT ["java","-jar","/usr/local/app/grep/lib/grep.jar"]**: Runs the application when the container starts.

- **EOF**: Marks the end of the input block.

### Step 2: Package the Java Application

Before building the Docker image, compile and package the Java application:

    mvn clean package

This generates the JAR file inside the target/ directory.

### Step 3: Build the Docker image:

    docker build -t ${docker_user}/grep .


### Step 4: Run the container:

    docker run --rm \
    -v `pwd`/data:/data -v `pwd`/log:/log \
    ${docker_user}/grep .*Romeo.*Juliet.* /data /log/grep.out

- **--rm**: Removes the container after execution.
- **-v \`pwd`/data:/data**: Mounts the local data/ directory into the container.
- **-v \`pwd`/log:/log"**: Mounts the local log/ directory into the container.
- **${docker_user}/grep**: Runs the built image.
- **".\*Romeo.\*Juliet.*"**: The regex pattern to search for.
- **/data**: The directory to search in.
- **/log/grep.out**: The output file.

### Step 5: Push the image to Docker Hub:

    docker push ${docker_user}/grep

# Improvement
- **Optimize Memory Usage**: Implement a streaming-based solution to process large files efficiently, minimizing memory consumption.

- **Enhance Error Handling**: Improve exception handling to ensure better fault tolerance and provide more informative error messages 
for users.

- **Add Unit Tests**: Automate testing using JUnit to ensure the reliability of the application and catch regressions early.

- **Create a Visual Frontend**: Develop a simple user interface to simplify the usage of the project, providing a more user-friendly 
experience.