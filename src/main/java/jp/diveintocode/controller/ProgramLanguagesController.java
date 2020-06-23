package jp.diveintocode.controller;

import com.typesafe.config.Config;
import io.jooby.annotations.GET;
import io.jooby.annotations.Path;
import io.sentry.Sentry;
import javax.inject.Inject;
import jp.diveintocode.document.ProgramLanguage;
import jp.diveintocode.model.ProgramLangConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/** プログラム言語API */
@Path("/programLanguages")
public class ProgramLanguagesController {

  Logger logger = LoggerFactory.getLogger(ProgramLanguagesController.class);

  @Inject
  public ProgramLanguagesController(Config config) {
    Sentry.init(config.getString("dsn"));
  }

  @GET
  public List<ProgramLanguage> list() {
    return ProgramLangConst.getProgramLangList();
  }

}
