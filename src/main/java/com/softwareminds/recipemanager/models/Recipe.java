package com.softwareminds.recipemanager.models;

import java.util.List;

public record Recipe(//todo preptime
    Integer id, String name, String description, List<Ingredient> ingredients, List<Step> steps) {}
