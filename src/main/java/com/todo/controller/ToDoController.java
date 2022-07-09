package com.todo.controller;

import com.todo.model.ActivityResponse;
import com.todo.model.CreateRequest;
import com.todo.model.Response;
import com.todo.model.UpdateRequest;
import com.todo.service.ToDoService;
import com.todo.service.helper.ActivityHelper;
import com.todo.service.helper.ResponseHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.Valid;
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
  public Mono<Response<ActivityResponse>> createActivity(@RequestBody @Valid CreateRequest request) {
    log.info("#CreateActivity with request : {}", request);
    return toDoService.createActivity(request)
        .map(ActivityHelper::constructActivityResponse)
        .map(ResponseHelper::ok)
        .subscribeOn(Schedulers.boundedElastic());
  }

  @ApiOperation("View all activities")
  @GetMapping
  public Mono<Response<List<ActivityResponse>>> viewAllActivities() {
    log.info("#ViewAllActivities");
    return toDoService.getAllActivities()
        .map(list -> list.stream()
            .map(ActivityHelper::constructActivityResponse)
            .collect(Collectors.toList()))
        .map(ResponseHelper::ok)
        .subscribeOn(Schedulers.boundedElastic());
  }

  @ApiOperation("View activity by id")
  @GetMapping(path = "/{id}")
  public Mono<Response<ActivityResponse>> viewActivityById(@PathVariable String id) {
    log.info("#ViewActivity by Id : {}", id);
    return toDoService.getActivityById(id)
        .map(ActivityHelper::constructActivityResponse)
        .map(ResponseHelper::ok)
        .subscribeOn(Schedulers.boundedElastic());
  }

  @ApiOperation("Update activity by id")
  @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Response<Boolean>> updateActivityById(@PathVariable String id, @RequestBody @Valid UpdateRequest request) {
    log.info("#UpdateActivity by Id : {} with request : {}", id, request);
    return toDoService.updateActivityById(id, request)
        .map(ResponseHelper::ok)
        .subscribeOn(Schedulers.boundedElastic());
  }

  @ApiOperation("Delete activity by id")
  @DeleteMapping(path = "/{id}")
  public Mono<Response<Boolean>> deleteActivityById(@PathVariable String id) {
    log.info("#DeleteActivity by Id : {}", id);
    return toDoService.deleteActivityById(id)
        .map(ResponseHelper::ok)
        .subscribeOn(Schedulers.boundedElastic());
  }

  @ApiOperation("Delete all activities")
  @DeleteMapping
  public Mono<Response<Boolean>> deleteAllActivities() {
    log.info("#DeleteAllActivities");
    return toDoService.deleteAllActivities()
        .map(ResponseHelper::ok)
        .subscribeOn(Schedulers.boundedElastic());
  }
}
