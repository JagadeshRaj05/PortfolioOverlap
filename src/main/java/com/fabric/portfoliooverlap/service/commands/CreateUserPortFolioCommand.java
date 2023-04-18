package com.fabric.portfoliooverlap.service.commands;

import static com.fabric.portfoliooverlap.model.CommandType.CURRENT_PORTFOLIO;

import com.fabric.portfoliooverlap.exception.InvalidCommandException;
import com.fabric.portfoliooverlap.model.CommandExecutionContext;
import com.fabric.portfoliooverlap.model.CommandExecutionContextHolder;
import com.fabric.portfoliooverlap.model.Fund;
import com.fabric.portfoliooverlap.service.Command;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CreateUserPortFolioCommand implements Command {

  public static final int MINIMUM_COMMAND_SEGMENTS = 2;

  private Set<String> getUserFundsToCreatePortFolio(String commandOperation) {
    String[] commandSegments = commandOperation.split("\\s");

    if (commandSegments.length < MINIMUM_COMMAND_SEGMENTS) {
      throw new InvalidCommandException(
          String.format("Given command operation - %s is invalid", commandOperation));
    }

    return Arrays.stream(commandSegments)
        .filter(s -> !s.equals(CURRENT_PORTFOLIO.toString()))
        .collect(Collectors.toSet());
  }

  @Override
  public void execute(String commandOperation) {
    CommandExecutionContext commandExecutionContext =
        CommandExecutionContextHolder.getCommandExecutionContext();

    Set<String> userFundsToCreatePortFolio = getUserFundsToCreatePortFolio(commandOperation);
    List<Fund> userFunds =
        commandExecutionContext.safeGetAvailableFunds().stream()
            .filter(funds -> userFundsToCreatePortFolio.contains(funds.getName()))
            .collect(Collectors.toList());
    commandExecutionContext.updateUserFunds(userFunds);
  }
}
