package com.fabric.portfoliooverlap.model;

import com.fabric.portfoliooverlap.exception.InvalidCommandException;
import com.fabric.portfoliooverlap.service.commands.AddStockInFundCommand;
import com.fabric.portfoliooverlap.service.commands.CalculatePortfolioOverlapCommand;
import com.fabric.portfoliooverlap.service.commands.CreateUserPortFolioCommand;
import com.fabric.portfoliooverlap.service.commands.PrintOverlapPercentageCommand;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommandType {
  CURRENT_PORTFOLIO(CreateUserPortFolioCommand.class),
  CALCULATE_OVERLAP(CalculatePortfolioOverlapCommand.class),
  ADD_STOCK(AddStockInFundCommand.class),
  OVERLAP_PERCENTAGE(PrintOverlapPercentageCommand.class);

  private final Class getCommandClassType;

  public static CommandType getCommandType(String value) {
    return Arrays.stream(CommandType.values())
        .filter(commandType -> commandType.name().equals(value))
        .findFirst()
        .orElseThrow(
            () -> new InvalidCommandException(String.format("Invalid command %s is given", value)));
  }
}
