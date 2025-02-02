package com.example.cassandra.controller;

import java.util.Map;

import com.example.cassandra.repository.model.User;
import com.example.cassandra.service.PasswordService;
import com.example.cassandra.service.UserService;
import com.example.cassandra.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController
{
	private final UserService userService;
	private final JwtUtil jwtUtil;
	private final PasswordService passwordService;


	public UserController(UserService userService, JwtUtil jwtUtil, PasswordService passwordService) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
		this.passwordService = passwordService;
	}

	@PostMapping("/register")
	public Map<String, String> register(@RequestBody Map<String, String> request) {
		User user = userService.registerUser(request.get("username"), request.get("password"));
		return Map.of("message", "User registered successfully", "userId", user.getId().toString());
	}


	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
		User foundUser = userService.findByUsername(request.get("username"));
		if (!passwordService.matches(request.get("password"), foundUser.getPassword())) {
			return ResponseEntity.status(401).body("Invalid credentials");
		}
		String token = jwtUtil.generateToken(foundUser.getUsername());
		return ResponseEntity.ok(token);
	}

}
