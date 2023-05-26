package com.quantion.ejercicio.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.quantion.ejercicio.model.User;
import com.quantion.ejercicio.model.UserResponse;

@RestController
public class UserRestController {

	List<User> userList = new ArrayList<>();

	/**
	 * Method to retrieve all the users of the application
	 * 
	 * @return Users
	 */
	@GetMapping("/users")
	public ResponseEntity<List<UserResponse>> getUsers() {

		return new ResponseEntity<>(userList.stream()
				.map(u -> new UserResponse(u.getId(), u.getName(), u.getSurname(), u.getEmail(), u.getAge(), u.isActive()))
				.collect(Collectors.toList()), HttpStatus.OK);

	}

	/**
	 * Method to save a new user
	 * 
	 * @param user User to be created
	 * @return Created user
	 */
	@PostMapping("/users")
	public ResponseEntity<UserResponse> saveUser(@RequestBody User user) {

		Optional<User> existingUser = userList.stream().filter(u -> u.getId().longValue() == user.getId().longValue())
				.findFirst();

		if (!existingUser.isPresent()) {
			userList.add(user);

			return new ResponseEntity<>(userList.stream().filter(u -> u.getId().longValue() == user.getId().longValue())
					.findFirst()
					.map(u -> new UserResponse(u.getId(), u.getName(), u.getSurname(), u.getEmail(), u.getAge(), u.isActive()))
					.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Method to delete a user
	 * 
	 * @param id Id of the user to be deleted
	 * @return
	 */
	@DeleteMapping("/users/{id}")
	public ResponseEntity<Boolean> deleteUser(@PathVariable("id") Long id) {

		Optional<User> existingUser = userList.stream().filter(u -> u.getId().longValue() == id.longValue()).findFirst();

		if (existingUser.isPresent()) {
			userList = userList.stream().filter(u -> u.getId().longValue() != id.longValue()).collect(Collectors.toList());
			return new ResponseEntity<>(true, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

	}

	/**
	 * Method to update the info of an user
	 * 
	 * @param id   Id of the user to be updated
	 * @param user User
	 * @return The updated user
	 */
	@PutMapping("/users/{id}")
	public ResponseEntity<UserResponse> updateUser(@PathVariable("id") Long id, @RequestBody User user) {

		Optional<User> existingUser = userList.stream().filter(u -> u.getId().longValue() == id.longValue()).findFirst();

		if (existingUser.isPresent()) {
			userList = userList.stream().filter(u -> u.getId().longValue() != id.longValue()).collect(Collectors.toList());
			user.setId(id);
			userList.add(user);
			return new ResponseEntity<>(userList.stream().filter(u -> u.getId().longValue() == user.getId().longValue())
					.findFirst()
					.map(u -> new UserResponse(u.getId(), u.getName(), u.getSurname(), u.getEmail(), u.getAge(), u.isActive()))
					.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

}
