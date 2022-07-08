package com.todo.service.helper;

import com.todo.entity.Activity;
import com.todo.model.ActivityResponse;
import com.todo.model.CreateRequest;

public class ActivityHelper {
  public static Activity constructActivity(CreateRequest request) {
    return Activity.builder()
        .content(request.getContent())
        .isCompleted(false)
        .build();
  }

  public static ActivityResponse constructActivityResponse(Activity response) {
    return ActivityResponse.builder()
        .id(response.getId())
        .content(response.getContent())
        .isCompleted(response.getIsCompleted())
        .build();
  }
}
