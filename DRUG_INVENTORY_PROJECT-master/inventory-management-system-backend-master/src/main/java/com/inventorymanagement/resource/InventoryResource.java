package com.inventorymanagement.resource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.inventorymanagement.dao.CategoryDao;
import com.inventorymanagement.dao.OrderDao;
import com.inventorymanagement.dao.ProductDao;
import com.inventorymanagement.dao.UserDao;
import com.inventorymanagement.dto.InventoryDashboardDataResponse;
import com.inventorymanagement.dto.ProductProfitDTO;
import com.inventorymanagement.dto.ProductQuantityDTO;
import com.inventorymanagement.model.Orders;
import com.inventorymanagement.service.InventoryService;

@Component
public class InventoryResource {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private InventoryService inventoryService;

	public ResponseEntity<InventoryDashboardDataResponse> getTodaysInventoryData() {

		InventoryDashboardDataResponse response = new InventoryDashboardDataResponse();

		LocalDateTime now = LocalDateTime.now();

		LocalDateTime todayMidnight = LocalDateTime.of(now.toLocalDate(), LocalTime.MIDNIGHT);

		LocalDateTime todayEOD = LocalDateTime.of(now.toLocalDate(), LocalTime.MAX);

		String startTime = String.valueOf(todayMidnight.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		String endTime = String.valueOf(todayEOD.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		Long totalProductCount = productDao.count();
		Long totalCategoryCount = categoryDao.count();
		Long totalCustomerCount = userDao.countByRole("Customer");
		Integer totalOrderCount = orderDao.countDistinctOrderIds();
		Integer totalTodaysOrdersCount = this.orderDao.countDistinctOrderIdsByStartTimeAndEndTime(startTime, endTime);
		Integer totalProductsSold = this.inventoryService.getTodaysTotalProductsSoldWithQuantity(startTime, endTime);
		
		List<Orders> todaysOrder = new ArrayList<>();
				
		List<Orders> orders = this.orderDao.findByOrderDateBetweenOrderByIdDesc(startTime, endTime);

		if(!CollectionUtils.isEmpty(orders)) {
			todaysOrder = orders;
		}
		
		BigDecimal totalSale = inventoryService.getTotalSalesByDate(startTime, endTime);

		BigDecimal totalProfit = inventoryService.getTotalProfitByDate(startTime, endTime);

		inventoryService.getTotalSalesByDate(startTime, endTime);

		List<ProductQuantityDTO> top5HighestSoldProducts = new ArrayList<>();

		List<ProductProfitDTO> top5HighestProfitProducts = new ArrayList<>();

		List<ProductQuantityDTO> highestSoldProducts = this.inventoryService
				.getTop5HighestSoldProductByStartTimeAndEndTime(startTime, endTime);

		if (!CollectionUtils.isEmpty(highestSoldProducts)) {
			top5HighestSoldProducts = highestSoldProducts;
		}

		List<ProductProfitDTO> highestProfitProducts = this.inventoryService
				.getTop5HighestProfitableProductByStartTimeAndEndTime(startTime, endTime);

		if (!CollectionUtils.isEmpty(highestProfitProducts)) {
			top5HighestProfitProducts = highestProfitProducts;
		}

		response.setTop5HighestProfitProducts(top5HighestProfitProducts);
		response.setTop5HighestSoldProducts(top5HighestSoldProducts);
		response.setTotalCategoryCount(totalCategoryCount);
		response.setTotalCustomerCount(totalCustomerCount);
		response.setTotalOrderCount(totalOrderCount);
		response.setTotalProductCount(totalProductCount);
		response.setTotalProductsSold(totalProductsSold);
		response.setTotalProfit(totalProfit);
		response.setTotalSale(totalSale);
		response.setTotalTodaysOrdersCount(totalTodaysOrdersCount);
		response.setTotalOrders(todaysOrder);

		response.setResponseMessage("Inventory Data Fetched Successful!!!");
		response.setSuccess(true);

		return new ResponseEntity<InventoryDashboardDataResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<InventoryDashboardDataResponse> getInventoryDataUsingTimeRange(String startTime,
			String endTime) {

		InventoryDashboardDataResponse response = new InventoryDashboardDataResponse();

		if(startTime == null || endTime == null) {
			response.setResponseMessage("Missing time range!!!");
			response.setSuccess(false);

			return new ResponseEntity<InventoryDashboardDataResponse>(response, HttpStatus.BAD_REQUEST);
		}

//		Long totalProductCount = productDao.count();
//		Long totalCategoryCount = categoryDao.count();
//		Long totalCustomerCount = userDao.countByRole("Customer");
//		Integer totalOrderCount = orderDao.countDistinctOrderIds();
		Integer totalTodaysOrdersCount = this.orderDao.countDistinctOrderIdsByStartTimeAndEndTime(startTime, endTime);
		Integer totalProductsSold = this.inventoryService.getTodaysTotalProductsSoldWithQuantity(startTime, endTime);

		List<Orders> todaysOrder = new ArrayList<>();
		
		List<Orders> orders = this.orderDao.findByOrderDateBetweenOrderByIdDesc(startTime, endTime);

		if(!CollectionUtils.isEmpty(orders)) {
			todaysOrder = orders;
		}
		
		BigDecimal totalSale = inventoryService.getTotalSalesByDate(startTime, endTime);

		BigDecimal totalProfit = inventoryService.getTotalProfitByDate(startTime, endTime);

		inventoryService.getTotalSalesByDate(startTime, endTime);

		List<ProductQuantityDTO> top5HighestSoldProducts = new ArrayList<>();

		List<ProductProfitDTO> top5HighestProfitProducts = new ArrayList<>();

		List<ProductQuantityDTO> highestSoldProducts = this.inventoryService
				.getTop5HighestSoldProductByStartTimeAndEndTime(startTime, endTime);

		if (!CollectionUtils.isEmpty(highestSoldProducts)) {
			top5HighestSoldProducts = highestSoldProducts;
		}

		List<ProductProfitDTO> highestProfitProducts = this.inventoryService
				.getTop5HighestProfitableProductByStartTimeAndEndTime(startTime, endTime);

		if (!CollectionUtils.isEmpty(highestProfitProducts)) {
			top5HighestProfitProducts = highestProfitProducts;
		}

		response.setTop5HighestProfitProducts(top5HighestProfitProducts);
		response.setTop5HighestSoldProducts(top5HighestSoldProducts);
//		response.setTotalCategoryCount(totalCategoryCount);
//		response.setTotalCustomerCount(totalCustomerCount);
//		response.setTotalOrderCount(totalOrderCount);
//		response.setTotalProductCount(totalProductCount);
		response.setTotalProductsSold(totalProductsSold);
		response.setTotalProfit(totalProfit);
		response.setTotalSale(totalSale);
		response.setTotalTodaysOrdersCount(totalTodaysOrdersCount);
		response.setTotalOrders(todaysOrder);
		
		response.setResponseMessage("Inventory Data Fetched Successful!!!");
		response.setSuccess(true);

		return new ResponseEntity<InventoryDashboardDataResponse>(response, HttpStatus.OK);
	}

}
