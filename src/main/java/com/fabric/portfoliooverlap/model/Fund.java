package com.fabric.portfoliooverlap.model;

import com.fabric.portfoliooverlap.util.CommonUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Fund {

  private String name;
  private List<String> stocks;

  @Override
  public String toString() {
    return CommonUtils.convertToString(this);
  }
}
