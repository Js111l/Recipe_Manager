package com.softwareminds.recipemanager.controller;

import com.softwareminds.recipemanager.models.Ingredient;
import com.softwareminds.recipemanager.models.Recipe;
import com.softwareminds.recipemanager.service.RecipeService;
import io.micrometer.observation.Observation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
  private final RecipeService recipeService;

  public RecipeController(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  @GetMapping
  @CrossOrigin
  public List<Recipe> getAllRecipes() {
    return this.recipeService.getAll();
  }

  @GetMapping(params = {"page", "size", "direction"})
  @CrossOrigin
  public List<Recipe> getAllRecipesWithPagination(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "3") int size,
      @RequestParam(defaultValue = "ASC") String direction) {
    return this.recipeService.getAll(page, size, direction);
  }

  @GetMapping("/{id}")
  @CrossOrigin
  public Recipe getRecipeById(@PathVariable int id) {
    return this.recipeService.getRecipeById(id);
  }

  @GetMapping(params = "name")
  @CrossOrigin
  public List<Recipe> getRecipesFilterByName(@RequestParam String name) {
    return this.recipeService.getRecipesFilterByName(name);
  }

  @GetMapping("/{id}/ingredients")
  @CrossOrigin
  public List<Ingredient> getRecipesIngredients(@PathVariable int id) {
    return this.recipeService.getRecipesIngredients(id);
  }

  @PutMapping
  @CrossOrigin
  public Recipe putRecipe(@RequestBody Recipe recipe) {
    return this.recipeService.updateRecipe(recipe);
  }

  @PostMapping
  @CrossOrigin
  public Recipe postRecipe(@RequestBody Recipe recipe) {
    return this.recipeService.saveRecipe(recipe);
  }

  @DeleteMapping("/{id}")
  @CrossOrigin
  public void deleteRecipe(@PathVariable int id) {
    this.recipeService.deleteRecipeById(id);
  }
}
