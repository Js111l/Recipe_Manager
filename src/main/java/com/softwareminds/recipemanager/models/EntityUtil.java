package com.softwareminds.recipemanager.models;

import com.softwareminds.recipemanager.entities.AmountEntity;
import com.softwareminds.recipemanager.entities.IngredientEntity;
import com.softwareminds.recipemanager.entities.RecipeEntity;
import com.softwareminds.recipemanager.entities.StepEntity;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

public class EntityUtil {

  public static RecipeEntity getRecipeEntity(Recipe recipe) {
    var recipeEntity = new RecipeEntity();
    recipeEntity.setTitle(recipe.name());
    recipeEntity.setShortDescription(recipe.description());
    recipeEntity.setPrepTime(Duration.of(1, ChronoUnit.HOURS));
    recipeEntity.setSteps(
        recipe.steps().stream()
            .map(x -> getStepEntity(recipeEntity, x))
            .collect(Collectors.toList()));

    setRecipeEntityAssociations(recipeEntity, recipe);
    return recipeEntity;
  }

  public static Recipe getRecipeModel(RecipeEntity recipeEntity) {
    var recipe =
        new Recipe(
            recipeEntity.getId(),
            recipeEntity.getTitle(),
            recipeEntity.getShortDescription(),
            recipeEntity.getAmountEntities().stream()
                .map(
                    x -> {
                      var ingredient =
                          new Ingredient(
                              x.getId(), x.getIngredient().getName(), x.getAmount(), x.getUnit());
                      return ingredient;
                    })
                .collect(Collectors.toList()),
            recipeEntity.getSteps().stream()
                .map(
                    x -> {
                      var step = new Step(x.getId(), x.getStepNumber(), x.getContent());
                      return step;
                    })
                .collect(Collectors.toList()));
    return recipe;
  }

  private static StepEntity getStepEntity(RecipeEntity recipeEntity, Step step) {
    var stepEntity = new StepEntity();
    stepEntity.setStepNumber(step.stepNumber());
    stepEntity.setContent(step.content());
    stepEntity.setRecipe(recipeEntity);
    return stepEntity;
  }

  private static void setRecipeEntityAssociations(RecipeEntity recipeEntity, Recipe recipe) {
    recipeEntity.setIngredients(
        recipe.ingredients().stream()
            .map(
                x -> {
                  var ingredient = getIngredientEntity(recipeEntity, x.name());
                  recipeEntity.addAmountEntity(getAmountEntity(recipeEntity, x, ingredient));
                  return ingredient;
                })
            .collect(Collectors.toList()));
  }

  private static AmountEntity getAmountEntity(
      RecipeEntity recipeEntity, Ingredient ingredient, IngredientEntity ingredientEntity) {
    var amount = new AmountEntity();
    amount.setRecipe(recipeEntity);
    amount.setIngredient(ingredientEntity);
    amount.setUnit(QuantityUnit.valueOf("GRAMS"));
    return amount;
  }

  private static IngredientEntity getIngredientEntity(RecipeEntity recipeEntity, String name) {
    var ingredient = new IngredientEntity();
    ingredient.setName(name);
    ingredient.setRecipe(recipeEntity);
    return ingredient;
  }

  public static Ingredient getIngredientModel(IngredientEntity ingredientEntity) {
    var amount =
        ingredientEntity.getRecipe().getAmountEntities().stream()
            .filter(x -> x.getIngredient().equals(ingredientEntity))
            .findFirst()
            .orElseThrow(
                () -> new RuntimeException()
                // todo
                );
    return new Ingredient(
        ingredientEntity.getId(), ingredientEntity.getName(), amount.getAmount(), amount.getUnit());
  }
}
