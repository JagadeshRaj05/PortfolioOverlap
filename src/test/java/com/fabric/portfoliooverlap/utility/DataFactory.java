package com.fabric.portfoliooverlap.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import org.springframework.core.io.ClassPathResource;

public class DataFactory {

  public static <T> T getResource(String path, TypeReference<T> type) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    ClassPathResource classPathResource = new ClassPathResource(path);
    try (BufferedReader bufferedReader =
        Files.newBufferedReader(classPathResource.getFile().toPath())) {
      return mapper.readValue(bufferedReader, type);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static <T> T getResource(String path, Class<T> type) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    ClassPathResource classPathResource = new ClassPathResource(path);
    try (BufferedReader bufferedReader =
        Files.newBufferedReader(classPathResource.getFile().toPath())) {
      return mapper.readValue(bufferedReader, type);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
