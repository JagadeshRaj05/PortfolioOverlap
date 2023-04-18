package com.fabric.portfoliooverlap.service.commands;

import static org.junit.jupiter.api.Assertions.*;

import com.fabric.portfoliooverlap.exception.InvalidCommandException;
import com.fabric.portfoliooverlap.model.CommandExecutionContext;
import com.fabric.portfoliooverlap.model.CommandExecutionContextHolder;
import com.fabric.portfoliooverlap.model.PortfolioOverlapDetails;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PrintOverlapPercentageCommandTest {

  @InjectMocks private PrintOverlapPercentageCommand printOverlapPercentageCommand;

  @Test
  void shouldThrowExceptionIfPortfolioOverlapDetailIsNull() {
    // Prepare
    CommandExecutionContextHolder.setCommandExecutionContext(new CommandExecutionContext());

    // Act & Assert
    String command = "OVERLAP_PERCENTAGE";
    InvalidCommandException invalidCommandException =
        assertThrows(
            InvalidCommandException.class, () -> printOverlapPercentageCommand.execute(command));
    assertEquals(
        "Please run calculate overlap command before running print command",
        invalidCommandException.getMessage());
  }

  @Test
  void shouldThrowExceptionIfPortfolioOverlapDetailIsEmpty() {
    // Prepare
    CommandExecutionContext commandExecutionContext = new CommandExecutionContext();
    commandExecutionContext.updateLastRunPortfolioOverlapDetails(Collections.emptyList());
    CommandExecutionContextHolder.setCommandExecutionContext(commandExecutionContext);

    // Act & Assert
    String command = "OVERLAP_PERCENTAGE";
    InvalidCommandException invalidCommandException =
        assertThrows(
            InvalidCommandException.class, () -> printOverlapPercentageCommand.execute(command));
    assertEquals(
        "Please run calculate overlap command before running print command",
        invalidCommandException.getMessage());
  }

  @Test
  void shouldPrintPortfolioOverlapDetails() {
    // Prepare
    CommandExecutionContext commandExecutionContext = new CommandExecutionContext();
    commandExecutionContext.updateLastRunPortfolioOverlapDetails(
        Collections.singletonList(
            new PortfolioOverlapDetails("SOURCE_FUND", "TARGET_FUND", "90%")));
    CommandExecutionContextHolder.setCommandExecutionContext(commandExecutionContext);

    // Act & Assert
    String command = "OVERLAP_PERCENTAGE";
    printOverlapPercentageCommand.execute(command);
  }
}
