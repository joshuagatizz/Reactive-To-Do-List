package com.todo.service.impl;

import com.todo.entity.Activity;
import com.todo.enums.ResponseStatusCode;
import com.todo.model.CreateRequest;
import com.todo.model.UpdateRequest;
import com.todo.model.exception.CustomException;
import com.todo.repository.ActivityRepository;
import com.todo.service.ToDoService;
import com.todo.service.helper.ActivityHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class ToDoServiceImpl implements ToDoService {

  @Autowired
  ActivityRepository activityRepository;

  @Override
  public Mono<Activity> createActivity(CreateRequest request) {
    return activityRepository.findActivityByContentEquals(request.getContent())
        .doOnNext(data -> {
          if (!ObjectUtils.isEmpty(data))
            throw new CustomException(ResponseStatusCode.DUPLICATE_DATA);
        })
        .switchIfEmpty(activityRepository.save(ActivityHelper.constructActivity(request)))
        .doOnError(e -> log.error("Error when #CreateActivity with request : {} and message : ", request, e));
  }

  @Override
  public Mono<List<Activity>> getAllActivities() {
    return activityRepository.findAll()
        .collectList()
        .doOnError(e -> log.error("Error when #ViewAllActivities with message : ", e));
  }

  @Override
  public Mono<Activity> getActivityById(String id) {
    return activityRepository.findById(id)
        .switchIfEmpty(Mono.error(new CustomException(ResponseStatusCode.NOT_FOUND)))
        .doOnError(e -> log.error("Error when #ViewActivity by Id : {} and message : ", id, e));
  }

  @Override
  public Mono<Boolean> deleteActivityById(String id) {
    return activityRepository.findById(id)
        .switchIfEmpty(Mono.error(new CustomException(ResponseStatusCode.NOT_FOUND)))
        .flatMap(data -> activityRepository.deleteById(data.getId()))
        .thenReturn(Boolean.TRUE)
        .doOnError(e -> log.error("Error when #DeleteActivity by Id : {} and message : ", id, e));
  }

  @Override
  public Mono<Boolean> updateActivityById(String id, UpdateRequest request) {
    return activityRepository.findById(id)
        .switchIfEmpty(Mono.error(new CustomException(ResponseStatusCode.NOT_FOUND)))
        .doOnNext(data -> data.setContent(request.getContent()))
        .doOnNext(data -> data.setIsCompleted(request.getIsCompleted()))
        .flatMap(activityRepository::save)
        .hasElement()
        .doOnError(e -> log.error("Error when #UpdateActivity by Id : {} with request : {} and message :", id, request, e));
  }

  @Override
  public Mono<Boolean> deleteAllActivities() {
    return activityRepository.deleteAll()
        .thenReturn(Boolean.TRUE)
        .doOnError(e -> log.error("Error when #DeleteAllActivities"));
  }
}
