package com.todo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResponseStatusCode {
  OK(HttpStatus.OK.value(), HttpStatus.OK, "Ok"),
  NOT_FOUND(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, "Activity not found"),
  BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, "Bad request"),
  DUPLICATE_DATA(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, "Duplicate data"),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

  private int code;
  private HttpStatus status;
  private String message;
}
