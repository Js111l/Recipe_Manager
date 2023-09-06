package com.recipemanager.repository;

import com.recipemanager.entities.AmountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmountRepository extends JpaRepository<AmountEntity, Integer> {}
