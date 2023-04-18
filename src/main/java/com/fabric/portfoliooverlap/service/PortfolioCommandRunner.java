package com.fabric.portfoliooverlap.service;

import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class PortfolioCommandRunner implements CommandLineRunner {

  private final PortfolioOverlapService portfolioOverlapService;

  @Override
  public void run(String... args) {
    log.info("******* Initiating PortfolioOverlap application *******");
    log.info("For more information on how to run part, please refer README.md");

    if (args.length > 0) {
      boolean isInputsNotFromClassPath = Paths.get(args[0]).isAbsolute();
      portfolioOverlapService.processSeriesOfCommandsFromFile(args[0], !isInputsNotFromClassPath);
    } else {
      log.info("No input file given so running for defaults");
      log.info("Running portfolio overlap commands - input1.txt");
      portfolioOverlapService.processSeriesOfCommandsFromFile("input1.txt", true);
      log.info("Running portfolio overlap commands - input2.txt");
      portfolioOverlapService.processSeriesOfCommandsFromFile("input2.txt", true);
    }
  }
}
