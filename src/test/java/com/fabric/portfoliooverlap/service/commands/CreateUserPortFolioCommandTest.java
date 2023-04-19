package com.fabric.portfoliooverlap.service.commands;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static net.javacrumbs.jsonunit.core.Option.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fabric.portfoliooverlap.exception.InvalidCommandException;
import com.fabric.portfoliooverlap.model.CommandExecutionContext;
import com.fabric.portfoliooverlap.model.CommandExecutionContextHolder;
import com.fabric.portfoliooverlap.model.Fund;
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
class CreateUserPortFolioCommandTest {

  @InjectMocks private CreateUserPortFolioCommand createUserPortFolioCommand;

  @AfterEach
  void clear() {
    CommandExecutionContextHolder.clearCommandExecutionContext();
  }

  @Test
  void shouldThrowInvalidCommandExceptionIfCommandLengthIsInvalid() {
    // Act & Assert
    String command = "CURRENT_PORTFOLIO";
    InvalidCommandException invalidCommandException =
        assertThrows(
            InvalidCommandException.class, () -> createUserPortFolioCommand.execute(command));
    assertEquals(
        "Given command operation - CURRENT_PORTFOLIO is invalid",
        invalidCommandException.getMessage());
  }

  @Test
  void shouldCreateUserPortFolioFromConfiguredAvailableFunds() {
    // Prepare
    List<Fund> availableFunds =
        DataFactory.getResource("available_funds.json", new TypeReference<List<Fund>>() {});
    CommandExecutionContextHolder.setCommandExecutionContext(
        new CommandExecutionContext(availableFunds));

    // Act
    String command = "CURRENT_PORTFOLIO AXIS_BLUECHIP ICICI_PRU_BLUECHIP UTI_NIFTY_INDEX";
    createUserPortFolioCommand.execute(command);

    // Assert
    CommandExecutionContext commandExecutionContext =
        CommandExecutionContextHolder.getCommandExecutionContext();

    List<Fund> actualUserFunds = commandExecutionContext.getUserFunds();
    List<Fund> expectedUserFunds =
        DataFactory.getResource(
            "user_funds_axis_icici_uti.json", new TypeReference<List<Fund>>() {});
    assertJsonEquals(expectedUserFunds, actualUserFunds, JsonAssert.when(IGNORING_ARRAY_ORDER));
  }

  @Test
  void shouldCreateEmptyUserPortFolioIfAvailableFundIsNotConfigured() {
    // Prepare
    CommandExecutionContextHolder.setCommandExecutionContext(new CommandExecutionContext(null));

    // Act
    String command = "CURRENT_PORTFOLIO AXIS_BLUECHIP INVALID_FUND UTI_NIFTY_INDEX";
    createUserPortFolioCommand.execute(command);

    // Assert
    CommandExecutionContext commandExecutionContext =
        CommandExecutionContextHolder.getCommandExecutionContext();

    List<Fund> actualUserFunds = commandExecutionContext.getUserFunds();
    assertTrue(actualUserFunds.isEmpty());
  }

  @Test
  void shouldIgnoreInvalidFundsWhileCreatingUserPortFolio() {
    // Prepare
    List<Fund> availableFunds =
        DataFactory.getResource("available_funds.json", new TypeReference<List<Fund>>() {});
    CommandExecutionContextHolder.setCommandExecutionContext(
        new CommandExecutionContext(availableFunds));

    // Act
    String command = "CURRENT_PORTFOLIO AXIS_BLUECHIP INVALID_FUND UTI_NIFTY_INDEX";
    createUserPortFolioCommand.execute(command);

    // Assert
    CommandExecutionContext commandExecutionContext =
        CommandExecutionContextHolder.getCommandExecutionContext();

    List<Fund> actualUserFunds = commandExecutionContext.getUserFunds();
    List<Fund> expectedUserFunds =
        DataFactory.getResource(
            "user_funds_axis_bluechip_uti_index.json", new TypeReference<List<Fund>>() {});
    assertJsonEquals(expectedUserFunds, actualUserFunds, JsonAssert.when(IGNORING_ARRAY_ORDER));
  }
}
