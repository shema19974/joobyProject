package jp.diveintocode.system;

import com.github.difflib.algorithm.DiffException;
import jp.diveintocode.document.Scoring;
import jp.diveintocode.document.ScoringResult;
import jp.diveintocode.document.TestCase;
import jp.diveintocode.document.TestSuite;
import jp.diveintocode.exception.ApiException;
import jp.diveintocode.model.ScoringTarget;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.*;

public class SystemCallTest {
  @Test
  public void runRuby1() throws IOException, InterruptedException, DiffException {

    // 言語
    String lang = "ruby";

    // 入力値
    String input = "4\n1000\n1992\n2000\n2001";

    // 期待値
    String expected =
        "1000 is not a leap year\n1992 is a leap year\n2000 is a leap year\n2001 is not a leap year";

    // テストケースID
    int testCaseId = 1;

    // 事前テストかどうか
    boolean isPretest = true;

    // テストケース
    TestCase testCase = new TestCase(testCaseId, isPretest, input, expected);

    // テストコード
    String code = "test1";

    // テストスイート
    TestSuite testSuite = new TestSuite(code, Arrays.asList(testCase));

    // 提出コード
    String submissionCode =
        "def isUruYear(year)\n return false if !(year % 4).zero?\n return false if (year % 100).zero? && !(year % 400).zero?\n true\nend\n\ninput_line = gets.to_i\n\ninput_line.times do |index|\n year = gets.to_i\n puts isUruYear(year) ? \"#{year} is a leap year\" : \"#{year} is not a leap year\"\nend";

    Scoring scoring = new Scoring();
    scoring.setSubmissionCode(submissionCode);
    scoring.getTestCaseIds().add(testCaseId);

    // 採点対象
    ScoringTarget scoringTarget = new ScoringTarget(testSuite, scoring, lang);

    // コード実行
    SystemCall systemCall = new SystemCall();

    ScoringResult result = systemCall.run(scoringTarget);
    assertThat(result.isAllPass()).isTrue();
  }

  @Test
  public void runRuby2() throws IOException, InterruptedException, DiffException {

    // 言語
    String lang = "ruby";

    // 入力値
    String input = "4\n1000\n1992\n2000\n2001";

    // 期待値
    String expected =
        "1000 is not a leap year\n1992 is a leap year\n2000 is a leap year\n2001 is not a leap year";

    // テストケースID
    int testCaseId = 1;

    // 事前テストかどうか
    boolean isPretest = true;

    // テストケース
    TestCase testCase = new TestCase(testCaseId, isPretest, input, expected);

    // テストコード
    String code = "test1";

    // テストスイート
    TestSuite testSuite = new TestSuite(code, Arrays.asList(testCase));

    // 提出コード
    String submissionCode = "i = 0 \n" + "while i == 0 do \n" + " puts '無限ループ' \n" + "end";

    Scoring scoring = new Scoring();
    scoring.setSubmissionCode(submissionCode);
    scoring.getTestCaseIds().add(testCaseId);

    // 採点対象
    ScoringTarget scoringTarget = new ScoringTarget(testSuite, scoring, lang);

    // コード実行
    SystemCall systemCall = new SystemCall();

    assertThatThrownBy(
            () -> {
              systemCall.run(scoringTarget);
            })
        .isInstanceOf(ApiException.class);
  }

  @Test
  public void runPython1() throws IOException, InterruptedException, DiffException {

    // 言語
    String lang = "python";

    // 入力値
    String input = "4\n1 2 3\n5 -5\n10.2 -2.2 0.1 1 0.5\n[]";

    // 期待値
    String expected = "6.0\n-50.0\n6.2\n0";

    // テストケースID
    int testCaseId = 1;

    // 事前テストかどうか
    boolean isPretest = true;

    // テストケース
    TestCase testCase = new TestCase(testCaseId, isPretest, input, expected);

    // テストコード
    String code = "test1";

    // テストスイート
    TestSuite testSuite = new TestSuite(code, Arrays.asList(testCase));

    // 提出コード
    String submissionCode =
        "def check_sample(l):\n    if l == []:\n        return 0\n    diff_num = max(l) - min(l)\n    result = diff_num * l[-1]\n    return round(result, 1)\n\nrowNum = int(input())\n\nfor i in range(rowNum):\n    inputs = input()\n    if inputs == \"[]\":\n        inputs = []\n    else:\n        inputs = inputs.rstrip().split(\' \')\n        inputs = list(map(float, inputs))\n    print(check_sample(inputs))";

    // 採点対象

    Scoring scoring = new Scoring();
    scoring.setSubmissionCode(submissionCode);
    scoring.getTestCaseIds().add(testCaseId);
    ScoringTarget scoringTarget = new ScoringTarget(testSuite, scoring, lang);

    // コード実行
    SystemCall systemCall = new SystemCall();

    ScoringResult result = systemCall.run(scoringTarget);
    assertThat(result.isAllPass()).isTrue();
  }
}
