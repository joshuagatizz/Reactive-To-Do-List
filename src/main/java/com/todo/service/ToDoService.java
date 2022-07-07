package com.todo.service;

import com.todo.entity.Activity;
import com.todo.model.CreateUpdateRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ToDoService {
  Mono<Activity> createActivity(CreateUpdateRequest request);
  Mono<List<Activity>> getAllActivities();
  Mono<List<Activity>> getActivitiesByContent(String content);
  Mono<Activity> getActivityById(String id);
  Mono<Boolean> deleteActivityById(String id);
  Mono<Boolean> updateActivityStatus(String id);
  Mono<Boolean> updateActivityById(String id, CreateUpdateRequest request);
  Mono<Boolean> deleteAllActivities();
}
