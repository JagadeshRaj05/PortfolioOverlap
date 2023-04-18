package com.fabric.portfoliooverlap.service.commands;

import com.fabric.portfoliooverlap.exception.FundNotFoundException;
import com.fabric.portfoliooverlap.exception.InvalidCommandException;
import com.fabric.portfoliooverlap.model.CommandExecutionContext;
import com.fabric.portfoliooverlap.model.CommandExecutionContextHolder;
import com.fabric.portfoliooverlap.model.Fund;
import com.fabric.portfoliooverlap.service.Command;
import org.springframework.stereotype.Service;

@Service
public class AddStockInFundCommand implements Command {

  public static final int MINIMUM_COMMAND_SEGMENTS = 3;

  private String getStockName(String commandOperation, String fundName) {
    int startingIndexOfFund = commandOperation.indexOf(fundName);
    return commandOperation.substring(startingIndexOfFund + fundName.length() + 1);
  }

  @Override
  public void execute(String commandOperation) {
    String[] commandSegments = commandOperation.split("\\s");

    if (commandSegments.length < MINIMUM_COMMAND_SEGMENTS) {
      throw new InvalidCommandException(
          String.format("Given command operation - %s is invalid", commandOperation));
    }

    String fundName = commandSegments[1];
    String stockName = getStockName(commandOperation, fundName);
    CommandExecutionContext commandExecutionContext =
        CommandExecutionContextHolder.getCommandExecutionContext();

    /* Problem statement has not mentioned anything about adding stock to user funds
     * So Assuming that operations happens at fund level */
    Fund fundToAddStock =
        commandExecutionContext.safeGetAvailableFunds().stream()
            .filter(funds -> funds.getName().equals(fundName))
            .findFirst()
            .orElseThrow(
                () ->
                    new FundNotFoundException(
                        String.format("Invalid fund - %s to add stock", fundName)));
    fundToAddStock.getStocks().add(stockName);
  }
}
