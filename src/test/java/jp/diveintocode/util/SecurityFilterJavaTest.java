package jp.diveintocode.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import jp.diveintocode.exception.ApiException;
import jp.diveintocode.model.ProgramLangConst;
import org.junit.Test;

public class SecurityFilterJavaTest {
  SecurityFilter securityFilter = SecurityFilterFactory.factory(ProgramLangConst.JAVA);

  @Test
  /** エラーになるケース */
  public void validateCode01() {

    // 提出コード
    List<String> list = new ArrayList<>();
    list.add("Runtime runtime = Runtime.getRuntime();");
    list.add("Process process = pb.start();");
    list.add("ProcessBuilder pb = new ProcessBuilder(\"cmd\", \"/c\", \"echo\", \"zzz\");");

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
    // 提出コード
    String submissionCode =
        "def check_sample(l):\n    if l == []:\n        return 0\n    diff_num = max(l) - min(l)\n    result = diff_num * l[-1]\n    return round(result, 1)\n\nrowNum = int(input())\n\nfor i in range(rowNum):\n    inputs = input()\n    if inputs == \"[]\":\n        inputs = []\n    else:\n        inputs = inputs.rstrip().split(\' \')\n        inputs = list(map(float, inputs))\n    print(check_sample(inputs))";
    boolean result = securityFilter.validate(submissionCode);
    assertThat(result).isTrue();
  }
}
