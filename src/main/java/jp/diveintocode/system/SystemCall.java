package jp.diveintocode.system;

import com.github.difflib.algorithm.DiffException;
import jp.diveintocode.document.ScoringResult;
import jp.diveintocode.model.ProgramLangConst;
import jp.diveintocode.model.ScoringTarget;
import jp.diveintocode.util.SecurityFilter;
import jp.diveintocode.util.SecurityFilterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/** 提出コードの実行と採点結果出力を担当するクラス */
public class SystemCall {

  Logger logger = LoggerFactory.getLogger(SystemCall.class);
  /**
   * 実行コードを実行します。
   *
   * @param scoringTarget 実行コードとテスト入力値、期待値など一式
   * @return 採点結果
   * @throws IOException
   * @throws InterruptedException
   * @throws DiffException
   */
  public ScoringResult run(ScoringTarget scoringTarget)
      throws IOException, InterruptedException, DiffException {
    ProgramLangConst lang = scoringTarget.getProgramLangConst();
    SecurityFilter securityFilter = SecurityFilterFactory.factory(lang);
    securityFilter.validate(scoringTarget.getSubmissionCode());
    ProgramRunner programRunner = ProgramRunner.factory(lang);
    return programRunner.run(scoringTarget);
  }
}
