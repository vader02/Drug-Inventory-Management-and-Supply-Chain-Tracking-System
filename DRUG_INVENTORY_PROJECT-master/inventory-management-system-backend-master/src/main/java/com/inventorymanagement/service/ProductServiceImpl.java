package com.inventorymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.inventorymanagement.dao.ProductDao;
import com.inventorymanagement.model.Product;
import com.inventorymanagement.utility.StorageService;


@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired 
	private ProductDao productDao;
	
	@Autowired
	private StorageService storageService;

	@Override
	public void addProduct(Product product, MultipartFile productImmage) {
		
		String productImageName = storageService.store(productImmage);
		
		product.setImageName(productImageName);
		
		this.productDao.save(product);
	}

}
