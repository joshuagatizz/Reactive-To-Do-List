package com.todo.model.exception;

import com.todo.enums.ResponseStatusCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class CustomException extends RuntimeException {

  private Integer code;
  private HttpStatus status;
  private String message;

  public CustomException(ResponseStatusCode responseStatusCode) {
    this.code = responseStatusCode.getCode();
    this.status = responseStatusCode.getStatus();
    this.message = responseStatusCode.getMessage();
  }
}
