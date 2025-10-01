package com.inventorymanagement.service;

import org.springframework.web.multipart.MultipartFile;

import com.inventorymanagement.model.Product;

public interface ProductService {
	
	void addProduct(Product product, MultipartFile productImmage);

}
