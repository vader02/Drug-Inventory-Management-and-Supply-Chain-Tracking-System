package com.inventorymanagement.resource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.inventorymanagement.dao.AddressDao;
import com.inventorymanagement.dao.UserDao;
import com.inventorymanagement.dto.AddUserRequest;
import com.inventorymanagement.dto.UserLoginRequest;
import com.inventorymanagement.dto.UserResponse;
import com.inventorymanagement.exception.UserSaveFailedException;
import com.inventorymanagement.model.Address;
import com.inventorymanagement.model.User;

@Component
@Transactional
public class UserResource {

	@Autowired
	private UserDao userDao;

	@Autowired
	private AddressDao addressDao;

	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public ResponseEntity<UserResponse> registerUser(AddUserRequest userRequest) {
		UserResponse response = new UserResponse();

		if (userRequest == null) {
			response.setResponseMessage("bad request - missing request");
			response.setSuccess(false);

			return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (!AddUserRequest.validate(userRequest)) {
			response.setResponseMessage("bad request - missing input");
			response.setSuccess(false);

			return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (userRequest.getPhoneNo().length() != 10) {
			response.setResponseMessage("Enter Valid Mobile No");
			response.setSuccess(false);

			return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Address address = new Address();
		address.setCity(userRequest.getCity());
		address.setPincode(userRequest.getPincode());
		address.setStreet(userRequest.getStreet());

		Address addAddress = addressDao.save(address);

		if (addAddress == null) {
			response.setResponseMessage("Failed to register User");
			response.setSuccess(false);

			return new ResponseEntity<UserResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		User user = new User();
		user.setAddress(addAddress);
		user.setEmailId(userRequest.getEmailId());
		user.setFirstName(userRequest.getFirstName());
		user.setLastName(userRequest.getLastName());
		user.setPhoneNo(userRequest.getPhoneNo());
//		user.setPassword(userRequest.getPassword());
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setRole(userRequest.getRole());
		User addUser = userDao.save(user);
		
		if(addUser == null) {
			throw new UserSaveFailedException("Failed to register the User");
		}

//		response.setUsers(Arrays.asList(addUser));
		response.setUsers(Collections.singletonList(addUser));
		response.setResponseMessage("User Registered Successful");
		response.setSuccess(true);

		return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
	}

//	public ResponseEntity<UserResponse> loginUser(UserLoginRequest loginRequest) {
//		UserResponse response = new UserResponse();
//
//		if (loginRequest == null) {
//			response.setResponseMessage("bad request - missing request");
//			response.setSuccess(false);
//
//			return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_REQUEST);
//		}
//
//		if (!UserLoginRequest.validateLoginRequest(loginRequest)) {
//			response.setResponseMessage("bad request - missing input");
//			response.setSuccess(false);
//
//			return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_REQUEST);
//		}
//
//		User user = new User();
//		user = userDao.findByEmailIdAndPasswordAndRole(loginRequest.getEmailId(), loginRequest.getPassword(),
//				loginRequest.getRole());
//
//		if (user == null) {
//			response.setResponseMessage("User not found in system");
//			response.setSuccess(false);
//
//			return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_REQUEST);
//		}
//
//		response.setUsers(Arrays.asList(user));
//		response.setResponseMessage("Logged in Successful");
//		response.setSuccess(true);
//
//		return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
//	}
	
	public ResponseEntity<UserResponse> loginUser(UserLoginRequest loginRequest) {
	    UserResponse response = new UserResponse();

	    if (loginRequest == null || !UserLoginRequest.validateLoginRequest(loginRequest)) {
	        response.setResponseMessage("Bad request - missing input");
	        response.setSuccess(false);
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }

	    // Fetch user by email and role (password can't be queried directly due to encryption)
	    User user = userDao.findByEmailIdAndRole(loginRequest.getEmailId(), loginRequest.getRole());

	    if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
	        response.setResponseMessage("Invalid email, password, or role");
	        response.setSuccess(false);
	        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	    }

	    response.setUsers(Collections.singletonList(user));
	    response.setResponseMessage("Logged in Successfully");
	    response.setSuccess(true);

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}


	public ResponseEntity<UserResponse> getAllDeliveryPersons() {
		UserResponse response = new UserResponse();

		List<User> deliveryPersons = this.userDao.findByRole("Delivery");

		if (CollectionUtils.isEmpty(deliveryPersons)) {
			response.setResponseMessage("No Delivery Person Found");
			response.setSuccess(false);

			return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
		}

		response.setUsers(deliveryPersons);
		response.setResponseMessage("Delivery Persons Fected Success!!!");
		response.setSuccess(true);

		return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<UserResponse> forgetPassword(UserLoginRequest request) {
		UserResponse response = new UserResponse();

		if (request == null) {
			response.setResponseMessage("bad request - missing request");
			response.setSuccess(false);

			return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (!UserLoginRequest.validateForgetRequest(request)) {
			response.setResponseMessage("bad request - missing input");
			response.setSuccess(false);

			return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User existingCustomer = this.userDao.findByEmailIdAndRole(request.getEmailId(), "Customer");
		
		if(existingCustomer == null) {
			response.setResponseMessage("User with this email id, Not Exist!!!");
			response.setSuccess(false);

			return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		User user = new User();
		user = userDao.findByEmailIdAndPasswordAndRole(request.getEmailId(), request.getPassword(),
				"Customer");

		if (user == null) {
			response.setResponseMessage("Current Password is Wrong!!!");
			response.setSuccess(false);

			return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		user.setPassword(request.getNewPassword());

        User addUser = userDao.save(user);
		
		if(addUser == null) {
			throw new UserSaveFailedException("Failed to update the Password");
		}
		
		response.setResponseMessage("Password Changed Successful!!!");
		response.setSuccess(true);

		return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<UserResponse> getAllSuppliers() {
		UserResponse response = new UserResponse();

		List<User> suppliers = this.userDao.findByRole("Supplier");

		if (CollectionUtils.isEmpty(suppliers)) {
			response.setResponseMessage("No Suppliers found!!!");
			response.setSuccess(false);

			return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
		}

		response.setUsers(suppliers);
		response.setResponseMessage("Suppliers Fected Success!!!");
		response.setSuccess(true);

		return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
	}

}
