package jp.diveintocode.document;

import jp.diveintocode.exception.ApiExceptionConstant;
import jp.diveintocode.exception.ApiException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TestSuite {
  private String code;
  private List<TestCase> testCases = new ArrayList<>();

  public TestSuite() {}

  public TestSuite(String code, List<TestCase> testCases) {
    this.code = code;
    this.testCases = testCases;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public List<TestCase> getTestCases() {
    return testCases;
  }

  public void valid() {
    String errorMsg = null;
    if (StringUtils.isEmpty(code)) {
      errorMsg = "code is empty.";
    }
    for (TestCase testCase : testCases) {
      testCase.valid();
    }
    if (errorMsg != null) {
      throw new ApiException(ApiExceptionConstant.ILLEGAL_ARGUMENT_EXCEPTION, errorMsg);
    }
  }

  public void setTestCases(List<TestCase> testCases) {
    this.testCases = testCases;
  }
}
