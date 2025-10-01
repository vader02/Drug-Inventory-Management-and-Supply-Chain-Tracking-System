package com.inventorymanagement.dto;

import com.inventorymanagement.model.Product;

public class ProductQuantityDTO {

	private Product product;

	private Long totalSold;

	public Product getProduct() {
		return product;
	}

	public ProductQuantityDTO(Product product, Long totalSold) {
		super();
		this.product = product;
		this.totalSold = totalSold;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Long getTotalSold() {
		return totalSold;
	}

	public void setTotalSold(Long totalSold) {
		this.totalSold = totalSold;
	}

}
