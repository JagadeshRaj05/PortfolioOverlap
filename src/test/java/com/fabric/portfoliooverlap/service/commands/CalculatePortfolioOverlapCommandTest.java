package com.fabric.portfoliooverlap.service.commands;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static net.javacrumbs.jsonunit.core.Option.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fabric.portfoliooverlap.exception.FundNotFoundException;
import com.fabric.portfoliooverlap.exception.InvalidCommandException;
import com.fabric.portfoliooverlap.model.CommandExecutionContext;
import com.fabric.portfoliooverlap.model.CommandExecutionContextHolder;
import com.fabric.portfoliooverlap.model.Funds;
import com.fabric.portfoliooverlap.model.PortfolioOverlapDetails;
import com.fabric.portfoliooverlap.utility.DataFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalculatePortfolioOverlapCommandTest {

  @InjectMocks private CalculatePortfolioOverlapCommand calculatePortfolioOverlapCommand;

  @Test
  void shouldThrowInvalidCommandExceptionIfCommandLengthIsInvalid() {
    // Act & Assert
    String command = "CALCULATE_OVERLAP";
    InvalidCommandException invalidCommandException =
        assertThrows(
            InvalidCommandException.class, () -> calculatePortfolioOverlapCommand.execute(command));
    assertEquals(
        "Given command operation - ADD_STOCK AXIS_BLUECHIP is invalid",
        invalidCommandException.getMessage());
  }

  @Test
  void shouldThrowFundNotFoundExceptionWhenAvailableFundIsNotConfigured() {
    // Prepare
    CommandExecutionContextHolder.setCommandExecutionContext(new CommandExecutionContext(null));

    // Act & Assert
    String command = "CALCULATE_OVERLAP MIRAE_ASSET_EMERGING_BLUECHIP";
    FundNotFoundException fundNotFoundException =
        assertThrows(
            FundNotFoundException.class, () -> calculatePortfolioOverlapCommand.execute(command));
    assertEquals("Invalid fundName to calculate overlap", fundNotFoundException.getMessage());
  }

  @Test
  void shouldThrowFundNotFoundExceptionWhenGivenFundIsNotFound() {
    // Prepare
    List<Funds> availableFunds =
        DataFactory.getResource("available_funds.json", new TypeReference<List<Funds>>() {});
    CommandExecutionContextHolder.setCommandExecutionContext(
        new CommandExecutionContext(availableFunds));

    // Act & Assert
    String command = "CALCULATE_OVERLAP INVALID_FUND";
    FundNotFoundException fundNotFoundException =
        assertThrows(
            FundNotFoundException.class, () -> calculatePortfolioOverlapCommand.execute(command));
    assertEquals("Invalid fundName to calculate overlap", fundNotFoundException.getMessage());
  }

  @Test
  void shouldThrowFundNotFoundExceptionWhenUserFundIsNotConfigured() {
    // Prepare
    List<Funds> availableFunds =
        DataFactory.getResource("available_funds.json", new TypeReference<List<Funds>>() {});
    CommandExecutionContextHolder.setCommandExecutionContext(
        new CommandExecutionContext(availableFunds));

    // Act & Assert
    String command = "CALCULATE_OVERLAP MIRAE_ASSET_EMERGING_BLUECHIP";
    FundNotFoundException fundNotFoundException =
        assertThrows(
            FundNotFoundException.class, () -> calculatePortfolioOverlapCommand.execute(command));
    assertEquals(
        "At least single user fund is expected here. Please run create portfolio command",
        fundNotFoundException.getMessage());
  }

  @Test
  void shouldCalculatePortfolioOverlapAndUpdateInCommandExecutionContext() {
    // Prepare
    List<Funds> availableFunds =
        DataFactory.getResource("available_funds.json", new TypeReference<List<Funds>>() {});
    List<Funds> userFunds =
        DataFactory.getResource("user_funds.json", new TypeReference<List<Funds>>() {});

    CommandExecutionContextHolder.setCommandExecutionContext(
        new CommandExecutionContext(availableFunds));
    CommandExecutionContext commandExecutionContext =
        CommandExecutionContextHolder.getCommandExecutionContext();
    commandExecutionContext.updateUserFunds(userFunds);

    // Act
    String command = "CALCULATE_OVERLAP MIRAE_ASSET_EMERGING_BLUECHIP";
    calculatePortfolioOverlapCommand.execute(command);

    // Assert
    List<PortfolioOverlapDetails> expectedLastRunPortfolioOverlapDetails =
        DataFactory.getResource(
            "last_run_portfolio_overlap_details.json",
            new TypeReference<List<PortfolioOverlapDetails>>() {});
    List<PortfolioOverlapDetails> actualLastRunPortfolioOverlapDetails =
        commandExecutionContext.getLastRunPortfolioOverlapDetails();
    assertJsonEquals(
        expectedLastRunPortfolioOverlapDetails,
        actualLastRunPortfolioOverlapDetails,
        JsonAssert.when(IGNORING_ARRAY_ORDER));
  }
}
