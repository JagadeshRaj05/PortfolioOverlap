package com.fabric.portfoliooverlap.model;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommandExecutionContext {

  private List<Fund> availableFunds;
  private List<Fund> userFunds;
  private List<PortfolioOverlapDetails> lastRunPortfolioOverlapDetails;

  public CommandExecutionContext(List<Fund> availableFunds) {
    this.availableFunds = availableFunds;
  }

  public void updateUserFunds(List<Fund> userFunds) {
    this.userFunds = userFunds;
  }

  public void updateLastRunPortfolioOverlapDetails(
      List<PortfolioOverlapDetails> lastRunPortfolioOverlapDetails) {
    this.lastRunPortfolioOverlapDetails = lastRunPortfolioOverlapDetails;
  }

  public List<Fund> safeGetAvailableFunds() {
    return this.availableFunds != null ? this.availableFunds : Collections.emptyList();
  }
}
