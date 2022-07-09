package com.todo.activity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.todo.ToDoBaseIntegrationTest;
import com.todo.entity.Activity;
import com.todo.enums.ResponseStatusCode;
import com.todo.model.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DeleteActivityByIdTest extends ToDoBaseIntegrationTest {

  @BeforeEach
  public void setUp() {
    setDbData();
  }

  @AfterEach
  public void tearDown() {
    repository.deleteAll().block();
  }

  @Test
  public void deleteActivityById_success() {
    webClient.delete()
        .uri(BASE_URL + "/" + ID_1)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .consumeWith(mapAndAssert(new TypeReference<Response<Boolean>>() {
        }, response -> {
          Assertions.assertEquals(ResponseStatusCode.OK.getCode(), response.getCode());
          Assertions.assertEquals(ResponseStatusCode.OK.getStatus().name(), response.getStatus());
          Assertions.assertTrue(response.getData());
          Assertions.assertNull(response.getErrors());
        }));

    List<Activity> result = repository.findAll().collectList().block();
    Assertions.assertNotNull(result);
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(ID_2, result.get(0).getId());
    Assertions.assertEquals(CONTENT_2, result.get(0).getContent());
    Assertions.assertEquals(COMPLETED_2, result.get(0).getIsCompleted());
  }

  @Test
  public void deleteActivityById_failed_idNotFound() {
    webClient.delete()
        .uri(BASE_URL + "/" + RANDOM_ID)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody()
        .consumeWith(mapAndAssert(new TypeReference<Response<Boolean>>() {
        }, response -> {
          Assertions.assertEquals(ResponseStatusCode.NOT_FOUND.getCode(), response.getCode());
          Assertions.assertEquals(ResponseStatusCode.NOT_FOUND.getStatus().name(), response.getStatus());
          Assertions.assertNull(response.getData());
          Assertions.assertEquals(1, response.getErrors().size());
          Assertions.assertEquals(ResponseStatusCode.NOT_FOUND.getMessage(), response.getErrors().get(0));
        }));

    List<Activity> result = repository.findAll().collectList().block();
    Assertions.assertNotNull(result);
    Assertions.assertEquals(2, result.size());
  }

  private void setDbData() {
    repository.save(
        Activity.builder()
            .id(ID_1)
            .content(CONTENT_1)
            .isCompleted(COMPLETED_1)
            .build()
    ).block();
    repository.save(
        Activity.builder()
            .id(ID_2)
            .content(CONTENT_2)
            .isCompleted(COMPLETED_2)
            .build()
    ).block();
  }
}
