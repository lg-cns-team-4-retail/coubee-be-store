package com.coubee.coubeebestore.domain.repository;

import com.coubee.coubeebestore.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    List<Category> findAllByNameIn(List<String> names);
}
