package com.todo.activity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.todo.ToDoBaseIntegrationTest;
import com.todo.entity.Activity;
import com.todo.enums.ResponseStatusCode;
import com.todo.model.ActivityResponse;
import com.todo.model.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ViewAllActivitiesTest extends ToDoBaseIntegrationTest {

  @BeforeEach
  public void setUp() {
    setDbData();
  }

  @AfterEach
  public void tearDown() {
    repository.deleteAll().block();
  }

  @Test
  public void viewAllActivities_success() {
    webClient.get()
        .uri(BASE_URL)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .consumeWith(mapAndAssert(new TypeReference<Response<List<ActivityResponse>>>() {
        }, response -> {
          Assertions.assertEquals(ResponseStatusCode.OK.getCode(), response.getCode());
          Assertions.assertEquals(ResponseStatusCode.OK.getStatus().name(), response.getStatus());
          Assertions.assertNotNull(response.getData());
          Assertions.assertEquals(2, response.getData().size());
          Assertions.assertEquals(ID_1, response.getData().get(0).getId());
          Assertions.assertEquals(CONTENT_1, response.getData().get(0).getContent());
          Assertions.assertEquals(COMPLETED_1, response.getData().get(0).getIsCompleted());
          Assertions.assertEquals(ID_2, response.getData().get(1).getId());
          Assertions.assertEquals(CONTENT_2, response.getData().get(1).getContent());
          Assertions.assertEquals(COMPLETED_2, response.getData().get(1).getIsCompleted());
          Assertions.assertNull(response.getErrors());
        }));
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
