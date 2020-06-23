package jp.diveintocode.exception;

public class ApiException extends IllegalArgumentException {
  public ApiException(ApiExceptionConstant errorEnum) {
    super(errorEnum.toErrorJson());
  }

  public ApiException(ApiExceptionConstant errorEnum, String message) {
    super(errorEnum.toErrorJson(message));
  }
}
