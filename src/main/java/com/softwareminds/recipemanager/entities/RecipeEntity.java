package com.softwareminds.recipemanager.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "Recipe")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id",
    scope = RecipeEntity.class)
public class RecipeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String title;
  private Duration prepTime;

  private String shortDescription;

  @OneToMany(
      mappedBy = "recipe",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<AmountEntity> amountEntities;

  @OneToMany(
      mappedBy = "recipe",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<IngredientEntity> ingredients;

  @OneToMany(
      mappedBy = "recipe",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<StepEntity> steps;

  public void addAmountEntity(AmountEntity amountEntity) {
    if (this.amountEntities == null) {
      this.amountEntities = new ArrayList<>();
    }
    this.amountEntities.add(amountEntity);
  }
}
