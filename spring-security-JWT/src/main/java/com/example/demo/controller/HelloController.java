package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.JwtUtil;
import com.example.demo.model.AuthenticationRequest;
import com.example.demo.model.AuthenticationResponse;
import com.example.demo.services.MyUserDetailsService;

@RestController
public class HelloController {
	
	@Autowired
	private AuthenticationManager authenticateManager;
	
	@Autowired
	private MyUserDetailsService userdetailservice;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	
	@GetMapping("/hello")
	public String hello() {
		return "<h1>Hello world</h1>";
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticateRequest) throws Exception {
		try {
		 authenticateManager.authenticate(new UsernamePasswordAuthenticationToken(authenticateRequest.getUsername(), authenticateRequest.getPassword()));
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username and password");
		}
		
		UserDetails userDetails = userdetailservice.loadUserByUsername(authenticateRequest.getUsername());
		
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
