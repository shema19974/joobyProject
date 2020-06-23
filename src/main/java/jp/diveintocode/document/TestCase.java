package jp.diveintocode.document;

import jp.diveintocode.exception.ApiExceptionConstant;
import jp.diveintocode.exception.ApiException;
import org.apache.commons.lang3.StringUtils;

public class TestCase {
  private int testCaseId = -1;
  private boolean isPretest;
  private String input;
  private String expectedOutput;

  public TestCase() {}

  public TestCase(int testCaseId, boolean isPretest, String input, String expectedOutput) {
    this.testCaseId = testCaseId;
    this.isPretest = isPretest;
    this.input = input;
    this.expectedOutput = expectedOutput;
  }

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public String getExpectedOutput() {
    return expectedOutput;
  }

  public void setExpectedOutput(String expectedOutput) {
    this.expectedOutput = expectedOutput;
  }

  public boolean isPretest() {
    return isPretest;
  }

  public void setPretest(boolean pretest) {
    this.isPretest = pretest;
  }

  public int getTestCaseId() {
    return testCaseId;
  }

  public void setTestCaseId(int testCaseId) {
    this.testCaseId = testCaseId;
  }

  public void valid() {
    String errorMsg = null;
    if (testCaseId < 0) {
      errorMsg = "testCaseId is invalid. Please set a number greater than 0.";
    } else if (StringUtils.isEmpty(input)) {
      errorMsg = "input is empty.";
    } else if (StringUtils.isEmpty(input)) {
      errorMsg = "expectedOutput is empty.";
    }
    if (errorMsg != null) {
      throw new ApiException(ApiExceptionConstant.ILLEGAL_ARGUMENT_EXCEPTION, errorMsg);
    }
  }
}
