package com.devsuperior.dslist.errors;

public class AppError extends RuntimeException {
  private String message;
  private Integer statusCode;

  public AppError(String message, Integer statusCode) {
    this.message = message;
    this.statusCode = statusCode;
  }
  
  public AppError() {}
  
  public String getMessage() {
    return message;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }

  public Integer getStatusCode() {
    return statusCode;
  }
  
  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }
}
