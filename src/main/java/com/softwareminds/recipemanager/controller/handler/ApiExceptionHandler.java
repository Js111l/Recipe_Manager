package com.softwareminds.recipemanager.controller.handler;

import com.softwareminds.recipemanager.exceptions.RecipeNotFoundException;
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

}
