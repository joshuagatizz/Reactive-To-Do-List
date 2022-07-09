package com.todo.activity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.todo.ToDoBaseIntegrationTest;
import com.todo.entity.Activity;
import com.todo.enums.ResponseStatusCode;
import com.todo.model.ActivityResponse;
import com.todo.model.CreateRequest;
import com.todo.model.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

public class CreateActivityTest extends ToDoBaseIntegrationTest {

  private CreateRequest request;

  @BeforeEach
  public void setUp() {
    request = constructRequest();
  }

  @AfterEach
  public void tearDown() {
    repository.deleteAll().block();
  }

  @Test
  public void createActivity_success() throws Exception {
    webClient.post()
        .uri(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(toJson(request)))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .consumeWith(mapAndAssert(new TypeReference<Response<ActivityResponse>>() {
        }, response -> {
          Assertions.assertEquals(ResponseStatusCode.OK.getCode(), response.getCode());
          Assertions.assertEquals(ResponseStatusCode.OK.getStatus().name(), response.getStatus());
          Assertions.assertNotNull(response.getData());
          Assertions.assertNull(response.getErrors());
        }));

    Activity result = repository.findAll().blockFirst();
    Assertions.assertNotNull(result);
    Assertions.assertEquals(request.getContent(), result.getContent());
    Assertions.assertEquals(Boolean.FALSE, result.getIsCompleted());
  }

  @Test
  public void createActivity_failed_duplicateData() throws Exception {
    webClient.post()
        .uri(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(toJson(request)))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .consumeWith(mapAndAssert(new TypeReference<Response<ActivityResponse>>() {
        }, response -> {
          Assertions.assertEquals(ResponseStatusCode.OK.getCode(), response.getCode());
          Assertions.assertEquals(ResponseStatusCode.OK.getStatus().name(), response.getStatus());
          Assertions.assertNotNull(response.getData());
          Assertions.assertNull(response.getErrors());
        }));

    webClient.post()
        .uri(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(toJson(request)))
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .consumeWith(mapAndAssert(new TypeReference<Response<ActivityResponse>>() {
        }, response -> {
          Assertions.assertEquals(ResponseStatusCode.DUPLICATE_DATA.getCode(), response.getCode());
          Assertions.assertEquals(ResponseStatusCode.DUPLICATE_DATA.getStatus().name(), response.getStatus());
          Assertions.assertNull(response.getData());
          Assertions.assertEquals(1, response.getErrors().size());
          Assertions.assertEquals(ResponseStatusCode.DUPLICATE_DATA.getMessage(), response.getErrors().get(0));
        }));

    List<Activity> result = repository.findAll().collectList().block();
    Assertions.assertNotNull(result);
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(request.getContent(), result.get(0).getContent());
    Assertions.assertEquals(Boolean.FALSE, result.get(0).getIsCompleted());
  }

  private static CreateRequest constructRequest() {
  return CreateRequest.builder()
      .content(CONTENT_1)
      .build();
  }
}
