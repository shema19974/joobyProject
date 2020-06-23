package jp.diveintocode.model;

import jp.diveintocode.document.ProgramLanguage;

import java.util.ArrayList;
import java.util.List;

/** プログラミング言語の定数クラスです。 */
public enum ProgramLangConst {
  RUBY("Ruby", "ruby", ".rb"),
  PYTHON("Python", "python", ".py"),
  JAVA("Java", "java", ".java");

  private String exeCmd;
  private String srcExtension;
  private String dispName;

  ProgramLangConst(String dispName, String exeCmd, String srcExtension) {
    this.dispName = dispName;
    this.exeCmd = exeCmd;
    this.srcExtension = srcExtension;
  }

  /**
   * 実行コマンドを取得します。
   *
   * @return
   */
  public String getExecCommand() {
    return this.exeCmd;
  }

  public String compileCmd() {
    if (!isCompilerLanguage()) {
      throw new RuntimeException(this.toString() + "はコンパイル言語ではありません");
    }
    if (this == JAVA) {
      return "javac";
    }
    return null;
  }

  /**
   * ソースファイルの拡張子を取得します。
   *
   * @return
   */
  public String getSrcExtension() {
    return this.srcExtension;
  }

  /**
   * コンパイル言語かどうか？ <br>
   * ソースファイルとバイナリファイルの拡張子が異なる場合は<br>
   * コンパイル言語と判定します。
   *
   * @return if true then コンパイル言語 else スクリプト言語.
   */
  public boolean isCompilerLanguage() {
    if (this == JAVA) {
      return true;
    }
    return false;
  }

  /**
   * 実行コマンドからEnumオブジェクトを取得します。
   *
   * @param exeCmd 実行コマンド
   * @return
   */
  public static ProgramLangConst getEnum(String exeCmd) {
    for (ProgramLangConst programLangConst : ProgramLangConst.values()) {
      if (!programLangConst.toString().equalsIgnoreCase(exeCmd)) {
        continue;
      }
      return programLangConst;
    }
    return null;
  }

  /**
   * 表示名称を取得します。
   *
   * @return
   */
  public String getDispName() {
    return dispName;
  }

  /**
   * プログラミング言語のリストを取得します。
   *
   * @return
   */
  public static List<ProgramLanguage> getProgramLangList() {
    List<ProgramLanguage> programLangList = new ArrayList<>();

    ProgramLangConst[] programLangConsts = ProgramLangConst.values();
    int length = programLangConsts.length;
    for (int i = 0; i < length; i++) {
      programLangList.add(new ProgramLanguage(programLangConsts[i]));
    }

    return programLangList;
  }
}
