package com.recipemanager.exceptions;

public class IngredientNotFound extends RuntimeException {
  public IngredientNotFound(String message) {
    super(message);
  }
}
