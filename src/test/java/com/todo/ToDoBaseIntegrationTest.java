package com.todo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.entity.Activity;
import com.todo.repository.ActivityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {ReactiveToDoListApplication.class})
@AutoConfigureWebTestClient(timeout = "30000")
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "com.todo")
@TestPropertySource({"/application-test.properties"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ToDoBaseIntegrationTest {

  @Autowired
  protected ActivityRepository repository;

  @Autowired
  protected WebTestClient webClient;

  @Autowired
  private ObjectMapper objectMapper;

  protected static final String BASE_URL = "/api/todo";
  protected static final String ID_1 = "1";
  protected static final String ID_2 = "2";
  protected static final String CONTENT_1 = "Content 1";
  protected static final String CONTENT_2 = "Content 2";
  protected static final Boolean COMPLETED_1 = true;
  protected static final Boolean COMPLETED_2 = false;
  protected static final String RANDOM_ID = "12345";

  protected String toJson(final Object obj) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }

  public <T> T fromJson(String json, TypeReference<T> typeReference) throws JsonProcessingException {
    return objectMapper.readValue(json, typeReference);
  }

  public <T> Consumer<EntityExchangeResult<byte[]>> mapAndAssert(
      TypeReference<T> typeReference, Consumer<T> assertion) {
    return entityExchangeResult -> {
      try {
        T tResponse =
            fromJson(new String(entityExchangeResult.getResponseBody()), typeReference);
        assertion.accept(tResponse);
      } catch (Exception e) {
        Assertions.fail();
      }
    };
  }
}
