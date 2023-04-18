package com.fabric.portfoliooverlap.service;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

import com.fabric.portfoliooverlap.exception.InvalidCommandException;
import com.fabric.portfoliooverlap.service.commands.AddStockInFundCommand;
import com.fabric.portfoliooverlap.service.commands.CalculatePortfolioOverlapCommand;
import com.fabric.portfoliooverlap.service.commands.CreateUserPortFolioCommand;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CommandFactoryTest {

  @InjectMocks private CommandFactory commandFactory;

  @Spy private CreateUserPortFolioCommand createUserPortFolioCommand;

  @Spy private CalculatePortfolioOverlapCommand calculatePortfolioOverlapCommand;

  @Spy private AddStockInFundCommand addStockInFundCommand;

  @BeforeEach
  void setUp() {
    List<Command> commands =
        asList(createUserPortFolioCommand, calculatePortfolioOverlapCommand, addStockInFundCommand);
    ReflectionTestUtils.setField(commandFactory, "commands", commands);
  }

  @Test
  void shouldReturnCommandByCommandClassType() {
    // Act & Assert
    assertTrue(
        commandFactory.getCommand(CreateUserPortFolioCommand.class)
            instanceof CreateUserPortFolioCommand);
  }

  @Test
  void shouldThrowExceptionForInvalidCommandType() {
    // Act & Assert
    InvalidCommandException invalidCommandException =
        assertThrows(InvalidCommandException.class, () -> commandFactory.getCommand(Object.class));
    assertEquals(
        "Invalid command class type class java.lang.Object given",
        invalidCommandException.getMessage());
  }
}
