package com.todo.service.helper;

import com.todo.model.Response;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ResponseHelper {

  public static <T> Response<T> ok(T data) {
    return status(HttpStatus.OK, data);
  }

  public static <T> Response<T> badRequest(List<String> errors) {
    return status(HttpStatus.BAD_REQUEST, null, errors);
  }

  public <T> Response<T> notFound(T data) {
    return status(HttpStatus.NOT_FOUND, data);
  }

  public <T> Response<T> internalServerError() {
    return status(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public static <T> Response<T> status(HttpStatus status) {
    return status(status, null);
  }

  public static <T> Response<T> status(HttpStatus status, T data) {
    return status(status, data, null);
  }

  public static <T> Response<T> status(HttpStatus status, T data, List<String> errors) {
    return Response.<T>builder().code(status.value()).status(status.name()).data(data).errors(errors).build();
  }
}
