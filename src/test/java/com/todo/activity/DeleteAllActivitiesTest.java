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

public class DeleteAllActivitiesTest extends ToDoBaseIntegrationTest {

  @BeforeEach
  public void setUp() {
    setDbData();
  }

  @AfterEach
  public void tearDown() {
    repository.deleteAll().block();
  }

  @Test
  public void deleteAllActivities_success() {
    webClient.delete()
        .uri(BASE_URL)
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
    Assertions.assertEquals(0, result.size());
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
