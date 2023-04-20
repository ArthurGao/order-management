package com.a2xaccounting.exceptions;

import lombok.Getter;

public class NotFoundException extends RuntimeException {

  @Getter
  private final ErrorCode errorCode;

  public NotFoundException(ErrorCode errorCode, String message) {
    this(errorCode, message, null);
  }

  public NotFoundException(String message) {
    this(DefaultAPIErrorCodes.RESOURCE_NOT_FOUND, message, null);
  }

  public NotFoundException(String message, Throwable cause) {
    this(DefaultAPIErrorCodes.RESOURCE_NOT_FOUND, message, cause);
  }

  public NotFoundException(ErrorCode errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }
}
