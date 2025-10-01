package com.inventorymanagement.dto;

import java.math.BigDecimal;

import com.inventorymanagement.model.Product;

public class ProductProfitDTO {

	private Product product;
	private Long totalSold;
	private BigDecimal totalProfit;

	public ProductProfitDTO() {
		super();
	}

	public ProductProfitDTO(Product product, Long totalSold, BigDecimal totalProfit) {
		super();
		this.product = product;
		this.totalSold = totalSold;
		this.totalProfit = totalProfit;
	}

	public Product getProduct() {
		return product;
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

	public BigDecimal getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(BigDecimal totalProfit) {
		this.totalProfit = totalProfit;
	}

}
