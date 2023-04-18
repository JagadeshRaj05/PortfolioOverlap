package com.fabric.portfoliooverlap.service;

import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fabric.portfoliooverlap.exception.InvalidCommandException;
import com.fabric.portfoliooverlap.model.FundDetails;
import com.fabric.portfoliooverlap.model.Fund;
import com.fabric.portfoliooverlap.service.commands.CalculatePortfolioOverlapCommand;
import com.fabric.portfoliooverlap.service.commands.CreateUserPortFolioCommand;
import com.fabric.portfoliooverlap.util.FileUtils;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PortfolioOverlapServiceTest {

  @InjectMocks private PortfolioOverlapService portfolioOverlapService;

  @Mock private CommandFactory commandFactory;

  @Mock private FundService fundService;

  @Mock private CalculatePortfolioOverlapCommand calculatePortfolioOverlapCommand;

  @Test
  void shouldCommandProcessingThrowExceptionIfFetchAvailableFundFails() {
    // Prepare
    when(fundService.fetchFundDetails()).thenThrow(RuntimeException.class);

    // Act & Assert
    assertThrows(
        RuntimeException.class,
        () -> portfolioOverlapService.processSeriesOfCommandsFromFile("input1.txt", true));

    verify(fundService, times(1)).fetchFundDetails();
    verify(commandFactory, times(0)).getCommand(any());
  }

  @Test
  void shouldCommandProcessingThrowExceptionIfInputDataIsCorrupt() {
    // Prepare
    when(fundService.fetchFundDetails())
        .thenReturn(
            new FundDetails(singletonList(new Fund("AXIS_BLUECHIP", singletonList("TCS")))));

    try (MockedStatic<FileUtils> fileUtilsMock = Mockito.mockStatic(FileUtils.class)) {
      fileUtilsMock
          .when(() -> FileUtils.readContentAsLines("input1.txt", true))
          .thenThrow(RuntimeException.class);

      // Act & Assert
      assertThrows(
          RuntimeException.class,
          () -> portfolioOverlapService.processSeriesOfCommandsFromFile("input1.txt", true));

      verify(fundService, times(1)).fetchFundDetails();
      verify(commandFactory, times(0)).getCommand(any());
    }
  }

  @Test
  void shouldExecuteAllCommandsEvenIfIntermediateCommandFails() {
    // Prepare
    when(fundService.fetchFundDetails())
        .thenReturn(
            new FundDetails(singletonList(new Fund("AXIS_BLUECHIP", singletonList("TCS")))));
    when(commandFactory.getCommand(CreateUserPortFolioCommand.class))
        .thenThrow(InvalidCommandException.class);
    when(commandFactory.getCommand(CalculatePortfolioOverlapCommand.class))
        .thenReturn(calculatePortfolioOverlapCommand);
    doThrow(RuntimeException.class).when(calculatePortfolioOverlapCommand).execute(any());

    try (MockedStatic<FileUtils> fileUtilsMock = Mockito.mockStatic(FileUtils.class)) {
      fileUtilsMock
          .when(() -> FileUtils.readContentAsLines("input1.txt", true))
          .thenReturn(
              Arrays.asList(
                  "CURRENT_PORTFOLIO AXIS_BLUECHIP ICICI_PRU_BLUECHIP UTI_NIFTY_INDEX",
                  "INVALID_COMMAND MIRAE_ASSET_EMERGING_BLUECHIP",
                  "CALCULATE_OVERLAP MIRAE_ASSET_LARGE_CAP",
                  "ADD_STOCK AXIS_BLUECHIP TCS",
                  "CALCULATE_OVERLAP MIRAE_ASSET_EMERGING_BLUECHIP"));

      // Act & Assert
      portfolioOverlapService.processSeriesOfCommandsFromFile("input1.txt", true);

      verify(fundService, times(1)).fetchFundDetails();
      verify(commandFactory, times(4))
          .getCommand(
              any()); // Since one command type itself wrong factory will be invoked 4 times only
    }
  }

  @Test
  void shouldExecuteAllCommands() {
    // Prepare
    when(fundService.fetchFundDetails())
        .thenReturn(
            new FundDetails(singletonList(new Fund("AXIS_BLUECHIP", singletonList("TCS")))));
    when(commandFactory.getCommand(CreateUserPortFolioCommand.class))
        .thenThrow(InvalidCommandException.class);
    when(commandFactory.getCommand(CalculatePortfolioOverlapCommand.class))
        .thenReturn(calculatePortfolioOverlapCommand);

    try (MockedStatic<FileUtils> fileUtilsMock = Mockito.mockStatic(FileUtils.class)) {
      fileUtilsMock
          .when(() -> FileUtils.readContentAsLines("input1.txt", true))
          .thenReturn(
              Arrays.asList(
                  "CURRENT_PORTFOLIO AXIS_BLUECHIP ICICI_PRU_BLUECHIP UTI_NIFTY_INDEX",
                  "CALCULATE_OVERLAP MIRAE_ASSET_EMERGING_BLUECHIP",
                  "CALCULATE_OVERLAP MIRAE_ASSET_LARGE_CAP",
                  "ADD_STOCK AXIS_BLUECHIP TCS",
                  "CALCULATE_OVERLAP MIRAE_ASSET_EMERGING_BLUECHIP"));

      // Act & Assert
      portfolioOverlapService.processSeriesOfCommandsFromFile("input1.txt", true);

      verify(fundService, times(1)).fetchFundDetails();
      verify(commandFactory, times(5)).getCommand(any());
    }
  }
}
