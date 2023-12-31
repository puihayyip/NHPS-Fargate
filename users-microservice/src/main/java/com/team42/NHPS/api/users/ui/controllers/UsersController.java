/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team42.NHPS.api.users.ui.controllers;

import com.team42.NHPS.api.users.service.UsersService;
import com.team42.NHPS.api.users.shared.UserDto;
import com.team42.NHPS.api.users.ui.model.CreateUserRequestModel;
import com.team42.NHPS.api.users.ui.model.UserResponseModel;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

	@Value("${token.secret}")
	private String token;
	@Value("${server.port}")
	private String port;
	private UsersService usersService;
	private Environment environment;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public UsersController(UsersService usersService, Environment environment) {
		this.usersService = usersService;
		this.environment = environment;
	}

	@PostMapping
	public ResponseEntity<UserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel requestModel) {
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(requestModel, UserDto.class);

		UserDto createdUserDetails = usersService.createUser(userDto);

		UserResponseModel returnValue = modelMapper.map(createdUserDetails, UserResponseModel.class);
		log.info("User created with id: " + returnValue.getUserId());

		return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<String> getUser(@PathVariable("userId") String userId/*,
			@RequestHeader("Authorization") String authorization,
			@RequestParam(value = "fields", required = false) String fields*/) {

		UserDto userDto = usersService.getUserByUserId(userId);

		UserResponseModel returnValue = new ModelMapper().map(userDto, UserResponseModel.class);

//		// Include albums if requested
//		if (fields != null) {
//			String[] includeFields = fields.split(",");
//			for (String field : includeFields) {
//				if (field.trim().equalsIgnoreCase("patients")) {
//					List<PatientsResponseModel> albums = usersService.getUserAlbums(authorization);
//					returnValue.setAlbums(albums);
//					break;
//				}
//			}
//		}

		return ResponseEntity.status(HttpStatus.OK).body(returnValue.getFirstName() + " " + returnValue.getLastName());
	}

	@GetMapping()
	public ResponseEntity<List<UserResponseModel>> getUsers(@RequestHeader("Authorization") String authorization) {

		List<UserDto> userDtoList = usersService.getUsers();

		Type listType = new TypeToken<List<UserResponseModel>>() {
		}.getType();

		List<UserResponseModel> returnValue = new ModelMapper().map(userDtoList, listType);
		log.info("Total users in database table: " + returnValue.size());

		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity deleteUser(@PathVariable("userId") String userId,
			@RequestHeader("Authorization") String authorization) {

		usersService.deleteUser(userId, authorization);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/status/check")
	public String status(@RequestHeader("Authorization") String authorizationHeader) {
		String returnValue = "Working on port " + port + " with token " + token + ".\nToken from environment "
				+ environment.getProperty("token.secret") + "\nAuthorizationHeader = " + authorizationHeader
				+ ".\nMy application environment = " + environment.getProperty("myapplication.environment");
		log.info(returnValue);
		return returnValue;
	}

	@GetMapping("/ip")
	public String getIp() {
		String returnValue;

		try {
			InetAddress ipAddr = InetAddress.getLocalHost();
			returnValue = ipAddr.getHostAddress();
		} catch (UnknownHostException ex) {
			returnValue = ex.getLocalizedMessage();
		}

		return returnValue;
	}

}
