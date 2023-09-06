package com.recipemanager.models;

import java.util.List;

public record Recipe(
    Integer id,
    String name,
    String prepTime,
    String description,
    List<Ingredient> ingredients,
    List<Step> steps) {}
