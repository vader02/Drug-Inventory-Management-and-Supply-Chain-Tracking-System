package com.inventorymanagement.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;

import com.inventorymanagement.dao.CategoryDao;
import com.inventorymanagement.dao.ProductDao;
import com.inventorymanagement.dao.UserDao;
import com.inventorymanagement.dto.CommonApiResponse;
import com.inventorymanagement.dto.ProductAddRequest;
import com.inventorymanagement.dto.ProductResponse;
import com.inventorymanagement.model.Category;
import com.inventorymanagement.model.Product;
import com.inventorymanagement.model.User;
import com.inventorymanagement.service.ProductService;
import com.inventorymanagement.utility.Constants.ProductStatus;
import com.inventorymanagement.utility.StorageService;

@Component
public class ProductResource {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private StorageService storageService;

	public ResponseEntity<CommonApiResponse> addProduct(ProductAddRequest productDto) {
		CommonApiResponse response = new CommonApiResponse();

		if (productDto == null) {
			response.setResponseMessage("bad request - missing request");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (!ProductAddRequest.validateProduct(productDto)) {
			response.setResponseMessage("bad request - missing field");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);

		}

		Product product = ProductAddRequest.toEntity(productDto);

		if (product == null) {
			response.setResponseMessage("bad request - missing field");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);

		}

		Optional<Category> optional = categoryDao.findById(productDto.getCategoryId());
		Category category = null;
		if (optional.isPresent()) {
			category = optional.get();
		}

		if (category == null) {
			response.setResponseMessage("please select correct product category");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);

		}

		Optional<User> supplierOptional = this.userDao.findById(productDto.getSupplierId());

		if (supplierOptional.isEmpty()) {
			response.setResponseMessage("Supplier Not Found!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User supplier = supplierOptional.get();

		product.setSuppliedQuantity(productDto.getQuantity());
		product.setCategory(category);
		product.setSupplier(supplier);
		product.setStatus(ProductStatus.ACTIVE.value());

		try {
			productService.addProduct(product, productDto.getImage());

			response.setResponseMessage("Product Added Successful!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.setResponseMessage("Failed to add the Product");
		response.setSuccess(false);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<ProductResponse> getAllAvailableProducts() {
		ProductResponse response = new ProductResponse();

		List<Product> products = new ArrayList<Product>();

		products = productDao.findByStatus(ProductStatus.ACTIVE.value());

		if (CollectionUtils.isEmpty(products)) {
			response.setResponseMessage("Products not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponse>(response, HttpStatus.OK);
		}

		response.setProducts(products);
		response.setResponseMessage("Product Fetched Successful!!!");
		response.setSuccess(true);

		return new ResponseEntity<ProductResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<ProductResponse> getProductById(int productId) {
		ProductResponse response = new ProductResponse();

		if (productId == 0) {
			response.setResponseMessage("Product Id is missing");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Product product = new Product();

		Optional<Product> optional = productDao.findById(productId);

		if (optional.isPresent()) {
			product = optional.get();
		}

		if (product == null) {
			response.setResponseMessage("Product not found");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setProducts(Arrays.asList(product));
		response.setResponseMessage("Product Fetched Successful!!!");
		response.setSuccess(true);

		return new ResponseEntity<ProductResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<?> getProductsByCategories(int categoryId) {
		ProductResponse response = new ProductResponse();

		if (categoryId == 0) {
			response.setResponseMessage("Category Id is missing");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Category category = null;

		Optional<Category> optional = categoryDao.findById(categoryId);

		if (optional.isPresent()) {
			category = optional.get();
		}

		if (category == null) {
			response.setResponseMessage("Category not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponse>(response, HttpStatus.BAD_REQUEST);

		}

		List<Product> products = new ArrayList<Product>();

		products = productDao.findByCategoryIdAndStatus(categoryId, ProductStatus.ACTIVE.value());

		if (CollectionUtils.isEmpty(products)) {
			response.setResponseMessage("Products not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponse>(response, HttpStatus.OK);
		}

		response.setProducts(products);
		response.setResponseMessage("Product Fetched Successful!!!");
		response.setSuccess(true);

		return new ResponseEntity<ProductResponse>(response, HttpStatus.OK);
	}

	public void fetchProductImage(String productImageName, HttpServletResponse resp) {
		Resource resource = storageService.load(productImageName);
		if (resource != null) {
			try (InputStream in = resource.getInputStream()) {
				ServletOutputStream out = resp.getOutputStream();
				FileCopyUtils.copy(in, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ResponseEntity<CommonApiResponse> deleteProduct(Integer productId) {
		CommonApiResponse response = new CommonApiResponse();

		if (productId == null) {
			response.setResponseMessage("bad request - product id missing");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Product product = new Product();

		Optional<Product> optional = productDao.findById(productId);

		if (optional.isEmpty()) {
			response.setResponseMessage("product not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		product = optional.get();

		if (product == null) {
			response.setResponseMessage("product not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		product.setStatus(ProductStatus.DEACTIVATED.value());
		Product deletedProduct = this.productDao.save(product);

		if (deletedProduct == null) {
			response.setResponseMessage("Failed to delete the product");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.setResponseMessage("Product Deleted Successful!!!");
		response.setSuccess(true);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<CommonApiResponse> updateProduct(ProductAddRequest productDto) {
		CommonApiResponse response = new CommonApiResponse();

		if (productDto == null) {
			response.setResponseMessage("bad request - missing request");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (!ProductAddRequest.validateUpdateProduct(productDto)) {
			response.setResponseMessage("bad request - missing field");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);

		}

		Product product = this.productDao.findById(productDto.getId()).get();

		if (product == null) {
			response.setResponseMessage("product not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		product.setTitle(productDto.getTitle());
		product.setDescription(productDto.getDescription());
		product.setPrice(productDto.getPrice());
		product.setPurchasePrice(productDto.getPurchasePrice());

		if (productDto.getQuantity() != 0) {

			// basically we have to add extra quantity in supplied quantity and available
			// quantity
			if (productDto.getQuantity() > 0) {
				product.setQuantity(product.getQuantity() + productDto.getQuantity());
				product.setSuppliedQuantity(product.getSuppliedQuantity() + productDto.getQuantity());
			} else { // we have to reduce the quantity from supplied quantity and available quantity
				int availableQuantity = product.getQuantity();

				// if 30 + -20
				if (availableQuantity + productDto.getQuantity() < 0) {
					response.setResponseMessage(
							"Only " + availableQuantity + " product available, Failed to update the Product");
					response.setSuccess(false);

					return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
				} else {
					product.setQuantity(product.getQuantity() + productDto.getQuantity());
					product.setSuppliedQuantity(product.getSuppliedQuantity() + productDto.getQuantity());
				}
			}
		}

		// it will update the category if changed
		if (product.getCategory().getId() != productDto.getCategoryId()) {
			Category category = this.categoryDao.findById(productDto.getCategoryId()).get();
			product.setCategory(category);
		}

		// it will update the supplier if changed
		if (product.getSupplier().getId() != productDto.getSupplierId()) {
			User supplier = this.userDao.findById(productDto.getSupplierId()).get();
			product.setSupplier(supplier);
		}

		try {
			product.setStatus(ProductStatus.ACTIVE.value());
			productService.addProduct(product, productDto.getImage());

			response.setResponseMessage("Product Updated Successful!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.setResponseMessage("Failed to update the Product");
		response.setSuccess(false);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public ResponseEntity<ProductResponse> getAllProducts() {
		ProductResponse response = new ProductResponse();

		List<Product> products = new ArrayList<Product>();

		products = productDao.findAll();

		if (CollectionUtils.isEmpty(products)) {
			response.setResponseMessage("Products not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponse>(response, HttpStatus.OK);
		}

		response.setProducts(products);
		response.setResponseMessage("Product Fetched Successful!!!");
		response.setSuccess(true);

		return new ResponseEntity<ProductResponse>(response, HttpStatus.OK);
	}

}
