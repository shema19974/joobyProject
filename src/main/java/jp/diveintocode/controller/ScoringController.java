package jp.diveintocode.controller;

import com.github.difflib.algorithm.DiffException;
import io.jooby.annotations.POST;
import io.jooby.annotations.Path;
import io.jooby.annotations.PathParam;
import jp.diveintocode.document.Scoring;
import jp.diveintocode.document.ScoringResult;
import jp.diveintocode.document.TestSuite;
import jp.diveintocode.exception.ApiException;
import jp.diveintocode.exception.ApiExceptionConstant;
import jp.diveintocode.model.ScoringTarget;
import jp.diveintocode.repository.TestSuitesRepository;
import jp.diveintocode.system.SystemCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

/** 採点用API */
@Path("/scoring")
public class ScoringController {

  Logger logger = LoggerFactory.getLogger(ScoringController.class);

  private final TestSuitesRepository repository;

  @Inject
  public ScoringController(TestSuitesRepository repository) {
    this.repository = repository;
  }

  /**
   * 採点を実行します。
   *
   * @param code 一意なテストコード
   * @param lang 実行言語(ruby, python3, java)
   * @param scoring Scoringモデル
   * @return 採点結果
   * @throws InterruptedException
   * @throws DiffException
   * @throws IOException
   */
  @POST
  @Path("/{code}/run/{lang}/")
  public ScoringResult run(
          @PathParam("code") String code, @PathParam("lang") String lang, Scoring scoring)
      throws InterruptedException, DiffException, IOException {
    TestSuite testSuite = repository.findOne(code);
    if (testSuite.getTestCases().isEmpty()) {
      throw new ApiException(
          ApiExceptionConstant.ILLEGAL_ARGUMENT_EXCEPTION, "TestCases is empty.");
    }
    scoring.valid();
    ScoringTarget scoringTarget = new ScoringTarget(testSuite, scoring, lang);
    if (scoringTarget.getTargetTestCase().isEmpty()) {
      throw new ApiException(
          ApiExceptionConstant.ILLEGAL_ARGUMENT_EXCEPTION, "TargetTestCase is empty.");
    }
    SystemCall systemCall = new SystemCall();
    return systemCall.run(scoringTarget);
  }
}
