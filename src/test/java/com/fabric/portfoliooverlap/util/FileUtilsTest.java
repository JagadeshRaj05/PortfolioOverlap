package com.fabric.portfoliooverlap.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fabric.portfoliooverlap.exception.FileProcessingException;
import com.fabric.portfoliooverlap.utility.DataFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class FileUtilsTest {

  @Test
  void shouldThrowExceptionIfReadContentAsLinesFails() {
    // Act & Assert
    FileProcessingException fileProcessingException =
        assertThrows(
            FileProcessingException.class,
            () -> FileUtils.readContentAsLines("invalid_file.txt", true));
    assertEquals(
        "Error while processing file in path invalid_file.txt, error class path resource [invalid_file.txt] cannot be resolved to URL because it does not exist",
        fileProcessingException.getMessage());
  }

  @Test
  void shouldReadContentsAsLinesFromClassPathFile() {
    // Act
    List<String> actualLines = FileUtils.readContentAsLines("input/sample.txt", true);

    // Assert
    List<String> expectedLines =
        DataFactory.getResource("expected/sample.json", new TypeReference<List<String>>() {});
    assertEquals(expectedLines, actualLines);
  }

  @Test
  void shouldReadContentsAsLinesFromSystemFile() {
    // Prepare
    List<String> expectedLines =
        DataFactory.getResource("expected/sample.json", new TypeReference<List<String>>() {});

    try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {
      filesMock
          .when(() -> Files.readAllLines(Paths.get("input/sample.txt")))
          .thenReturn(expectedLines);

      // Act
      List<String> actualLines = FileUtils.readContentAsLines("input/sample.txt", false);

      // Assert
      assertEquals(expectedLines, actualLines);
    }
  }

  @Test
  void shouldThrowExceptionIfReadContentAsJsonFails() {
    // Act & Assert
    FileProcessingException fileProcessingException =
        assertThrows(
            FileProcessingException.class,
            () -> FileUtils.readContentAsJson("invalid_file.json", Object.class, true));
    assertEquals(
        "Error while processing file in path invalid_file.json, error class path resource [invalid_file.json] cannot be resolved to URL because it does not exist",
        fileProcessingException.getMessage());
  }

  @Test
  void shouldReadContentAsJsonFromClassPathFile() {
    // Act
    JsonNode actualObject = FileUtils.readContentAsJson("input/sample.json", JsonNode.class, true);

    // Assert
    JsonNode expectedObject = DataFactory.getResource("expected/sample.json", JsonNode.class);
    assertEquals(expectedObject, actualObject);
  }
}
