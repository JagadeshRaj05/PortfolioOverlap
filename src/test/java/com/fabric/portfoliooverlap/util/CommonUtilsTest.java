package com.fabric.portfoliooverlap.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CommonUtilsTest {

  @Test
  void shouldReturnNullIfConversionObjectIsNull() {
    // Act & Assert
    assertNull(CommonUtils.convertToString(null));
  }

  @Test
  void shouldConvertObjectToString() {
    // Act & Assert
    assertEquals("1", CommonUtils.convertToString(1));
    assertEquals("2", CommonUtils.convertToString(2L));
    assertEquals("3.23", CommonUtils.convertToString(3.23));
  }
}
