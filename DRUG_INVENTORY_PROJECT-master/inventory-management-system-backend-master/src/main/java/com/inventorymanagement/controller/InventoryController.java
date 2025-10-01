package com.inventorymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventorymanagement.dto.InventoryDashboardDataResponse;
import com.inventorymanagement.resource.InventoryResource;

@RestController
@RequestMapping("api/inventory")
@CrossOrigin(origins = "http://localhost:3000")
public class InventoryController {

	@Autowired
	private InventoryResource inventoryResource;

	@GetMapping("dashboard")
	public ResponseEntity<InventoryDashboardDataResponse> getDashboard() {
		return inventoryResource.getTodaysInventoryData();
	}

	@GetMapping("data/time-range")
	public ResponseEntity<InventoryDashboardDataResponse> getInventoryDataUsingTimeRange(
			@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
		return inventoryResource.getInventoryDataUsingTimeRange(startTime, endTime);
	}

}
