package jp.diveintocode.model;

import java.util.ArrayList;
import java.util.List;
import jp.diveintocode.document.Scoring;
import jp.diveintocode.document.TestCase;
import jp.diveintocode.document.TestSuite;

public class ScoringTarget {
  private TestSuite testSuite;
  private String submissionCode;
  private ProgramLangConst programLangConst;
  private List<TestCase> targetTestCase;

  public ScoringTarget(TestSuite testSuite, Scoring scoring, String lang) {
    this.testSuite = testSuite;
    this.programLangConst = ProgramLangConst.getEnum(lang);
    this.submissionCode = scoring.getSubmissionCode();
    this.targetTestCase = new ArrayList<>();
    for (TestCase testCase : this.testSuite.getTestCases()) {
      if (scoring.getTestCaseIds().contains(testCase.getTestCaseId())) {
        targetTestCase.add(testCase);
      }
    }
  }

  public TestSuite getTestSuite() {
    return testSuite;
  }

  public String getSubmissionCode() {
    return submissionCode;
  }

  public ProgramLangConst getProgramLangConst() {
    return programLangConst;
  }

  public List<TestCase> getTargetTestCase() {
    return targetTestCase;
  }
}
