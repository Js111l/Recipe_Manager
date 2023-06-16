package com.softwareminds.recipemanager.repository;

import com.softwareminds.recipemanager.entities.StepEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepRepository extends JpaRepository<StepEntity, Integer> {}
