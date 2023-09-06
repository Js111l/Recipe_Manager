package com.recipemanager.models;

public record Ingredient(Integer id, String name, double amount, QuantityUnit unit) {}
