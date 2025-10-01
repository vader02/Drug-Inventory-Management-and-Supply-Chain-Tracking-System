package com.inventorymanagement.dto;

import java.math.BigDecimal;
import java.util.List;

import com.inventorymanagement.model.Orders;

public class InventoryDashboardDataResponse extends CommonApiResponse {

	private BigDecimal totalSale;

	private BigDecimal totalProfit;

	private Long totalCustomerCount;

	private Long totalCategoryCount;

	private Long totalProductCount;

	private Integer totalTodaysOrdersCount;

	private Integer totalOrderCount;

	private Integer totalProductsSold; // quantity count

	// highest sold products --> based quantity
	private List<ProductQuantityDTO> top5HighestSoldProducts;

	// highest profit products --> based on profit
	private List<ProductProfitDTO> top5HighestProfitProducts;

	private List<Orders> totalOrders;

	public BigDecimal getTotalSale() {
		return totalSale;
	}

	public void setTotalSale(BigDecimal totalSale) {
		this.totalSale = totalSale;
	}

	public BigDecimal getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(BigDecimal totalProfit) {
		this.totalProfit = totalProfit;
	}

	public Long getTotalCustomerCount() {
		return totalCustomerCount;
	}

	public void setTotalCustomerCount(Long totalCustomerCount) {
		this.totalCustomerCount = totalCustomerCount;
	}

	public Long getTotalCategoryCount() {
		return totalCategoryCount;
	}

	public void setTotalCategoryCount(Long totalCategoryCount) {
		this.totalCategoryCount = totalCategoryCount;
	}

	public Long getTotalProductCount() {
		return totalProductCount;
	}

	public void setTotalProductCount(Long totalProductCount) {
		this.totalProductCount = totalProductCount;
	}

	public Integer getTotalTodaysOrdersCount() {
		return totalTodaysOrdersCount;
	}

	public void setTotalTodaysOrdersCount(Integer totalTodaysOrdersCount) {
		this.totalTodaysOrdersCount = totalTodaysOrdersCount;
	}

	public Integer getTotalOrderCount() {
		return totalOrderCount;
	}

	public void setTotalOrderCount(Integer totalOrderCount) {
		this.totalOrderCount = totalOrderCount;
	}

	public Integer getTotalProductsSold() {
		return totalProductsSold;
	}

	public void setTotalProductsSold(Integer totalProductsSold) {
		this.totalProductsSold = totalProductsSold;
	}

	public List<ProductQuantityDTO> getTop5HighestSoldProducts() {
		return top5HighestSoldProducts;
	}

	public void setTop5HighestSoldProducts(List<ProductQuantityDTO> top5HighestSoldProducts) {
		this.top5HighestSoldProducts = top5HighestSoldProducts;
	}

	public List<ProductProfitDTO> getTop5HighestProfitProducts() {
		return top5HighestProfitProducts;
	}

	public void setTop5HighestProfitProducts(List<ProductProfitDTO> top5HighestProfitProducts) {
		this.top5HighestProfitProducts = top5HighestProfitProducts;
	}

	public List<Orders> getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(List<Orders> totalOrders) {
		this.totalOrders = totalOrders;
	}

}
