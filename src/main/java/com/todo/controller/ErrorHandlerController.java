package com.todo.controller;

import com.todo.enums.ResponseStatusCode;
import com.todo.model.Response;
import com.todo.model.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandlerController {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(WebExchangeBindException.class)
  public Response<Object> handleWebExchangeBindException(WebExchangeBindException ex) {
    return Response.builder()
        .code(ResponseStatusCode.BAD_REQUEST.getCode())
        .status(ResponseStatusCode.BAD_REQUEST.getStatus().name())
        .errors(ex.getAllErrors().stream()
            .map(ObjectError::getDefaultMessage)
            .collect(Collectors.toList()))
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public Response<Object> handleConstraintViolationException(ConstraintViolationException ex) {
    return Response.builder()
        .code(HttpStatus.BAD_REQUEST.value())
        .status(HttpStatus.BAD_REQUEST.name())
        .errors(ex.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toList()))
        .build();
  }

  @ExceptionHandler({CustomException.class})
  public ResponseEntity<Response<Object>> handleCustomException(CustomException ex) {
    Response<Object> response = Response.builder()
        .code(ex.getCode())
        .status(ex.getStatus().name())
        .errors(Collections.singletonList(ex.getMessage()))
        .build();

    return new ResponseEntity<>(response, ex.getStatus());
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Throwable.class)
  public Response<Object> throwable(Throwable throwable) {
    log.error("internal server error with message", throwable);
    return Response.builder()
        .code(ResponseStatusCode.INTERNAL_SERVER_ERROR.getCode())
        .status(ResponseStatusCode.INTERNAL_SERVER_ERROR.getStatus().name())
        .build();
  }
}
