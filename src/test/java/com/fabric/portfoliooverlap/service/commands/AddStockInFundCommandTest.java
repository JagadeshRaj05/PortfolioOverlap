package com.fabric.portfoliooverlap.service.commands;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.junit.jupiter.api.Assertions.*;

import com.fabric.portfoliooverlap.exception.FundNotFoundException;
import com.fabric.portfoliooverlap.exception.InvalidCommandException;
import com.fabric.portfoliooverlap.model.CommandExecutionContext;
import com.fabric.portfoliooverlap.model.CommandExecutionContextHolder;
import com.fabric.portfoliooverlap.model.Funds;
import com.fabric.portfoliooverlap.utility.DataFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddStockInFundCommandTest {

  @InjectMocks private AddStockInFundCommand addStockInFundCommand;

  @AfterEach
  void clear() {
    CommandExecutionContextHolder.clearCommandExecutionContext();
  }

  @Test
  void shouldThrowInvalidCommandExceptionIfCommandLengthIsInvalid() {
    // Act & Assert
    String command = "ADD_STOCK AXIS_BLUECHIP";
    InvalidCommandException invalidCommandException =
        assertThrows(InvalidCommandException.class, () -> addStockInFundCommand.execute(command));
    assertEquals(
        "Given command operation - ADD_STOCK AXIS_BLUECHIP is invalid",
        invalidCommandException.getMessage());
  }

  @Test
  void shouldThrowFundNotFoundExceptionIfAvailableFundIsNotConfigured() {
    // Prepare
    CommandExecutionContextHolder.setCommandExecutionContext(new CommandExecutionContext());

    // Act & Assert
    String command = "ADD_STOCK AXIS_BLUECHIP TCS";
    FundNotFoundException fundNotFoundException =
        assertThrows(FundNotFoundException.class, () -> addStockInFundCommand.execute(command));
    assertEquals("Invalid fund - AXIS_BLUECHIP to add stock", fundNotFoundException.getMessage());
  }

  @Test
  void shouldThrowFundNotFoundExceptionIfGivenFundNameIsInvalid() {
    // Prepare
    List<Funds> availableFunds =
        DataFactory.getResource("available_funds.json", new TypeReference<List<Funds>>() {});
    CommandExecutionContext commandExecutionContext = new CommandExecutionContext(availableFunds);
    CommandExecutionContextHolder.setCommandExecutionContext(commandExecutionContext);

    // Act & Assert
    String command = "ADD_STOCK INVALID_FUND_NAME TCS";
    FundNotFoundException fundNotFoundException =
        assertThrows(FundNotFoundException.class, () -> addStockInFundCommand.execute(command));
    assertEquals(
        "Invalid fund - INVALID_FUND_NAME to add stock", fundNotFoundException.getMessage());
  }

  @Test
  void shouldAddSingleWordStockToFundProperly() {
    // Prepare
    List<Funds> availableFunds =
        DataFactory.getResource("available_funds.json", new TypeReference<List<Funds>>() {});
    CommandExecutionContext commandExecutionContext = new CommandExecutionContext(availableFunds);
    CommandExecutionContextHolder.setCommandExecutionContext(commandExecutionContext);

    // Act
    String command = "ADD_STOCK AXIS_BLUECHIP TCS";
    addStockInFundCommand.execute(command);

    // Assert
    List<Funds> actualAvailableFunds = commandExecutionContext.getAvailableFunds();
    List<Funds> expectedAvailableFunds =
        DataFactory.getResource(
            "available_funds_added_single_word_stock.json", new TypeReference<List<Funds>>() {});
    assertJsonEquals(
        expectedAvailableFunds, actualAvailableFunds, JsonAssert.when(IGNORING_ARRAY_ORDER));
  }

  @Test
  void shouldAddMultiwordStockToFundProperly() {
    // Prepare
    List<Funds> availableFunds =
        DataFactory.getResource("available_funds.json", new TypeReference<List<Funds>>() {});
    CommandExecutionContext commandExecutionContext = new CommandExecutionContext(availableFunds);
    CommandExecutionContextHolder.setCommandExecutionContext(commandExecutionContext);

    // Act
    String command = "ADD_STOCK ICICI_PRU_NIFTY_NEXT_50_INDEX TATA CONSULTANCY SERVICE LIMITED";
    addStockInFundCommand.execute(command);

    // Assert
    List<Funds> actualAvailableFunds = commandExecutionContext.getAvailableFunds();
    List<Funds> expectedAvailableFunds =
        DataFactory.getResource(
            "available_funds_added_multi_word_stock.json", new TypeReference<List<Funds>>() {});
    assertJsonEquals(
        expectedAvailableFunds, actualAvailableFunds, JsonAssert.when(IGNORING_ARRAY_ORDER));
  }
}
