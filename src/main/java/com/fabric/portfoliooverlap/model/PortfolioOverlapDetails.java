package com.fabric.portfoliooverlap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PortfolioOverlapDetails {

  private String sourceFundName;
  private String targetFundName;
  private String overlapPercentage;

  @Override
  public String toString() {
    return String.format("%s %s %s%%", sourceFundName, targetFundName, overlapPercentage);
  }
}
