package com.todo.repository;

import com.todo.entity.Activity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface ActivityRepository extends ReactiveMongoRepository<Activity, String> {
  Mono<Activity> findActivityByContentEquals(String Id);
  Flux<Activity> findActivitiesByContentLike(String content);
}
