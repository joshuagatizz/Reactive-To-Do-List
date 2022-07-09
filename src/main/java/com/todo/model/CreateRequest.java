package com.todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequest {

  @NotBlank(message = "content cannot be empty")
  @Size(max = 100, message = "content max length exceeded")
  String content;

}