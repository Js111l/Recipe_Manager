package com.recipemanager.controller;

import com.recipemanager.service.RecipeService;
import com.recipemanager.models.Ingredient;
import com.recipemanager.models.Recipe;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
