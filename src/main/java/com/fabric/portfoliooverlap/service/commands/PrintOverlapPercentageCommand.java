package com.fabric.portfoliooverlap.service.commands;

import com.fabric.portfoliooverlap.exception.InvalidCommandException;
import com.fabric.portfoliooverlap.model.CommandExecutionContextHolder;
import com.fabric.portfoliooverlap.model.PortfolioOverlapDetails;
import com.fabric.portfoliooverlap.service.Command;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PrintOverlapPercentageCommand implements Command {

  /* This command is not used in inputs given in codu.ai
   * they are using CALCULATE command to print the percentage
   * however added the implementations (Just in case) */

  @Override
  public void execute(String commandOperation) {
    // To replicate the output as such used system out instead of logger
    List<PortfolioOverlapDetails> lastRunPortfolioOverlapDetails =
        CommandExecutionContextHolder.getCommandExecutionContext()
            .getLastRunPortfolioOverlapDetails();

    if (lastRunPortfolioOverlapDetails == null || lastRunPortfolioOverlapDetails.isEmpty()) {
      throw new InvalidCommandException(
          "Please run calculate overlap command before running print command");
    }

    lastRunPortfolioOverlapDetails.forEach(System.out::println);
  }
}
