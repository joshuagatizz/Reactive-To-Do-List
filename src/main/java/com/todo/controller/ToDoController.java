package com.todo.controller;

import com.todo.entity.Activity;
import com.todo.model.CreateUpdateRequest;
import com.todo.model.ActivityResponse;
import com.todo.service.ToDoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping(path = "/api/todo", produces = MediaType.APPLICATION_JSON_VALUE)
public class ToDoController {
  @Autowired
  ToDoService toDoService;

  @ApiOperation("Add new activity")
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public Mono<ResponseEntity<ActivityResponse>> createActivity(@RequestBody CreateUpdateRequest request) {
    log.info("#CreateActivity with request : {}", request);
    return toDoService.createActivity(request)
        .map(this::toResponse)
        .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
        .subscribeOn(Schedulers.boundedElastic());
  }

  @ApiOperation("View all activities")
  @GetMapping
  public Mono<ResponseEntity<List<ActivityResponse>>> viewAllActivities() {
    log.info("#ViewAllActivities");
    return toDoService.getAllActivities()
        .map(list -> list.stream().map(this::toResponse).collect(Collectors.toList()))
        .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
        .subscribeOn(Schedulers.boundedElastic());
  }

  @ApiOperation("View activity by id")
  @GetMapping(path = "/{id}")
  public Mono<ResponseEntity<ActivityResponse>> viewActivityById(@PathVariable String id) {
    log.info("#ViewActivity by Id : {}", id);
    return toDoService.getActivityById(id)
        .map(this::toResponse)
        .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
        .subscribeOn(Schedulers.boundedElastic());
  }

  @ApiOperation("View activities by content")
  @GetMapping(path = "/find")
  public Mono<ResponseEntity<List<ActivityResponse>>> viewActivitiesByContent(@RequestParam String content) {
    log.info("#ViewActivity by Content : {}", content);
    return toDoService.getActivitiesByContent(content)
        .map(list -> list.stream().map(this::toResponse).collect(Collectors.toList()))
        .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
        .subscribeOn(Schedulers.boundedElastic());
  }

  @ApiOperation("Update activity by id")
  @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public Mono<ResponseEntity<Boolean>> updateActivityById(@PathVariable String id, @RequestBody CreateUpdateRequest request) {
    log.info("#UpdateActiviy by Id : {} with request : {}", id, request);
    return toDoService.updateActivityById(id, request)
        .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
        .subscribeOn(Schedulers.boundedElastic());
  }

  @ApiOperation("Update activity status")
  @PutMapping(path = "/{id}")
  public Mono<ResponseEntity<Boolean>> updateActivityStatus(@PathVariable String id) {
    log.info("#UpdateStatus by Id : {}", id);
    return toDoService.updateActivityStatus(id)
        .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
        .subscribeOn(Schedulers.boundedElastic());
  }

  @ApiOperation("Delete activity by id")
  @DeleteMapping(path = "/{id}")
  public Mono<ResponseEntity<Boolean>> deleteActivityById(@PathVariable String id) {
    log.info("#DeleteActivity by Id : {}", id);
    return toDoService.deleteActivityById(id)
        .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
        .subscribeOn(Schedulers.boundedElastic());
  }

  @ApiOperation("Delete all activities")
  @DeleteMapping
  public Mono<ResponseEntity<Boolean>> deleteAllActivities() {
    log.info("#DeleteAllActivities");
    return toDoService.deleteAllActivities()
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
