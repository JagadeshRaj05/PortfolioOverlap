package com.fabric.portfoliooverlap.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static String convertToString(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException ex) {
      throw new RuntimeException(
          String.format("Error while converting object to string, error = %s", ex.getMessage()),
          ex);
    }
  }
}
