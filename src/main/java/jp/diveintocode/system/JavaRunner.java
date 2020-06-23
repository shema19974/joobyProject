package jp.diveintocode.system;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;
import java.util.concurrent.TimeUnit;
import jp.diveintocode.document.ScoringResult;
import jp.diveintocode.document.TestCase;
import jp.diveintocode.document.TestCaseResult;
import jp.diveintocode.exception.ApiException;
import jp.diveintocode.exception.ApiExceptionConstant;
import jp.diveintocode.model.ProgramLangConst;
import jp.diveintocode.model.ScoringTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 提出コードの実行と採点結果出力を担当するクラス */
public class JavaRunner extends CompileProgramRunner {

  Logger logger = LoggerFactory.getLogger(JavaRunner.class);

  /**
   * 実行コードを実行します。
   *
   * @param scoringTarget 実行コードとテスト入力値、期待値など一式
   * @return 採点結果
   * @throws IOException
   * @throws InterruptedException
   * @throws DiffException
   */
  @Override
  public ScoringResult run(ScoringTarget scoringTarget)
          throws IOException, InterruptedException, DiffException {
    ProgramLangConst lang = scoringTarget.getProgramLangConst();

    // コンパイル
    String srcFileName = compile(scoringTarget);

    List<TestCaseResult> testCaseResults = new ArrayList<>();

    // テストケースの検証
    for (TestCase testCase : scoringTarget.getTargetTestCase()) {
      boolean isPass = false;

      // 期待値が改行で入っているので、配列に変換する
      // diffライブラリの仕様のため
      List<String> expectedList = Arrays.asList(testCase.getExpectedOutput().split("\n|\r\n|\r"));

      // 提出コードの実行
      ProcessBuilder pb = pb = new ProcessBuilder();
      pb.command(lang.getExecCommand(), srcFileName.replace(".java", ""));
      Process process = pb.start();

      // 標準入力に対して、入力値を入れる
      PrintStream out = new PrintStream(process.getOutputStream());
      out.println(testCase.getInput());
      out.flush();
      out.close();

      // 実行結果を待つ
      boolean end = process.waitFor(TIMEOUT, TimeUnit.SECONDS);
      if (!end) {
        // プロセスを強制終了
        destroyProcess(process, srcFileName);
        throw new ApiException(ApiExceptionConstant.TIMEOUT_EXCEPTION);
      }

      // 出力結果の取得
      InputStream is = process.getInputStream();
      List<String> outputList = getOutputList(is);

      // 期待値と出力値を比較する
      Patch<String> diff = DiffUtils.diff(outputList, expectedList);
      if (diff.getDeltas().size() > 0) {
        // diffがあった場合は不合格とする
        printDiff(outputList, expectedList);
      } else {
        isPass = true;
      }
      // テスト結果を配列に格納する
      testCaseResults.add(new TestCaseResult(isPass, testCase, outputList));
    }

    // 提出コードの一時ファイルを削除する
    deleteFiles(lang, srcFileName);
    boolean isAllPass = true;
    for (TestCaseResult result : testCaseResults) {
      if (!result.isPass()) {
        isAllPass = false;
        break;
      }
    }
    return new ScoringResult(isAllPass, testCaseResults);
  }
}
