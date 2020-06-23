package jp.diveintocode.document;

import java.util.List;

public class TestCaseResult {
  TestCase testCase;
  private String actualOutput;
  private boolean isPass;

  public TestCaseResult(boolean isPass, TestCase testCase, List<String> outputList) {
    this.isPass = isPass;
    this.testCase = testCase;
    this.actualOutput = convertToActualOutput(outputList);
  }

  private String convertToActualOutput(List<String> outputList) {
    StringBuilder builder = new StringBuilder();
    for (String output : outputList) {
      builder.append(output).append("\n");
    }
    return builder.toString();
  }

  public String getActualOutput() {
    return actualOutput;
  }

  public boolean isPass() {
    return isPass;
  }

  public TestCase getTestCase() {
    return testCase;
  }
}
