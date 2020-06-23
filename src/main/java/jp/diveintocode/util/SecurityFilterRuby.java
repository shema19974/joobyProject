package jp.diveintocode.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jp.diveintocode.exception.ApiException;
import jp.diveintocode.exception.ApiExceptionConstant;

public class SecurityFilterRuby implements SecurityFilter {

  /**
   * 提出されたコードにセキュリティ的に問題があるコードが含まれていないかチェックします。 <br>
   * セキュリティ違反があった場合は、ApiExceptionを発生させます。
   *
   * @param submissionCode 提出コード
   * @return
   * @throws ApiException
   */
  public boolean validate(String submissionCode) throws ApiException {
    Pattern pattern = Pattern.compile("(system|exec|\\%x|\\`|http|File)");
    Matcher matcher = pattern.matcher(submissionCode);
    if (matcher.find()) {
      throw new ApiException(ApiExceptionConstant.SECURITY_EXCEPTION);
    }
    return true;
  }
}
