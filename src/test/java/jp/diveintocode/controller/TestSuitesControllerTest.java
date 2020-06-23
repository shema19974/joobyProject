package jp.diveintocode.controller;

import com.google.gson.Gson;
import io.jooby.JoobyTest;
import io.restassured.http.ContentType;
import jp.diveintocode.App;
import jp.diveintocode.document.TestCase;
import jp.diveintocode.document.TestSuite;
import jp.diveintocode.repository.TestSuitesRepository;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

@JoobyTest(App.class)
public class TestSuitesControllerTest {

  Logger logger = LoggerFactory.getLogger(TestSuitesControllerTest.class);


  final String RESOURCE_PATH = "/testSuites";

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
    testCase.setTestCaseId(1);
    testCase.setInput("1000");
    testCase.setExpectedOutput(
        "1000 is not a leap year");
    testCaseList.add(testCase);
    TestSuite testSuite = new TestSuite();
    testSuite.setCode(code);
    testSuite.setTestCases(testCaseList);
    Gson gson = new Gson();
    return gson.toJson(testSuite);

  }

  /** postTestSuitesメソッド 正常系 */
 @Test
  public void postTestSuitesNormal1() {
    String testSuiteStr = createTestSuiteJson("testCode");
    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(testSuiteStr)
      .post(RESOURCE_PATH)
      .then()
      .assertThat()
      .statusCode(204);
  }

  /** deleteTestSuitesメソッド 正常系 */
  @Test
  public void deleteTestSuitesNormal1() {
    String testSuiteStr = createTestSuiteJson("testCode");
    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(testSuiteStr)
      .post(RESOURCE_PATH)
      .then()
      .assertThat()
      .statusCode(204);

    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .delete(RESOURCE_PATH + "/testCode")
      .then()
      .assertThat()
      .statusCode(204);

    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .get(RESOURCE_PATH + "/testCode")
      .then()
      .assertThat()
      .statusCode(400);
  }

  /** updateTestSuitesメソッド 正常系 */
  @Test
  public void updateTestSuitesNormal1() {
    String testSuiteStr = createTestSuiteJson("testCode");
    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(testSuiteStr)
      .post(RESOURCE_PATH)
      .then()
      .assertThat()
      .statusCode(204);
  }

  /** postTestSuitesメソッド 異常系1 codeが空の場合、エラーになること。 */
  @Test
  public void postTestSuitesException1() {

    String testSuiteStr = createTestSuiteJson("");

    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(testSuiteStr)
      .post(RESOURCE_PATH)
      .then()
      .assertThat()
      .statusCode(400);
  }

  /** postTestSuitesメソッド 異常系2 codeが重複した場合、エラーになること。 */
  @Test
  public void postTestSuitesException2() {
    String testSuiteStr = createTestSuiteJson("testCode");
    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(testSuiteStr)
      .post(RESOURCE_PATH)
      .then()
      .assertThat()
      .statusCode(204);
    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(testSuiteStr)
      .post(RESOURCE_PATH)
      .then()
      .assertThat()
      .statusCode(500);
  }

  /** findTestSuitesメソッド 正常系 */
  @Test
  public void findTestSuitesNormal1() {
    String testSuiteStr = createTestSuiteJson("testCode");

    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .body(testSuiteStr)
      .post(RESOURCE_PATH)
      .then()
      .assertThat()
      .statusCode(204);

    String path = RESOURCE_PATH + "/testCode";
    given().log().all().get(path).then().assertThat().statusCode(200);
    String json = given().get(path).asString();
    Gson gson = new Gson();
    TestSuite testSuite = gson.fromJson(json, TestSuite.class);
    assertThat(testSuite.getCode()).isEqualTo("testCode");
  }
}
