package com.softwareminds.recipemanager.repository;

import com.softwareminds.recipemanager.entities.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository
    extends JpaRepository<RecipeEntity, Integer>,
        PagingAndSortingRepository<RecipeEntity, Integer> {
  String QUERY = "SELECT id FROM ingredient WHERE recipe_id=:id";
  String SELECT_QUERY =
      "SELECT * FROM recipe WHERE LOWER(title) = LOWER(:name) OR LOWER(title) LIKE CONCAT(LOWER(:name), '%')";

  @Query(value = QUERY, nativeQuery = true)
  List<Integer> getRecipeIngredients(int id);

  @Query(value = SELECT_QUERY, nativeQuery = true)
  List<RecipeEntity> getRecipesFilterByName(String name);
}
