package com.todo.controller;

import com.todo.model.Response;
import com.todo.model.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandlerController {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Response<Object>> handleHibernateException(MethodArgumentNotValidException ex) {
    Response<Object> response = Response.builder()
        .code(HttpStatus.BAD_REQUEST.value())
        .status(HttpStatus.BAD_REQUEST.name())
        .errors(ex.getBindingResult().getAllErrors().stream()
            .map(ObjectError::getDefaultMessage)
            .collect(Collectors.toList()))
        .build();
    return ResponseEntity.badRequest().body(response);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({CustomException.class})
  public Response<Object> handleCustomException(CustomException ex) {
    return Response.builder()
        .code(ex.getCode())
        .status(ex.getStatus().name())
        .errors(Collections.singletonList(ex.getMessage()))
        .build();
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Throwable.class)
  public Response<Object> throwable(Throwable throwable) {
    log.error("internal server error with error", throwable);
    return Response.builder()
        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
        .build();
  }
}
