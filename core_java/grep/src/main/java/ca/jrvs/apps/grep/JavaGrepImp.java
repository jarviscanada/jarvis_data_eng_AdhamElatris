package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;


public class JavaGrepImp implements JavaGrep {

  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

  private String regex;
  private String rootPath;
  private String outFile;

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      javaGrepImp.process();
    } catch (Exception ex) {
      javaGrepImp.logger.error("Error: Unable to process", ex);
    }
  }

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getRootPath() {
    return rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getOutFile() {
    return outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }

  @Override
  public void process() throws IOException {
    List<String> matchedLines = new ArrayList<>();

    List<File> files = listFiles(getRootPath());

    for (File file : files) {
      List<String> lines = readLines(file);

      for (String line : lines) {
        if (containsPattern(line)) {
          matchedLines.add(line);
        }
      }
    }
    writeToFile(matchedLines);
  }

  @Override
  public List<File> listFiles(String rootDir) {
    List<File> files = new ArrayList<>();
    File root = new File(rootDir);

    if (!root.exists() || !root.isDirectory()) {
      logger.error("Root directory is invalid: " + rootDir);
      return Collections.emptyList();
    }

    if (root.isDirectory()) {
      File[] directoryFiles = root.listFiles();

      if (directoryFiles != null) {
        for (File file : directoryFiles) {
          if (file.isDirectory()) {
            files.addAll(listFiles(file.getAbsolutePath()));
          } else {
            files.add(file);
          }
        }
      }

    }
    return files;
  }

  @Override
  public List<String> readLines(File inputFile) {
    if (!inputFile.isFile()) {
      throw new IllegalArgumentException(
          "The given input is not a valid file: " + inputFile.getAbsolutePath());
    }
    List<String> lines = new ArrayList<>();
    try (BufferedReader b_reader = new BufferedReader(new FileReader(inputFile))) {
      String line;
      while ((line = b_reader.readLine()) != null) {
        lines.add(line);
      }
    } catch (IOException e) {
      logger.error("Failed to read file: " + inputFile.getAbsolutePath(), e);
    }
    return lines;
  }

  @Override
  public boolean containsPattern(String line) {
    return Pattern.compile(getRegex()).matcher(line).find();
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    try (
        FileOutputStream fos = new FileOutputStream(outFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter b_writer = new BufferedWriter(osw)
    ) {

      for (String line : lines) {
        b_writer.write(line);
        b_writer.newLine();
      }
    } catch (IOException e) {
      logger.error("Failed to write to file: {}", outFile, e);
      throw e;
    }
  }


}
