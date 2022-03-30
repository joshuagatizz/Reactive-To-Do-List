package com.todo.service;

import com.todo.entity.Activity;
import com.todo.model.CreateRequest;
import com.todo.model.UpdateRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ToDoService {
    Mono<Activity> createActivity(CreateRequest request);
    Mono<List<Activity>> getAllActivities();
    Mono<Boolean> deleteActivityById(String id);
    Mono<Boolean> updateActivityById(String id, UpdateRequest request);
}
