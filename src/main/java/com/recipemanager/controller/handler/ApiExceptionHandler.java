package com.recipemanager.controller.handler;

import com.recipemanager.exceptions.RecipeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler
  public ProblemDetail recipeNotFoundHandler(RecipeNotFoundException exception) {
    var pb = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    pb.setTitle("RECIPE_NOT_FOUND");
    pb.setDetail(exception.getMessage());
    return pb;
  }

  @ExceptionHandler
  public ProblemDetail illegalArgExceptionHandler(IllegalArgumentException exception) {
    var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pb.setTitle("ILLEGAL_ARGUMENT");
    pb.setDetail(exception.getMessage());
    return pb;
  }
}
