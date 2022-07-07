package com.todo.service.impl;

import com.todo.entity.Activity;
import com.todo.model.CreateUpdateRequest;
import com.todo.repository.ActivityRepository;
import com.todo.service.ToDoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class ToDoServiceImpl implements ToDoService {

  @Autowired
  ActivityRepository activityRepository;

  @Override
  public Mono<Activity> createActivity(CreateUpdateRequest request) {
    return activityRepository.findActivityByContent(request.getContent())
        .doOnNext(data -> {
          try {if (data.getContent().equals(request.getContent())) {throw new Exception();}
          } catch (Exception e) {e.printStackTrace();}
        })
        .switchIfEmpty(activityRepository.save(constructActivity(request)))
        .doOnError(e -> log.error("Error when #CreateActivity with request : {} and message : ", request, e));
  }

  @Override
  public Mono<List<Activity>> getAllActivities() {
    return activityRepository.findAll()
        .collectList()
        .doOnError(e -> log.error("Error when #ViewAllActivities with message : ", e));
  }

  @Override
  public Mono<List<Activity>> getActivitiesByContent(String content) {
    return activityRepository.findActivitiesByContentLike(content)
        .collectList()
        .doOnError(e -> log.error("Error when #ViewActivity by content : {} and message : ", content, e));
  }

  @Override
  public Mono<Activity> getActivityById(String id) {
    return activityRepository.findById(id)
        .doOnError(e -> log.error("Error when #ViewActivity by Id : {} and message : ", id, e));
  }

  @Override
  public Mono<Boolean> deleteActivityById(String id) {
    return activityRepository.deleteById(id)
        .thenReturn(Boolean.TRUE)
        .doOnError(e -> log.error("Error when #DeleteActivity by Id : {} and message : ", id, e));
  }

  @Override
  public Mono<Boolean> updateActivityStatus(String id) {
    return activityRepository.findById(id)
        .doOnNext(data -> {
          if (data.getIsCompleted()) data.setIsCompleted(Boolean.FALSE);
          else data.setIsCompleted(Boolean.TRUE);
        })
        .flatMap(data -> activityRepository.save(data))
        .hasElement()
        .doOnError(e -> log.error("Error when #UpdateStatus by Id : {} and message : ", id, e));
  }

  @Override
  public Mono<Boolean> updateActivityById(String id, CreateUpdateRequest request) {
    return activityRepository.findById(id)
        .doOnNext(data -> data.setContent(request.getContent()))
        .flatMap(data -> activityRepository.save(data))
        .hasElement()
        .doOnError(e -> log.error("Error when #UpdateActivity by Id : {} with request : {} and message :", id, request, e));
  }

  @Override
  public Mono<Boolean> deleteAllActivities() {
    return activityRepository.deleteAll()
        .thenReturn(Boolean.TRUE)
        .doOnError(e -> log.error("Error when #DeleteAllActivities"));
  }

  private Activity constructActivity(CreateUpdateRequest request) {
    return Activity.builder()
        .content(request.getContent())
        .isCompleted(false)
        .build();
  }
}
