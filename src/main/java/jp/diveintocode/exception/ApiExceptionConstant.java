package jp.diveintocode.exception;

import com.google.gson.Gson;

public enum ApiExceptionConstant {
  COMPILE_EXCEPTION(100, "CompileException", "Compilation error has occurred."),
  ILLEGAL_ARGUMENT_EXCEPTION(
      110, "IllegalArgumentException", "IllegalArgumentException error has occurred."),
  SECURITY_EXCEPTION(120, "SecurityException", "Security violation has been detected."),
  TIMEOUT_EXCEPTION(130, "TimeoutException", "Timeout error has occurred.");

  private int code;
  private String type;
  private String message;

  ApiExceptionConstant(int code, String type, String message) {
    this.code = code;
    this.type = type;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getType() {
    return type;
  }

  public String getMessage() {
    return message;
  }

  public String toErrorJson() {
    Error error = new Error(this);
    Gson gson = new Gson();
    return gson.toJson(error);
  }

  public String toErrorJson(String message) {
    Error error = new Error(this);
    error.addMessage(message);
    Gson gson = new Gson();
    return gson.toJson(error);
  }
}
