package jp.diveintocode.util;

import jp.diveintocode.exception.ApiException;

public interface SecurityFilter {

  /**
   * 提出されたコードにセキュリティ的に問題があるコードが含まれていないかチェックします。<br>
   * セキュリティ違反があった場合は、ApiExceptionを発生させます。
   *
   * @param submissionCode 提出コード
   * @return
   * @throws ApiException
   */
  public boolean validate(String submissionCode) throws ApiException;
}
