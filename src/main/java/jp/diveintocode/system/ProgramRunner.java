package jp.diveintocode.system;

import com.github.difflib.algorithm.DiffException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import jp.diveintocode.document.ScoringResult;
import jp.diveintocode.exception.ApiException;
import jp.diveintocode.exception.ApiExceptionConstant;
import jp.diveintocode.model.ProgramLangConst;
import jp.diveintocode.model.ScoringTarget;
import jp.diveintocode.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/** 提出コードの実行と採点結果出力を担当するクラス */
public abstract class ProgramRunner {

  public static int TIMEOUT = 2;

  Logger logger = LoggerFactory.getLogger(ProgramRunner.class);

  public static ProgramRunner factory(ProgramLangConst lang) {
    switch (lang) {
      case JAVA:
        return new JavaRunner();
      default:
        return new ScriptProgramRunner();
    }
  }
  /**
   * 実行コードを実行します。
   *
   * @param scoringTarget 実行コードとテスト入力値、期待値など一式
   * @return 採点結果
   * @throws IOException
   * @throws InterruptedException
   * @throws DiffException
   */
  public abstract ScoringResult run(ScoringTarget scoringTarget)
      throws IOException, InterruptedException, DiffException;

  /**
   * 提出コードの標準出力を配列で取得する
   *
   * @param is
   * @return
   * @throws IOException
   */
  protected List<String> getOutputList(InputStream is) throws IOException {
    List<String> result = new ArrayList<>();
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    try {
      while (true) {
        String line = br.readLine();
        if (line == null) break;
        result.add(line);
      }
    } finally {
      br.close();
    }
    return result;
  }

  /**
   * 提出コードの出力と期待値のdiffを出力する
   *
   * @param original
   * @param expected
   * @throws DiffException
   */
  protected void printDiff(List<String> original, List<String> expected) throws DiffException {
    DiffRowGenerator generator =
        DiffRowGenerator.create()
            .showInlineDiffs(true)
            .inlineDiffByWord(true)
            .oldTag(f -> "~")
            .newTag(f -> "**")
            .build();
    List<DiffRow> rows = generator.generateDiffRows(original, expected);

    System.out.println("|original|expected|");
    System.out.println("|---|---|");
    for (DiffRow row : rows) {
      System.out.println("|" + row.getOldLine() + "|" + row.getNewLine() + "|");
    }
  }

  protected void deleteFiles(ProgramLangConst programLangConst, String srcFileName) {
    FileUtils.delete(srcFileName);
    if (programLangConst == ProgramLangConst.JAVA) {
      FileUtils.delete(srcFileName.replace(".java", ".class"));
    }
  }

  protected void destroyProcess(Process process, String srcFileName) {
    // プロセスを強制終了
    process.destroy();
    FileUtils.delete(srcFileName);
  }
}
