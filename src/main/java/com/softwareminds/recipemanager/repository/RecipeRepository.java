package com.softwareminds.recipemanager.repository;

import com.softwareminds.recipemanager.entities.IngredientEntity;
import com.softwareminds.recipemanager.entities.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Integer> {
  String QUERY = "SELECT id FROM ingredient WHERE recipe_id=:id";

  @Query(value = QUERY, nativeQuery = true)
  List<Integer> getRecipeIngredients(int id);
}
