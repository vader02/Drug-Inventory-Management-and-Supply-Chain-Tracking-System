package com.inventorymanagement.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.inventorymanagement.dao.CategoryDao;
import com.inventorymanagement.dao.OrderDao;
import com.inventorymanagement.dao.ProductDao;
import com.inventorymanagement.dao.UserDao;
import com.inventorymanagement.dto.ProductProfitDTO;
import com.inventorymanagement.dto.ProductQuantityDTO;
import com.inventorymanagement.model.Orders;
import com.inventorymanagement.model.Product;

@Component
public class InventoryService {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private UserDao userDao;

	public BigDecimal getTotalSalesByDate(String startTime, String endTime) {

		BigDecimal totalPrice = BigDecimal.ZERO;

		if (startTime == null || endTime == null) {
			return BigDecimal.ZERO;
		}

		List<Orders> orders = this.orderDao.findByOrderDateBetweenOrderByIdDesc(startTime, endTime);

		if (CollectionUtils.isEmpty(orders)) {
			return BigDecimal.ZERO;
		}

		for (Orders order : orders) {

			Product product = order.getProduct();
			int quantity = order.getQuantity();

			totalPrice = totalPrice.add(product.getPrice().multiply(new BigDecimal(quantity)));

		}

		return totalPrice;
	}

	public BigDecimal getTotalProfitByDate(String startTime, String endTime) {

		BigDecimal totalPrice = BigDecimal.ZERO;

		if (startTime == null || endTime == null) {
			return BigDecimal.ZERO;
		}

		List<Orders> orders = this.orderDao.findByOrderDateBetweenOrderByIdDesc(startTime, endTime);

		if (CollectionUtils.isEmpty(orders)) {
			return BigDecimal.ZERO;
		}

		for (Orders order : orders) {

			Product product = order.getProduct();
			int quantity = order.getQuantity();

			BigDecimal totalSalesPrice = product.getPrice().multiply(new BigDecimal(quantity));

			BigDecimal totalPurchasePrice = product.getPurchasePrice().multiply(new BigDecimal(quantity));

			BigDecimal totalProfit = totalSalesPrice.subtract(totalPurchasePrice);

			totalPrice = totalPrice.add(totalProfit);

		}

		return totalPrice;
	}

	public List<ProductQuantityDTO> getTop5HighestSoldProductByStartTimeAndEndTime(String startTime, String endTime) {

		if (startTime == null || endTime == null) {
			return new ArrayList<ProductQuantityDTO>();
		}

		List<ProductQuantityDTO> top5HighestSoldProducts = new ArrayList<>();

		List<ProductQuantityDTO> products = this.orderDao.findTop5ProductsByTotalQuantitySoldBetweenDates(startTime,
				endTime, PageRequest.of(0, 5));

		if (CollectionUtils.isEmpty(products)) {
			return top5HighestSoldProducts;
		}

		return products;

	}

	public List<ProductProfitDTO> getTop5HighestProfitableProductByStartTimeAndEndTime(String startTime,
			String endTime) {

		if (startTime == null || endTime == null) {
			return new ArrayList<ProductProfitDTO>();
		}

		List<ProductProfitDTO> top5HighestProfitableProducts = new ArrayList<>();

		List<ProductProfitDTO> products = this.orderDao.findTop5ProductsByTotalProfitAndOrderDateRange(startTime,
				endTime, PageRequest.of(0, 5));

		if (CollectionUtils.isEmpty(products)) {
			return top5HighestProfitableProducts;
		}

		return products;

	}
	
	public Integer getTodaysTotalProductsSoldWithQuantity(String startTime, String endTime) {

		Integer totalQuantitySold = 0;

		if (startTime == null || endTime == null) {
			return 0;
		}

		List<Orders> orders = this.orderDao.findByOrderDateBetweenOrderByIdDesc(startTime, endTime);

		if (CollectionUtils.isEmpty(orders)) {
			return 0;
		}

		for (Orders order : orders) {

			Product product = order.getProduct();
			int quantity = order.getQuantity();

			totalQuantitySold = quantity + totalQuantitySold;

		}

		return totalQuantitySold;
	}

}
