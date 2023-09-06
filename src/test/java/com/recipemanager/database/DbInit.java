package com.recipemanager.database;

import com.recipemanager.models.Ingredient;
import com.recipemanager.models.QuantityUnit;
import com.recipemanager.models.Recipe;
import com.recipemanager.models.Step;
import com.recipemanager.service.RecipeService;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DbInit {
  private final RecipeService recipeService;

  public DbInit(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  public List<Recipe> getRecipesList() {

    var ingrCounter = new AtomicInteger(0);
    var stepCounter = new AtomicInteger(0);

    return IntStream.rangeClosed(1, 6)
        .mapToObj(
            x ->
                new Recipe(
                    x,
                    "Recipe" + x,
                    Duration.of(x, ChronoUnit.HOURS).toString(),
                    "Short description of a recipe",
                    List.of(
                        new Ingredient(ingrCounter.incrementAndGet(), "Milk", 200, QuantityUnit.ML),
                        new Ingredient(ingrCounter.incrementAndGet(), "Egg", 3, QuantityUnit.PIECE),
                        new Ingredient(
                            ingrCounter.incrementAndGet(), "Cheese", 100, QuantityUnit.GRAMS)),
                    List.of(
                        new Step(stepCounter.incrementAndGet(), 1, "First step!"),
                        new Step(stepCounter.incrementAndGet(), 2, "Second step!"),
                        new Step(stepCounter.incrementAndGet(), 3, "Third step!"))))
        .collect(Collectors.toList());
  }

  public void initTestData() {
    getRecipesList().forEach(this.recipeService::saveRecipe);
  }
}
