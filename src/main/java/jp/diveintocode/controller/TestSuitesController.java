package jp.diveintocode.controller;

import com.typesafe.config.Config;
import io.jooby.StatusCode;
import io.jooby.annotations.*;
import io.sentry.Sentry;
import jp.diveintocode.document.TestSuite;
import jp.diveintocode.repository.TestSuitesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;

/** テストケース管理API */
@Path("/testSuites")
public class TestSuitesController {

  Logger logger = LoggerFactory.getLogger(TestSuitesController.class);

  private final TestSuitesRepository repository;

  @Inject
  public TestSuitesController(TestSuitesRepository repository, Config config) {
    this.repository = repository;
    Sentry.init(config.getString("dsn"));
  }

  /**
   * テストケースを保存します。
   *
   * @param testSuite テストケースモデル
   * @return
   */
  @POST
  public StatusCode create(TestSuite testSuite) {
    // エラーチェック
    testSuite.valid();
    repository.insertOne(testSuite);
    // To return the status 204
    return StatusCode.NO_CONTENT;
  }

  /**
   * テストケースを取得します。
   *
   * @param code 一意のテストコード
   * @return
   */
  @GET
  @Path("/{code}")
  public TestSuite find(@PathParam("code") String code) {
    TestSuite testSuite = repository.findOne(code);
    return testSuite;
  }

  /**
   * テストケースを更新します。 <br>
   * delete&insertなので部分更新はできません。
   *
   * @param testSuite テストケースモデル
   */
  @PUT
  @Path("/{code}")
  public void update(TestSuite testSuite) {

    // エラーチェック
    testSuite.valid();
    repository.updateOne(testSuite);
  }

  @DELETE
  @Path({"/{code}"})
  public void delete(@PathParam("code") String code) {
    repository.deleteOne(code);
  }
}
