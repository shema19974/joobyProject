package jp.diveintocode.util;

import jp.diveintocode.exception.ApiException;
import jp.diveintocode.exception.ApiExceptionConstant;
import jp.diveintocode.model.ProgramLangConst;

public class SecurityFilterFactory {

  public static SecurityFilter factory(ProgramLangConst programLang) throws ApiException {
    SecurityFilter securityFilter;
    switch (programLang) {
      case JAVA:
        securityFilter = new SecurityFilterJava();
        break;
      case RUBY:
        securityFilter = new SecurityFilterRuby();
        break;
      case PYTHON:
        securityFilter = new SecurityFilterPython();
        break;
      default:
        throw new ApiException(ApiExceptionConstant.ILLEGAL_ARGUMENT_EXCEPTION);
    }
    return securityFilter;
  }
}
