package com.softwareminds.recipemanager.config;

import com.softwareminds.recipemanager.entities.*;
import com.softwareminds.recipemanager.models.QuantityUnit;
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
  public void run(String... args) throws Exception {

    var recipe = new RecipeEntity();
    recipe.setTitle("Recipe for bread");
    recipe.setPrepTime(Duration.of(1, ChronoUnit.HOURS));
    recipe.setShortDescription("Quick recipe for bread");
    // 1 step
    var step = new StepEntity();
    step.setStepNumber(1);
    step.setContent("Huhuhudhuhduuhdhuuhhu");
    recipe.setSteps(List.of(step));
    step.setRecipe(recipe);
    // 2.ing
    var ingr = new IngredientEntity();
    ingr.setName("ingr nnn" + 1);
    ingr.setRecipe(recipe);
    // 3.ingr
    var ingr2 = new IngredientEntity();
    ingr2.setName("ingr  nnn" + 2);
    ingr2.setRecipe(recipe);

    // 4.  set ingr
    recipe.setIngredients(List.of(ingr, ingr2));

    //
    AmountEntity amountEntity = new AmountEntity();
    amountEntity.setIngredient(ingr);
    amountEntity.setUnit(QuantityUnit.GRAMS);
    amountEntity.setAmount(200.0);

    AmountEntity amountEntity2 = new AmountEntity();
    amountEntity2.setIngredient(ingr2);
    amountEntity2.setUnit(QuantityUnit.GRAMS);
    amountEntity2.setAmount(30.0);

    amountEntity.setRecipe(recipe);
    amountEntity2.setRecipe(recipe);

    recipe.setAmountEntities(List.of(amountEntity, amountEntity2));

    //  this.recipeService.saveRecipe(recipe);
  }
}
