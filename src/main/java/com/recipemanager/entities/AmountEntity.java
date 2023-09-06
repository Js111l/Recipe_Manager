package com.recipemanager.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.recipemanager.models.QuantityUnit;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "Amount")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id",
    scope = AmountEntity.class)
public class AmountEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private double amount;

  @ManyToOne
  @JoinColumn(name = "recipe_id")
  private RecipeEntity recipe;

  @Enumerated(value = EnumType.STRING)
  private QuantityUnit unit;

  @ManyToOne
  @JoinColumn(name = "ingredient_id")
  private IngredientEntity ingredient;
}
