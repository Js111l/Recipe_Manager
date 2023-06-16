package com.softwareminds.recipemanager.controller;

import com.softwareminds.recipemanager.models.Ingredient;
import com.softwareminds.recipemanager.models.Recipe;
import com.softwareminds.recipemanager.service.RecipeService;
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

  @GetMapping("/{id}")
  @CrossOrigin
  public Recipe getRecipeById(@PathVariable int id) {
    return this.recipeService.getRecipeById(id);
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
