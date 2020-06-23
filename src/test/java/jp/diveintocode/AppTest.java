package jp.diveintocode;
import jp.diveintocode.controller.ScoringControllerTest;
import jp.diveintocode.controller.TestSuitesControllerTest;
import jp.diveintocode.system.SystemCallTest;
import jp.diveintocode.util.SecurityFilterJavaTest;
import jp.diveintocode.util.SecurityFilterPythonTest;
import jp.diveintocode.util.SecurityFilterRubyTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestSuitesControllerTest.class,
    ScoringControllerTest.class,
    SystemCallTest.class,
    SecurityFilterJavaTest.class,
    SecurityFilterRubyTest.class,
    SecurityFilterPythonTest.class,
    SecurityFilterPythonTest.class
})

public class AppTest {

  public void integrationTest() {}
}
