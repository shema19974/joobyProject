package jp.diveintocode.system;


import jp.diveintocode.exception.ApiException;
import jp.diveintocode.exception.ApiExceptionConstant;
import jp.diveintocode.model.ProgramLangConst;
import jp.diveintocode.model.ScoringTarget;
import jp.diveintocode.util.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/** 提出コードの実行と採点結果出力を担当するクラス */
public abstract class CompileProgramRunner extends ProgramRunner {
  /**
   * コンパイルします
   * @param scoringTarget
   * @return ソースファイル名 ex. main.java
   * @throws IOException
   * @throws InterruptedException
   */
  protected String compile(ScoringTarget scoringTarget) throws IOException, InterruptedException {
    ProgramLangConst lang = scoringTarget.getProgramLangConst();
    String fileName = RandomStringUtils.randomAlphabetic(10);
    String srcFileName = fileName + lang.getSrcExtension();
    String submissionCode = scoringTarget.getSubmissionCode();
    if (lang == ProgramLangConst.JAVA) {
      // Javaの場合
      submissionCode = scoringTarget.getSubmissionCode().replaceAll("Main", fileName);
    }
    FileUtils.writeAll(srcFileName, submissionCode);

    // コンパイル
    ProcessBuilder pb = new ProcessBuilder();
    pb.command(lang.compileCmd(), srcFileName);
    Process process = pb.start();
    if (process.waitFor() > 0) {
      // コンパイルエラーの場合

      // 提出コードの一時ファイルを削除する
      deleteFiles(lang, fileName);

      // エラー出力
      InputStream is = process.getErrorStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      StringBuilder builder = new StringBuilder();
      try {
        while (true) {
          String line = br.readLine();
          if (line == null) break;
          builder.append(line);
        }
      } finally {
        br.close();
      }
      logger.error(builder.toString());
      throw new ApiException(ApiExceptionConstant.COMPILE_EXCEPTION);
    }
    return srcFileName;
  }
}
