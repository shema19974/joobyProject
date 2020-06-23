package jp.diveintocode.document;

import java.util.ArrayList;
import java.util.List;
import jp.diveintocode.exception.ApiException;
import jp.diveintocode.exception.ApiExceptionConstant;
import org.apache.commons.lang3.StringUtils;

/** 採点モデル */
public class Scoring {
  private List<Integer> testCaseIds;
  private String submissionCode;

  public Scoring() {
    this.testCaseIds = new ArrayList<>();
  }

  /**
   * 採点対象のコードを取得します
   *
   * @return
   */
  public String getSubmissionCode() {
    return submissionCode;
  }

  /**
   * 採点対象のコードを設定します
   *
   * @param submissionCode
   */
  public void setSubmissionCode(String submissionCode) {
    this.submissionCode = submissionCode;
  }

  /**
   * テストケースIDのリストを取得します
   *
   * @return
   */
  public List<Integer> getTestCaseIds() {
    return testCaseIds;
  }

  public void setTestCaseIds(List<Integer> testCaseIds) {
    this.testCaseIds = testCaseIds;
  }

  public void valid() {
    String errorMsg = null;
    if (testCaseIds == null || testCaseIds.isEmpty()) {
      errorMsg = "TestCaseIds is empty.";
    } else if (StringUtils.isEmpty(submissionCode)) {
      errorMsg = "SubmissionCode is empty.";
    }
    if (errorMsg != null) {
      throw new ApiException(ApiExceptionConstant.ILLEGAL_ARGUMENT_EXCEPTION, errorMsg);
    }
  }
}
