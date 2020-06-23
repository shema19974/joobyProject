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
import jp.diveintocode.util.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 提出コードの実行と採点結果出力を担当するクラス */
public class ScriptProgramRunner extends ProgramRunner {

  @Override
  public ScoringResult run(ScoringTarget scoringTarget)
      throws IOException, InterruptedException, DiffException {
    ProgramLangConst lang = scoringTarget.getProgramLangConst();

    // 提出コードの一時ファイル生成 10桁のアルファベット + ファイル識別子
    String fileName = RandomStringUtils.randomAlphabetic(10);
    String srcFileName = fileName + lang.getSrcExtension();

    String submissionCode = scoringTarget.getSubmissionCode();
    logger.info(submissionCode);
    FileUtils.writeAll(srcFileName, submissionCode);

    List<TestCaseResult> testCaseResults = new ArrayList<>();

    // テストケースの検証
    for (TestCase testCase : scoringTarget.getTargetTestCase()) {

      // 期待値が改行で入っているので、配列に変換する
      // diffライブラリの仕様のため
      List<String> expectedList = Arrays.asList(testCase.getExpectedOutput().split("\n|\r\n|\r"));

      // 提出コードの実行
      ProcessBuilder pb = new ProcessBuilder();
      pb.command(lang.getExecCommand(), srcFileName);
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
        // diffがある場合：不合格
        printDiff(outputList, expectedList);
        List<String> errorList = getOutputList(process.getErrorStream());
        outputList.addAll(errorList);
        testCaseResults.add(new TestCaseResult(false, testCase, outputList));
      } else {
        // diffが無い場合：合格
        testCaseResults.add(new TestCaseResult(true, testCase, outputList));
      }
    }



    // 提出コードの一時ファイルを削除する
    FileUtils.delete(srcFileName);
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
