package com.softwareminds.recipemanager.models;

import java.time.Duration;
import java.util.List;

public record Recipe(
    Integer id,
    String name,
    String prepTime,
    String description,
    List<Ingredient> ingredients,
    List<Step> steps) {}
