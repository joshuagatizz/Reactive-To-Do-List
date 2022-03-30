package com.todo.controller;

import com.todo.entity.Activity;
import com.todo.model.CreateRequest;
import com.todo.model.ActivityResponse;
import com.todo.model.UpdateRequest;
import com.todo.service.ToDoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
public class ToDoController {
    @Autowired
    ToDoService toDoService;

    @ApiOperation("Add new activity")
    @PostMapping(
            path = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ActivityResponse>> createActivity(@RequestBody CreateRequest request) {
        return toDoService.createActivity(request)
                .map(this::toResponse)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @ApiOperation("View all activities")
    @GetMapping(
            path = "/get-all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ActivityResponse>>> viewActivities() {
        return toDoService.getAllActivities()
                .map(list -> list.stream().map(this::toResponse).collect(Collectors.toList()))
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @ApiOperation("Update activity by id")
    @PutMapping(
            path = "/get/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Boolean>> updateActivityById(@PathVariable String id, @RequestBody UpdateRequest request) {
        return toDoService.updateActivityById(id, request)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @ApiOperation("Delete activity by id")
    @DeleteMapping(
            path = "delete/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Boolean>> deleteActivityById(@PathVariable String id) {
        return toDoService.deleteActivityById(id)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private ActivityResponse toResponse(Activity response) {
        return ActivityResponse.builder()
                .id(response.getId())
                .content(response.getContent())
                .isCompleted(response.getIsCompleted())
                .build();
    }

}
