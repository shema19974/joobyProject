package jp.diveintocode.controller;

import com.google.gson.Gson;
import io.jooby.JoobyTest;
import io.restassured.http.ContentType;
import jp.diveintocode.App;
import jp.diveintocode.document.Scoring;
import jp.diveintocode.document.TestCase;
import jp.diveintocode.document.TestSuite;
import jp.diveintocode.repository.TestSuitesRepository;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

@JoobyTest(App.class)
public class ScoringControllerTest {

  Logger logger = LoggerFactory.getLogger(ScoringControllerTest.class);


  final String TEST_DATA_PATH = "/testSuites";
  final String SCORING_PATH = "/scoring";
  final String TEST_CODE = "testCode";
  final String TEST_LANG_RUBY = "ruby";
  final String TEST_LANG_JAVA = "java";
  final String TEST_LANG_PYTHON = "python";
  final int TEST_CASE_ID = 1;

  @Before
  public void initialize() {
    logger.info("initialize");
    TestSuitesRepository repository = new TestSuitesRepository();
    repository.drop();
    repository.initialize();
  }

  private String createTestSuiteJson(String code) {
    List<TestCase> testCaseList = new ArrayList<>();
    TestCase testCase = new TestCase();
    testCase.setTestCaseId(TEST_CASE_ID);
    testCase.setPretest(true);
    testCase.setInput("4\n1000\n1992\n2000\n2001");
    testCase.setExpectedOutput(
        "1000 is not a leap year\n1992 is a leap year\n2000 is a leap year\n2001 is not a leap year");
    testCaseList.add(testCase);
    TestSuite testSuite = new TestSuite();
    testSuite.setCode(code);
    testSuite.setTestCases(testCaseList);
    Gson gson = new Gson();
    return gson.toJson(testSuite);
  }

  private String createTestSuitePythonJson(String code) {
    // 入力値
    String input = "4\n1 2 3\n5 -5\n10.2 -2.2 0.1 1 0.5\n[]";

    // 期待値
    String expected = "6.0\n-50.0\n6.2\n0";

    // テストケースID
    int testCaseId = TEST_CASE_ID;

    // 事前テストかどうか
    boolean isPretest = true;

    // テストケース
    TestCase testCase = new TestCase(testCaseId, isPretest, input, expected);

    // テストスイート
    TestSuite testSuite = new TestSuite(code, Arrays.asList(testCase));

    Gson gson = new Gson();
    return gson.toJson(testSuite);
  }

  private String createSubmitRubyCodeJson() {
    Scoring scoring = new Scoring();
    String submissionCode =
        "def isUruYear(year)\n return false if !(year % 4).zero?\n return false if (year % 100).zero? && !(year % 400).zero?\n true\nend\n\ninput_line = gets.to_i\n\ninput_line.times do |index|\n year = gets.to_i\n puts isUruYear(year) ? \"#{year} is a leap year\" : \"#{year} is not a leap year\"\nend";
    scoring.setSubmissionCode(submissionCode);
    scoring.getTestCaseIds().add(TEST_CASE_ID);
    Gson gson = new Gson();
    return gson.toJson(scoring);
  }

  private String createSubmitErrorRubyCodeJson() {
    Scoring scoring = new Scoring();
    String submissionCode = "テスト";
    scoring.getTestCaseIds().add(TEST_CASE_ID);
    scoring.setSubmissionCode(submissionCode);
    Gson gson = new Gson();
    return gson.toJson(scoring);
  }

  private String createSubmitPythonCodeJson() {
    Scoring scoring = new Scoring();
    String submissionCode =
        "def check_sample(l):\n    if l == []:\n        return 0\n    diff_num = max(l) - min(l)\n    result = diff_num * l[-1]\n    return round(result, 1)\n\nrowNum = int(input())\n\nfor i in range(rowNum):\n    inputs = input()\n    if inputs == \"[]\":\n        inputs = []\n    else:\n        inputs = inputs.rstrip().split(\' \')\n        inputs = list(map(float, inputs))\n    print(check_sample(inputs))";
    scoring.setSubmissionCode(submissionCode);
    scoring.getTestCaseIds().add(TEST_CASE_ID);
    Gson gson = new Gson();
    return gson.toJson(scoring);
  }

  private String createSubmitJavaCodeJson() {
    Scoring scoring = new Scoring();
    String submissionCode =
        "import java.util.*;\n\npublic class Main {\n  public static void main(String[] args) {\n    Scanner sc = new Scanner(System.in);\n    int line = Integer.valueOf(sc.nextLine());\n    for (int i = 0; i < line; i++) {\n      int year = Integer.valueOf(sc.nextLine());\n      String text;\n      if (isUruYear(year)) {\n        text = String.format(\"%d is a leap year\", year);\n      } else {\n        text = String.format(\"%d is not a leap year\", year);\n      }\n      System.out.println(text);\n    }\n  }\n\n  private static boolean isUruYear(int year) {\n    if (year % 4 != 0) {\n      return false;\n    }\n    if ((year % 100) == 0 && (year % 400) != 0) {\n      return false;\n    }\n    return true;\n  }\n}";
    scoring.setSubmissionCode(submissionCode);
    scoring.getTestCaseIds().add(TEST_CASE_ID);
    Gson gson = new Gson();
    return gson.toJson(scoring);
  }

  private String createSubmitPythonNgCodeJson() {
    Scoring scoring = new Scoring();
    String submissionCode =
        "def check_sample(l):\n    if l == []:\n        return 0\n    diff_num = max(l) + min(l)\n    result = diff_num * l[-1]\n    return round(result, 1)\n\nrowNum = int(input())\n\nfor i in range(rowNum):\n    inputs = input()\n    if inputs == \"[]\":\n        inputs = []\n    else:\n        inputs = inputs.rstrip().split(\' \')\n        inputs = list(map(float, inputs))\n    print(check_sample(inputs))";
    scoring.setSubmissionCode(submissionCode);
    scoring.getTestCaseIds().add(TEST_CASE_ID);
    Gson gson = new Gson();
    return gson.toJson(scoring);
  }

  /** runRuby 正常系 /scoring/:code/run/ruby */
  @Test
  public void runRuby1() {

    String testSuiteStr = createTestSuiteJson(TEST_CODE);
    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(testSuiteStr)
      .post(TEST_DATA_PATH)
      .then()
      .assertThat()
      .statusCode(204);

    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(createSubmitRubyCodeJson())
      .post(String.format("%s/%s/run/%s/", SCORING_PATH, TEST_CODE, TEST_LANG_RUBY))
      .then()
      .body("isAllPass", equalTo(true))
      .body("testCaseResults.size()", equalTo(1))
      .body("testCaseResults.testCase.testCaseId", hasItems(TEST_CASE_ID))
      .body("testCaseResults.testCase.isPretest", hasItems(true))
      .body("testCaseResults.isPass", hasItems(true))
      .statusCode(200)
      .log()
      .all();
  }

  /**
   * runRuby /scoring/:code/run/ruby
   * 異常系
   * コードが滅茶苦茶な場合
   * */
  @Test
  public void runRubyError1() {

    String testSuiteStr = createTestSuiteJson(TEST_CODE);
    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(testSuiteStr)
      .post(TEST_DATA_PATH)
      .then()
      .assertThat()
      .statusCode(204);

    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(createSubmitErrorRubyCodeJson())
      .post(String.format("%s/%s/run/%s/", SCORING_PATH, TEST_CODE, TEST_LANG_RUBY))
      .then()
      .body("isAllPass", equalTo(false))
      .body("testCaseResults.size()", equalTo(1))
      .body("testCaseResults.testCase.testCaseId", hasItems(TEST_CASE_ID))
      .body("testCaseResults.testCase.isPretest", hasItems(true))
      .body("testCaseResults.isPass", hasItems(false))
      .statusCode(200)
      .log()
      .all();
  }

  /**
   * runRuby /scoring/:code/run/ruby
   * 異常系
   * codeが間違っている場合
   * */
  @Test
  public void runRubyError2() {

    String testSuiteStr = createTestSuiteJson(TEST_CODE);
    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(testSuiteStr)
      .post(TEST_DATA_PATH)
      .then()
      .assertThat()
      .statusCode(204);

    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(createSubmitJavaCodeJson())
      .post(String.format("%s/%s/run/%s/", SCORING_PATH, "hoge", TEST_LANG_RUBY))
      .then()
      .statusCode(400)
      .log()
      .all();
  }


  @Test
  public void runJava1() {

    String testSuiteStr = createTestSuiteJson(TEST_CODE);
    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(testSuiteStr)
      .post(TEST_DATA_PATH)
      .then()
      .assertThat()
      .statusCode(204);

    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(createSubmitJavaCodeJson())
      .post(String.format("%s/%s/run/%s/", SCORING_PATH, TEST_CODE, TEST_LANG_JAVA))
      .then()
      .body("isAllPass", equalTo(true))
      .body("testCaseResults.size()", equalTo(1))
      .body("testCaseResults.testCase.testCaseId", hasItems(TEST_CASE_ID))
      .body("testCaseResults.testCase.isPretest", hasItems(true))
      .body("testCaseResults.isPass", hasItems(true))
      .statusCode(200)
      .log()
      .all();
  }

  /** Python正常系 */
  @Test
  public void runPython1() {

    String testSuiteStr = createTestSuitePythonJson(TEST_CODE);
    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(testSuiteStr)
      .post(TEST_DATA_PATH)
      .then()
      .assertThat()
      .statusCode(204);

    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(createSubmitPythonCodeJson())
      .post(String.format("%s/%s/run/%s/", SCORING_PATH, TEST_CODE, TEST_LANG_PYTHON))
      .then()
      .body("isAllPass", equalTo(true))
      .body("testCaseResults.size()", equalTo(1))
      .body("testCaseResults.testCase.testCaseId", hasItems(TEST_CASE_ID))
      .body("testCaseResults.testCase.isPretest", hasItems(true))
      .body("testCaseResults.isPass", hasItems(true))
      .statusCode(200)
      .log()
      .all();
  }

  /** Python正常系2 */
  @Test
  public void runPython2() {

    String testSuiteStr = createTestSuitePythonJson(TEST_CODE);
    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(testSuiteStr)
      .post(TEST_DATA_PATH)
      .then()
      .assertThat()
      .statusCode(204);

    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(createSubmitPythonNgCodeJson())
      .post(String.format("%s/%s/run/%s/", SCORING_PATH, TEST_CODE, TEST_LANG_PYTHON))
      .then()
      .body("isAllPass", equalTo(false))
      .body("testCaseResults.size()", equalTo(1))
      .body("testCaseResults.testCase.testCaseId", hasItems(TEST_CASE_ID))
      .body("testCaseResults.testCase.isPretest", hasItems(true))
      .body("testCaseResults.isPass", hasItems(false))
      .statusCode(200)
      .log()
      .all();
  }
}
