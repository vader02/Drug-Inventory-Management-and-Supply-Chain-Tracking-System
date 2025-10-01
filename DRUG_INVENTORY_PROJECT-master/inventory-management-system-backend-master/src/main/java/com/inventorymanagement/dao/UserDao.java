package com.inventorymanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.inventorymanagement.model.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

	User findByEmailIdAndPasswordAndRole(String emailId, String password, String role);

	User findByEmailIdAndRole(String emailId, String role);

	List<User> findByRole(String role);
	
	Long countByRole(String role);
	

}
