package com.recipemanager.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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

  private String prepTime;

  private String shortDescription;

  @OneToMany(
      mappedBy = "recipe",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<AmountEntity> amountEntities = new ArrayList<>();

  @OneToMany(
      mappedBy = "recipe",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<IngredientEntity> ingredients = new ArrayList<>();

  @OneToMany(
      mappedBy = "recipe",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<StepEntity> steps = new ArrayList<>();

  public void addAmountEntity(AmountEntity amountEntity) {
    if (this.amountEntities == null) {
      this.amountEntities = new ArrayList<>();
    }
    this.amountEntities.add(amountEntity);
  }
}
