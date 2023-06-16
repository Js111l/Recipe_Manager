package com.softwareminds.recipemanager.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.softwareminds.recipemanager.models.QuantityUnit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

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
