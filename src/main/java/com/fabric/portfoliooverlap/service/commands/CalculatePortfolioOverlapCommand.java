package com.fabric.portfoliooverlap.service.commands;

import com.fabric.portfoliooverlap.exception.FundNotFoundException;
import com.fabric.portfoliooverlap.exception.InvalidCommandException;
import com.fabric.portfoliooverlap.model.CommandExecutionContext;
import com.fabric.portfoliooverlap.model.CommandExecutionContextHolder;
import com.fabric.portfoliooverlap.model.Fund;
import com.fabric.portfoliooverlap.model.PortfolioOverlapDetails;
import com.fabric.portfoliooverlap.service.Command;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CalculatePortfolioOverlapCommand implements Command {

  public static final int MINIMUM_COMMAND_SEGMENTS = 2;
  public static final String ROUNDING_PATTERN = "0.00";

  @Override
  public void execute(String commandOperation) {
    CommandExecutionContext commandExecutionContext =
        CommandExecutionContextHolder.getCommandExecutionContext();
    String[] commandSegments = commandOperation.split("\\s");

    if (commandSegments.length < MINIMUM_COMMAND_SEGMENTS) {
      throw new InvalidCommandException(
          String.format("Given command operation - %s is invalid", commandOperation));
    }

    String fundName = commandSegments[1];
    Fund fundToCalculateOverlap =
        commandExecutionContext.safeGetAvailableFunds().stream()
            .filter(funds -> funds.getName().equals(fundName))
            .findFirst()
            .orElseThrow(() -> new FundNotFoundException("Invalid fundName to calculate overlap"));

    List<Fund> userFunds = commandExecutionContext.getUserFunds();
    if (userFunds == null || userFunds.isEmpty()) {
      throw new FundNotFoundException(
          "At least single user fund is expected here. Please run create portfolio command");
    }

    // To replicate the output as such used system out instead of logger
    List<PortfolioOverlapDetails> lastRunPortfolioOverlapDetails =
        userFunds.stream()
            .map(userFund -> calculateOverlap(fundToCalculateOverlap, userFund))
            .peek(System.out::println)
            .collect(Collectors.toList());

    commandExecutionContext.updateLastRunPortfolioOverlapDetails(lastRunPortfolioOverlapDetails);
  }

  private PortfolioOverlapDetails calculateOverlap(Fund source, Fund target) {
    HashSet<String> sourceStocks = new HashSet<>(source.getStocks());
    sourceStocks.retainAll(target.getStocks());

    int overallCommonStocks = sourceStocks.size();
    double overlapPercentage =
        ((2.0 * overallCommonStocks) / (source.getStocks().size() + target.getStocks().size()))
            * 100.0;
    return new PortfolioOverlapDetails(
        source.getName(), target.getName(), getRoundedOverlapPercentage(overlapPercentage));
  }

  private String getRoundedOverlapPercentage(double overlapPercentage) {
    DecimalFormat decimalFormat = new DecimalFormat(ROUNDING_PATTERN);
    decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
    return decimalFormat.format(overlapPercentage);
  }
}
