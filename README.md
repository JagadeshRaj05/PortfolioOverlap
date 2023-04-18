# Portfolio Overlap

Application is to run series of commands related to user portfolio.
- CURRENT_PORTFOLIO
- CALCULATE_OVERLAP
- OVERLAP_PERCENTAGE
- ADD_STOCK.

## Prerequisite

- Java 8

## How to run

- Build application - In root folder run `./gradlew clean build`
- Run application - In root folder `java build/libs/portfoliooverlap-1.0.0.jar <input file absolute path>`
    - ex:  `java build/libs/portfoliooverlap-1.0.0.jar /Users/jagadesh.raj/workspace/portfoliooverlap/src/main/resources/input1.txt`
    - If no input file  is given, application will use defaults inputs in resource folder (input_1 & input_2 - mentioned in [codu.ai problem](https://codu.ai/coding-problem/portfolio-overlap)

> **Note**: If you want to run application using one of the default input run `java build/libs/portfoliooverlap-1.0.0.jar src/main/resources/(input1.txt|input2.txt)`

## Accomplishments

- Given problem statement
- 97% code coverage

## Extras

-  google-java-format plugin - Code formatter
    - Format the code automatically while building
    - To format manually, run `./gradlew spotlessApply`
- jacoco report & verification
    - Verification rule is 0.9 (90% minimum)
    - Report location - `build/reports/jacoco/test/html/index.html`  after running `./gradlew clean (build|test)`
