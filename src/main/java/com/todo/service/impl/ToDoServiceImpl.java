package com.todo.service.impl;

import com.todo.entity.Activity;
import com.todo.model.CreateRequest;
import com.todo.model.UpdateRequest;
import com.todo.repository.ActivityRepository;
import com.todo.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Objects;

@Service
public class ToDoServiceImpl implements ToDoService {

    @Autowired
    ActivityRepository activityRepository;

    @Override
    public Mono<Activity> createActivity(CreateRequest request) {
        return activityRepository.findActivityByContent(request.getContent())
                .doOnNext(data -> {
                    try {
                        if (data.getContent().equals(request.getContent())) {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .switchIfEmpty(activityRepository.save(constructActivity(request)));
    }

    @Override
    public Mono<List<Activity>> getAllActivities() {
        return activityRepository.findAll().collectList();
    }

    @Override
    public Mono<Boolean> deleteActivityById(String id) {
        return activityRepository.deleteById(id)
                .thenReturn(Boolean.TRUE);
    }

    @Override
    public Mono<Boolean> updateActivityById(String id, UpdateRequest request) {
        return activityRepository.findById(id)
                .doOnNext(data -> {
                    data.setContent(request.getContent());
                    data.setIsCompleted(request.getIsCompleted());
                })
                .flatMap(data -> activityRepository.save(data))
                .hasElement();
    }

    private Activity constructActivity(CreateRequest request) {
        return Activity.builder()
                .content(request.getContent())
                .isCompleted(false)
                .build();
    }
}
