package com.fabric.portfoliooverlap.service;

import com.fabric.portfoliooverlap.model.CommandExecutionContext;
import com.fabric.portfoliooverlap.model.CommandExecutionContextHolder;
import com.fabric.portfoliooverlap.model.CommandType;
import com.fabric.portfoliooverlap.model.FundDetails;
import com.fabric.portfoliooverlap.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PortfolioOverlapService {

  private final CommandFactory commandFactory;
  private final FundService fundService;

  public void processSeriesOfCommandsFromFile(String inputPath, boolean isClassPathFile) {
    FundDetails fundDetails = fundService.fetchFundDetails();

    /* Since there is no requirement to do DB actions,
     * using thread local to share context between commands */
    CommandExecutionContextHolder.setCommandExecutionContext(
        new CommandExecutionContext(fundDetails.getFunds()));
    FileUtils.readContentAsLines(inputPath, isClassPathFile).forEach(this::safeExecuteCommand);
    CommandExecutionContextHolder.clearCommandExecutionContext();
  }

  private void safeExecuteCommand(String commandOperation) {
    try {
      CommandType commandType = CommandType.getCommandType(commandOperation.split("\\s")[0]);
      Command command = commandFactory.getCommand(commandType.getGetCommandClassType());
      command.execute(commandOperation);
    } catch (Exception ex) {
      // To replicate the output as mentioned in codu.ai used system out instead of logger
      System.out.println(ex.getMessage());
    }
  }
}
