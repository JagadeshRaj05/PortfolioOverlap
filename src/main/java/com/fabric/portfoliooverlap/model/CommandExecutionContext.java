package com.fabric.portfoliooverlap.model;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommandExecutionContext {

  private List<Funds> availableFunds;
  private List<Funds> userFunds;
  private List<PortfolioOverlapDetails> lastRunPortfolioOverlapDetails;

  public CommandExecutionContext(List<Funds> availableFunds) {
    this.availableFunds = availableFunds;
  }

  public void updateUserFunds(List<Funds> userFunds) {
    this.userFunds = userFunds;
  }

  public void updateLastRunPortfolioOverlapDetails(
      List<PortfolioOverlapDetails> lastRunPortfolioOverlapDetails) {
    this.lastRunPortfolioOverlapDetails = lastRunPortfolioOverlapDetails;
  }

  public List<Funds> safeGetAvailableFunds() {
    return this.availableFunds != null ? this.availableFunds : Collections.emptyList();
  }
}
