package com.todo.repository;

import com.todo.entity.Activity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends ReactiveMongoRepository<Activity, String> {
}
