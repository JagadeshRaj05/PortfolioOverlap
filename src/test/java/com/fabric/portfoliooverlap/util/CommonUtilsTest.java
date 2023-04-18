package com.fabric.portfoliooverlap.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

class CommonUtilsTest {

  @Test
  void shouldConvertObjectToString() {
    // Act & Assert
    assertEquals("1", CommonUtils.convertToString(1));
    assertEquals("2", CommonUtils.convertToString(2L));
    assertEquals("3.23", CommonUtils.convertToString(3.23));
  }

  @Test
  void shouldThrowExceptionWhenConversionFails() {
    // Prepare
    Object object = mock(Object.class);
    when(object.toString()).thenThrow(RuntimeException.class);

    // Act & Assert
    assertThrows(RuntimeException.class, () -> CommonUtils.convertToString(object));
  }
}
