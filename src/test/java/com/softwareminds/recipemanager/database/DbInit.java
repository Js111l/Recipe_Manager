package com.softwareminds.recipemanager.config;

import com.softwareminds.recipemanager.models.Ingredient;
import com.softwareminds.recipemanager.models.QuantityUnit;
import com.softwareminds.recipemanager.models.Recipe;
import com.softwareminds.recipemanager.models.Step;
import com.softwareminds.recipemanager.repository.AmountRepository;
import com.softwareminds.recipemanager.repository.IngredientRepository;
import com.softwareminds.recipemanager.repository.StepRepository;
import com.softwareminds.recipemanager.service.RecipeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class DbInit implements CommandLineRunner {
  private final RecipeService recipeService;
  private final IngredientRepository ingredientRepository;

  private final AmountRepository amountRepository;

  private final StepRepository stepRepository;

  public DbInit(
      RecipeService recipeService,
      IngredientRepository ingredientRepository,
      AmountRepository amountRepository,
      StepRepository stepRepository) {
    this.recipeService = recipeService;
    this.ingredientRepository = ingredientRepository;
    this.amountRepository = amountRepository;
    this.stepRepository = stepRepository;
  }

  @Override
  public void run(String... args) {

    var recipe =
        new Recipe(
            1,
            "Recipe",
            Duration.of(1, ChronoUnit.HOURS).toString(),
            "Desc",
            List.of(new Ingredient(1, "Ingr", 200, QuantityUnit.GRAMS)),
            List.of(new Step(1, 1, "Content")));

    this.recipeService.saveRecipe(recipe);
  }
}
