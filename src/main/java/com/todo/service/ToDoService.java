package com.todo.service;

import com.todo.entity.Activity;
import com.todo.model.ActivityResponse;
import com.todo.model.CreateRequest;
import com.todo.model.UpdateRequest;
import com.todo.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class ToDoService {

    @Autowired
    ActivityRepository activityRepository;

    public Mono<Activity> createActivity(CreateRequest request) {
        Activity activity = Activity.builder()
                .content(request.getContent())
                .isCompleted(false)
                .build();
        return activityRepository.save(activity);
    }

    public Mono<List<Activity>> getAllActivities() {
        return activityRepository.findAll().collectList();
    }

    public Mono<Boolean> deleteActivityById(String id) {
        activityRepository.deleteById(id).subscribe();
        return Mono.just(true);
    }

    public Mono<Boolean> updateActivityById(String id, UpdateRequest request) {
        return activityRepository.findById(id)
                .doOnNext(data -> data.setContent(request.getContent()))
                .doOnNext(data -> data.setIsCompleted(request.getIsCompleted()))
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(data -> activityRepository.save(data).subscribe())
                .hasElement();
    }
}
