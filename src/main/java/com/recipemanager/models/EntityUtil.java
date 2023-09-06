package com.recipemanager.models;

import com.recipemanager.entities.AmountEntity;
import com.recipemanager.entities.IngredientEntity;
import com.recipemanager.entities.RecipeEntity;
import com.recipemanager.entities.StepEntity;

import java.util.stream.Collectors;

public class EntityUtil {

  public static RecipeEntity getRecipeEntity(Recipe recipe) {
    var recipeEntity = new RecipeEntity();
    recipeEntity.setTitle(recipe.name());
    recipeEntity.setShortDescription(recipe.description());
    recipeEntity.setPrepTime(recipe.prepTime());
    recipeEntity.setSteps(
        recipe.steps().stream()
            .map(x -> getStepEntity(recipeEntity, x))
            .collect(Collectors.toList()));

    setRecipeEntityAssociations(recipeEntity, recipe);
    return recipeEntity;
  }

  public static Recipe getRecipeModel(RecipeEntity recipeEntity) {
    return new Recipe(
        recipeEntity.getId(),
        recipeEntity.getTitle(),
        recipeEntity.getPrepTime(),
        recipeEntity.getShortDescription(),
        recipeEntity.getAmountEntities().stream()
            .map(
                x ->
                    new Ingredient(
                        x.getId(), x.getIngredient().getName(), x.getAmount(), x.getUnit()))
            .collect(Collectors.toList()),
        recipeEntity.getSteps().stream()
            .map(x -> new Step(x.getId(), x.getStepNumber(), x.getContent()))
            .collect(Collectors.toList()));
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
                ingredient -> {
                  var ingredientEntity = getIngredientEntity(recipeEntity, ingredient.name());
                  recipeEntity.addAmountEntity(
                      getAmountEntity(recipeEntity, ingredientEntity, ingredient));
                  return ingredientEntity;
                })
            .collect(Collectors.toList()));
  }

  private static AmountEntity getAmountEntity(
      RecipeEntity recipeEntity, IngredientEntity ingredientEntity, Ingredient ingredient) {
    var amount = new AmountEntity();
    amount.setRecipe(recipeEntity);
    amount.setIngredient(ingredientEntity);
    amount.setUnit(ingredient.unit());
    amount.setAmount(ingredient.amount());
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
                RuntimeException::new
                // todo
                );
    return new Ingredient(
        ingredientEntity.getId(), ingredientEntity.getName(), amount.getAmount(), amount.getUnit());
  }
}
