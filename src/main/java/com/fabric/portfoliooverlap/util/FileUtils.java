package com.fabric.portfoliooverlap.util;

import com.fabric.portfoliooverlap.exception.FileProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;

@UtilityClass
public class FileUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private Path getPath(String filePath, boolean isFileFromClassPath) throws IOException {
    if (isFileFromClassPath) {
      ClassPathResource classPathResource = new ClassPathResource(filePath);
      return classPathResource.getFile().toPath();
    }

    return Paths.get(filePath);
  }

  public static List<String> readContentAsLines(String filePath, boolean isFileFromClassPath)
      throws FileProcessingException {
    try {
      return Files.readAllLines(getPath(filePath, isFileFromClassPath));
    } catch (IOException ex) {
      throw new FileProcessingException(
          String.format(
              "Error while processing file in path %s, error %s", filePath, ex.getMessage()),
          ex);
    }
  }

  public static <T> T readContentAsJson(String input, Class<T> type, boolean isFileFromClassPath)
      throws FileProcessingException {
    try (BufferedReader br =
        new BufferedReader(
            new InputStreamReader(Files.newInputStream(getPath(input, isFileFromClassPath))))) {
      return objectMapper.readValue(br, type);
    } catch (IOException ex) {
      throw new FileProcessingException(
          String.format("Error while processing file in path %s, error %s", input, ex.getMessage()),
          ex);
    }
  }
}
