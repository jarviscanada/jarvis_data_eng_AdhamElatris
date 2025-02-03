package ca.jrvs.apps.grep;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class JavaGrepLambdaImp extends JavaGrepImp {

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRootPath(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);

    try {
      javaGrepLambdaImp.process();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  @Override
  public void process() throws IOException {
    List<String> matchedLines = listFiles(getRootPath()).stream()
        .flatMap(file -> readLines(file).stream())
        .filter(this::containsPattern)
        .collect(Collectors.toList());

    writeToFile(matchedLines);
  }

  @Override
  public List<File> listFiles(String rootDir) {
    File root = new File(rootDir);
    List<File> files;

    if (!root.exists() || !root.isDirectory()) {
      logger.error("Root directory is invalid: " + rootDir);
      return Collections.emptyList();
    }

    File[] directoryFiles = root.listFiles();
    if (directoryFiles == null) {
      return Collections.emptyList();
    }

    files = Arrays.stream(directoryFiles)
        .flatMap(file -> file.isDirectory() ? listFiles(file.getAbsolutePath()).stream()
            : Stream.of(file))
        .collect(Collectors.toList());

    return files;
  }


  @Override
  public List<String> readLines(File inputFile) {
    if (!inputFile.isFile()) {
      throw new IllegalArgumentException("Invalid file: " + inputFile.getAbsolutePath());
    }
    try (
        Stream<String> stream = Files.lines(inputFile.toPath())
    ) {
      return stream.collect(Collectors.toList()); //
    } catch (IOException e) {
      logger.error("Failed to read file: " + inputFile.getAbsolutePath(), e);
      return Collections.emptyList();
    }
  }

  // I don't need this method because it's implementation is exactly the same as the superclass (JavaGrepImp)
//  @Override
//  public boolean containsPattern(String line) {
//    return Pattern.compile(getRegex()).matcher(line).find();
//  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    try (
        FileOutputStream fos = new FileOutputStream(getOutFile());
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter b_writer = new BufferedWriter(osw)
    ) {
      lines.stream()
          .peek(line -> {  //I could have also used map()
            try {
              b_writer.write(line);
              b_writer.newLine();
            } catch (IOException e) {
              throw new UncheckedIOException(e);
            }
          })
          .count();

    } catch (IOException e) {
      logger.error("Failed to write to file: {}", getOutFile(), e);
      throw e;
    }
  }


}
