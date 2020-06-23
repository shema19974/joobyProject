package jp.diveintocode.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import jp.diveintocode.exception.ApiException;
import jp.diveintocode.model.ProgramLangConst;
import org.junit.Test;

public class SecurityFilterRubyTest {
  SecurityFilter securityFilter = SecurityFilterFactory.factory(ProgramLangConst.RUBY);

  @Test
  /** エラーになるケース */
  public void validateCode01() {

    // 提出コード
    List<String> list = new ArrayList<>();
    list.add("system(\"echo \'hi\'\")");
    list.add("exec(\"echo 'hi'\")");
    list.add("p `echo 'hi'`");
    list.add("%x(echo 'hi')");

    for (String code : list) {
      assertThatThrownBy(
              () -> {
                securityFilter.validate(code);
              })
          .isInstanceOf(ApiException.class);
    }
  }

  @Test
  /** エラーにならないケース */
  public void validateCode02() {
    String submissionCode =
        "def isUruYear(year)\n return false if !(year % 4).zero?\n return false if (year % 100).zero? && !(year % 400).zero?\n true\nend\n\ninput_line = gets.to_i\n\ninput_line.times do |index|\n year = gets.to_i\n puts isUruYear(year) ? \"#{year} is a leap year\" : \"#{year} is not a leap year\"\nend";
    boolean result = securityFilter.validate(submissionCode);
    assertThat(result).isTrue();
  }
}
