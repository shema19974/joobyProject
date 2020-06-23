package jp.diveintocode;


import io.jooby.OpenAPIModule;
import io.jooby.di.GuiceModule;
import io.jooby.json.GsonModule;
import jp.diveintocode.controller.ProgramLanguagesController;
import jp.diveintocode.controller.ScoringController;
import jp.diveintocode.controller.TestSuitesController;
import io.jooby.Jooby;

public class App extends Jooby {
  {
    install(new OpenAPIModule().swaggerUI("/swagger"));
    install(new GsonModule());
    install(new GuiceModule());
    mvc(ProgramLanguagesController.class);
    mvc(TestSuitesController.class);
    mvc(ScoringController.class);
  }

  public static void main(String[] args) {
    runApp(args, App::new);
  }
}
