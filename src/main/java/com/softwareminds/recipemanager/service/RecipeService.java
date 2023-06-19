package com.softwareminds.recipemanager.service;

import com.softwareminds.recipemanager.exceptions.IngredientNotFound;
import com.softwareminds.recipemanager.models.EntityUtil;
import com.softwareminds.recipemanager.models.Ingredient;
import com.softwareminds.recipemanager.models.Recipe;
import com.softwareminds.recipemanager.repository.IngredientRepository;
import com.softwareminds.recipemanager.repository.RecipeRepository;
import com.softwareminds.recipemanager.exceptions.RecipeNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.softwareminds.recipemanager.models.EntityUtil.*;

@Service
public class RecipeService {
  private final RecipeRepository repository;
  private final IngredientRepository ingredientRepository;

  public RecipeService(RecipeRepository repository, IngredientRepository ingredientRepository) {
    this.repository = repository;
    this.ingredientRepository = ingredientRepository;
  }

  public Recipe saveRecipe(Recipe recipe) {
    var recipeEntity = this.repository.save(getRecipeEntity(recipe));
    return getRecipeModel(recipeEntity);
  }

  public List<Recipe> getAll() {
    return this.repository.findAll().stream()
        .map(EntityUtil::getRecipeModel)
        .collect(Collectors.toList());
  }

  public List<Recipe> getAll(int page, int size, String direction) {
    var recipeEntityPage =
        this.repository.findAll(
            PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), "id")));
    return recipeEntityPage.getContent().stream().map(EntityUtil::getRecipeModel).toList();
  }

  public Recipe getRecipeById(int id) {
    var recipeEntity =
        this.repository
            .findById(id)
            .orElseThrow(
                () -> new RecipeNotFoundException("Recipe with given id has not been found!"));
    return getRecipeModel(recipeEntity);
  }

  public Recipe updateRecipe(Recipe recipe) {
    var updatedRecipeEntity = getRecipeEntity(recipe);

    var recipeEntity =
        this.repository
            .findById(recipe.id())
            .orElseThrow(
                () -> new RecipeNotFoundException("Recipe with given id has not been found!"));

    recipeEntity.setShortDescription(updatedRecipeEntity.getShortDescription());
    recipeEntity.setAmountEntities(updatedRecipeEntity.getAmountEntities());
    recipeEntity.setTitle(updatedRecipeEntity.getTitle());
    recipeEntity.setSteps(updatedRecipeEntity.getSteps());
    recipeEntity.setIngredients(updatedRecipeEntity.getIngredients());
    recipeEntity.setPrepTime(updatedRecipeEntity.getPrepTime());
    this.repository.save(recipeEntity);

    return recipe;
  }

  public void deleteRecipeById(int id) {
    this.repository
        .findById(id)
        .orElseThrow(() -> new RecipeNotFoundException("Recipe with given id has not been found!"));
    this.repository.deleteById(id);
  }

  public List<Ingredient> getRecipesIngredients(int id) {
    this.repository
        .findById(id)
        .orElseThrow(() -> new RecipeNotFoundException("Recipe with given id has not been found!"));

    return this.repository.getRecipeIngredients(id).stream()
        .map(
            x ->
                getIngredientModel(
                    this.ingredientRepository
                        .findById(x)
                        .orElseThrow(
                            () -> new IngredientNotFound("No ingredient with given id found!"))))
        .toList();
  }

  public List<Recipe> getRecipesFilterByName(String name) {
    return this.repository.getRecipesFilterByName(name).stream()
        .map(EntityUtil::getRecipeModel)
        .collect(Collectors.toList());
  }
}
