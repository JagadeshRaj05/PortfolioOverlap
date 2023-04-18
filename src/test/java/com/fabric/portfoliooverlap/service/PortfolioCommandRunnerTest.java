package com.fabric.portfoliooverlap.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PortfolioCommandRunnerTest {

  @InjectMocks private PortfolioCommandRunner portfolioCommandRunner;

  @Mock private PortfolioOverlapService portfolioOverlapService;

  @Test
  void shouldProcessCommandsWhenUserInputsSystemFile() {
    // Act
    String inputFile = "/absolute_path.txt";
    portfolioCommandRunner.run(inputFile);

    // Verify
    verify(portfolioOverlapService, times(1))
        .processSeriesOfCommandsFromFile(eq(inputFile), eq(false));
  }

  @Test
  void shouldProcessCommandsWhenUserInputsResourceFile() {
    // Act
    String inputFile = "absolute_path.txt";
    portfolioCommandRunner.run(inputFile);

    // Verify
    verify(portfolioOverlapService, times(1))
        .processSeriesOfCommandsFromFile(eq(inputFile), eq(true));
  }

  @Test
  void shouldProcessDefaultsInputsWhenUserDidNotPassAnyInputs() {
    // Act
    portfolioCommandRunner.run();

    // Verify
    verify(portfolioOverlapService, times(1))
        .processSeriesOfCommandsFromFile(eq("input1.txt"), eq(true));
    verify(portfolioOverlapService, times(1))
        .processSeriesOfCommandsFromFile(eq("input2.txt"), eq(true));
  }

  @Test
  void shouldThrowExceptionIfProcessingCommandFails() {
    // Prepare
    String inputFile = "absolute_path.txt";
    doThrow(RuntimeException.class)
        .when(portfolioOverlapService)
        .processSeriesOfCommandsFromFile(inputFile, true);

    // Act
    assertThrows(RuntimeException.class, () -> portfolioCommandRunner.run(inputFile));

    // Verify
    verify(portfolioOverlapService, times(1))
        .processSeriesOfCommandsFromFile(eq(inputFile), eq(true));
  }
}
