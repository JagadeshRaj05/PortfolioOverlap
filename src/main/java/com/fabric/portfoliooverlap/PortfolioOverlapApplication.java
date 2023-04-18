package com.fabric.portfoliooverlap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PortfolioOverlapApplication {

  public static void main(String[] args) {
    SpringApplication portfolioApplication =
        new SpringApplication(PortfolioOverlapApplication.class);
    portfolioApplication.run(args);
  }
}
