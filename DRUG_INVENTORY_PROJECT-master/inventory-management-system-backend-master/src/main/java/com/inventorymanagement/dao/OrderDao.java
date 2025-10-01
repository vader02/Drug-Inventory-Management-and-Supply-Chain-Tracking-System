package com.inventorymanagement.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inventorymanagement.dto.ProductProfitDTO;
import com.inventorymanagement.dto.ProductQuantityDTO;
import com.inventorymanagement.model.Orders;
import com.inventorymanagement.model.User;

@Repository
public interface OrderDao extends JpaRepository<Orders, Integer> {

	List<Orders> findByUser_id(int userId);

	List<Orders> findByOrderId(String orderId);

	List<Orders> findByUser_idAndProduct_id(int userId, int productId);

	List<Orders> findByUser(User user);

	List<Orders> findByDeliveryPersonId(int deliveryPersonId);

	List<Orders> findByOrderDateBetweenOrderByIdDesc(String startTime, String endTime);

	// highest sold product
	@Query("SELECT NEW com.inventorymanagement.dto.ProductQuantityDTO(o.product, SUM(o.quantity) AS totalSold) "
			+ "FROM Orders o " + "WHERE o.orderDate BETWEEN :startDate AND :endDate " + "GROUP BY o.product "
			+ "ORDER BY totalSold DESC")
	List<ProductQuantityDTO> findTop5ProductsByTotalQuantitySoldBetweenDates(@Param("startDate") String startDate,
			@Param("endDate") String endDate, Pageable pageable);

	// highest profitable product
	@Query("SELECT NEW com.inventorymanagement.dto.ProductProfitDTO(o.product, SUM(o.quantity) AS totalSold, "
			+ "(SUM(o.quantity) * (p.price - p.purchasePrice)) AS totalProfit) " + "FROM Orders o JOIN o.product p "
			+ "WHERE o.orderDate BETWEEN :startDate AND :endDate " + "GROUP BY o.product "
			+ "ORDER BY totalProfit DESC")
	List<ProductProfitDTO> findTop5ProductsByTotalProfitAndOrderDateRange(@Param("startDate") String startDate,
			@Param("endDate") String endDate, Pageable pageable);

//	@Query("SELECT NEW com.example.dto.ProductProfitDTO(o.product, SUM(o.quantity) AS totalSold, "
//			+ "(SUM(o.quantity) * (p.price - p.purchasePrice)) AS totalProfit) " + "FROM Orders o "
//			+ "JOIN o.product p " + "GROUP BY o.product " + "ORDER BY totalProfit DESC " + "LIMIT 5")
//	List<ProductProfitDTO> findTop5ProductsByTotalProfit();

	@Query("SELECT COUNT(DISTINCT o.orderId) FROM Orders o")
	Integer countDistinctOrderIds();

	@Query("SELECT COUNT(DISTINCT o.orderId) FROM Orders o where o.orderDate BETWEEN :startDate AND :endDate")
	Integer countDistinctOrderIdsByStartTimeAndEndTime(@Param("startDate") String startDate,
			@Param("endDate") String endDate);

}
