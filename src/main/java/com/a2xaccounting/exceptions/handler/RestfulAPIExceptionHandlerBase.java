package com.a2xaccounting.exceptions.handler;

import com.a2xaccounting.exceptions.*;
import com.a2xaccounting.ordermanagement.model.ErrorResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintDefinitionException;
import javax.validation.ConstraintViolationException;

@Log4j2
@ControllerAdvice
public class RestfulAPIExceptionHandlerBase extends ResponseEntityExceptionHandler {

  public ErrorResponseDto createErrorResponse(ErrorCode apiErrorCode) {
    ErrorResponseDto errorResponse = new ErrorResponseDto();
    errorResponse.setErrorCode(apiErrorCode.getErrorCode());
    errorResponse.setErrorMessage(apiErrorCode.getMessage());
    return errorResponse;
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    log.warn("Failed to handle request {} [status: {}] due to {}.", request.getContextPath(),
        status, ex.getMessage(), ex);
    if (ex instanceof NoHandlerFoundException) {
      return ResponseEntity.notFound()
          .build();
    }
    return ResponseEntity.internalServerError()
        .body(createErrorResponse(DefaultAPIErrorCodes.INTERNAL_ERROR));
  }

  @ExceptionHandler({ConstraintDefinitionException.class, MethodArgumentTypeMismatchException.class,
          ConstraintViolationException.class, IllegalArgumentException.class})
  private ResponseEntity handleInvalidArgument(
      RuntimeException ex, WebRequest request) {
    log.warn("Bad request for request {} due to {}.", request.getContextPath(),
        ex.getMessage(), ex);
    return ResponseEntity.badRequest()
        .body(createErrorResponse(DefaultAPIErrorCodes.BAD_REQUEST));
  }

  @ExceptionHandler(BadRequestException.class)
  private ResponseEntity handleBadRequestRuntimeException(
      BadRequestException ex, WebRequest request) {
    log.warn("Bad request exception for request {} due to {}.", request.getContextPath(),
        ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(createErrorResponse(ex.getErrorCode()));
  }

  @ExceptionHandler(NotFoundException.class)
  private ResponseEntity handlNotFoundRuntimeException(
          NotFoundException ex, WebRequest request) {
    log.warn("Not found content exception for request {} due to {}.", request.getContextPath(),
            ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(createErrorResponse(ex.getErrorCode()));
  }

  @ExceptionHandler(ServerInternalException.class)
  public ResponseEntity handleServerInternalException(ServerInternalException ex,
      WebRequest request) {
    log.warn("Internal error for request {} due to {}.", request.getContextPath(),
        ex.getMessage(), ex);
    return ResponseEntity.internalServerError()
        .body(createErrorResponse(ex.getErrorCode()));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity handleRuntimeException(RuntimeException ex,
      WebRequest request) {
    log.warn("Internal error for request {} due to {}.", request.getContextPath(),
        ex.getMessage(), ex);
    return ResponseEntity.internalServerError()
        .body(createErrorResponse(DefaultAPIErrorCodes.INTERNAL_ERROR));
  }
}
