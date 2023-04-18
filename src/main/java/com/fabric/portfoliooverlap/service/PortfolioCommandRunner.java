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
    log.info(
        "If you want to use file from your system directory give absolute path eg: /Users/jagadesh.raj/workspace/portfoliooverlap/src/main/resources/input1.txt");
    log.info(
        "Else if you want to use existing inputs in resource folder give file name alone eg: input1.txt or input2.txt");
    log.info("For more information, please refer README.md");

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
