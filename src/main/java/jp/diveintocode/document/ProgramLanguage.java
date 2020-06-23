package jp.diveintocode.document;

import jp.diveintocode.model.ProgramLangConst;

public class ProgramLanguage {
  private String code;
  private String displayName;

  public ProgramLanguage(ProgramLangConst langConst) {
    this.displayName = langConst.getDispName();
    this.code = langConst.getExecCommand();
  }

  public String getCode() {
    return code;
  }

  public String getDisplayName() {
    return displayName;
  }
}
