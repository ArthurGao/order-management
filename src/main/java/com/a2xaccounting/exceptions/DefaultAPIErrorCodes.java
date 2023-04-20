package com.a2xaccounting.exceptions;

public interface DefaultAPIErrorCodes {

  ErrorCode BAD_REQUEST = new ErrorCode(400, "One or more of the arguments are invalid.");
  ErrorCode INTERNAL_ERROR = new ErrorCode(500, "Could not process the request. Please try again later.");
  ErrorCode AUTHENTICATION_FAILED = new ErrorCode(401, "Authentication failed");
  ErrorCode RESOURCE_NOT_FOUND = new ErrorCode(404, "Resource not found");
  ErrorCode NOT_AUTHORIZED = new ErrorCode(403, "Not authorized to perform the operation");
  ErrorCode FORBIDDEN_NO_PERMISSION = new ErrorCode(403,
      "Not authorized to access the requested resource(s)");
}