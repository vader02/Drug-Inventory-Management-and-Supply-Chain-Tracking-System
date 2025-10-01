package com.inventorymanagement.dto;

import java.util.ArrayList;
import java.util.List;

import com.inventorymanagement.model.User;

public class UserResponse extends CommonApiResponse {

	private List<User> users = new ArrayList<>();

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
