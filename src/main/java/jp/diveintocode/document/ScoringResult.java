package jp.diveintocode.document;

import java.util.List;

public class ScoringResult {
  private boolean isAllPass;
  private List<TestCaseResult> testCaseResults;

  public ScoringResult(boolean isAllPass, List<TestCaseResult> testCaseResults) {
    this.isAllPass = isAllPass;
    this.testCaseResults = testCaseResults;
  }

  public boolean isAllPass() {
    return isAllPass;
  }

  public List<TestCaseResult> getTestCaseResults() {
    return testCaseResults;
  }
}
