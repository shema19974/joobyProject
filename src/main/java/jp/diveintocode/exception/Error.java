package jp.diveintocode.exception;

public class Error {
  private int code;
  private String type;
  private String message;

  public Error(ApiExceptionConstant apiExceptionConstant) {
    this.code = apiExceptionConstant.getCode();
    this.type = apiExceptionConstant.getType();
    this.message = apiExceptionConstant.getMessage();
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

  public void addMessage(String msg) {
    message += msg;
  }
}
