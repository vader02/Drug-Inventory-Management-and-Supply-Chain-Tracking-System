package com.inventorymanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventorymanagement.model.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {

	List<Product> findByCategoryIdAndStatus(int category, String status);

	List<Product> findByStatus(String status);
	
}