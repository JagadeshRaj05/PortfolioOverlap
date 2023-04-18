package com.fabric.portfoliooverlap.service;

import com.fabric.portfoliooverlap.model.FundDetails;
import com.fabric.portfoliooverlap.util.FileUtils;
import org.springframework.stereotype.Service;

@Service
public class FundService {

  public FundDetails fetchFundDetails() {
    return FileUtils.readContentAsJson("fund_details.json", FundDetails.class, true);
  }
}
