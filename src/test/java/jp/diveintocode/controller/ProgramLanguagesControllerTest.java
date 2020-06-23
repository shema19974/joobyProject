package jp.diveintocode.controller;

import io.jooby.JoobyTest;
import io.restassured.http.ContentType;
import jp.diveintocode.App;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

@JoobyTest(App.class)
public class ProgramLanguagesControllerTest {

  Logger logger = LoggerFactory.getLogger(ProgramLanguagesController.class);

  final String TEST_DATA_PATH = "/programLanguages";

  /** runRuby 正常系 /scoring/:code/run/ruby */
  @Test
  public void list() {
    given()
      .log()
      .all()
      .contentType(ContentType.JSON)
      .get(TEST_DATA_PATH)
      .then()
      .assertThat()
      .statusCode(200)
      .body("size()", equalTo(3))
      .log()
      .all();
    }
}
