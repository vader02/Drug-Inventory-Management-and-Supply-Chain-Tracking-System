package com.inventorymanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inventorymanagement.model.Category;

public interface CategoryDao extends JpaRepository<Category, Integer> {

}
