package com.example.blog.auth;

import com.example.blog.auth.dto.AuthRequest;
import com.example.blog.auth.dto.AuthResponse;
import com.example.blog.security.JwtService;
import com.example.blog.user.User;
import com.example.blog.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthService(UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager,
			JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	public AuthResponse signup(AuthRequest request) {
		if (userRepository.findByUsername(request.username()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
		}

		User user = User.builder()
				.username(request.username())
				.password(passwordEncoder.encode(request.password()))
				.build();
		userRepository.save(user);

		String token = jwtService.generateToken(
				new org.springframework.security.core.userdetails.User(
						user.getUsername(),
						user.getPassword(),
						java.util.List.of()));

		return new AuthResponse(user.getId(), token, user.getUsername());
	}

	public AuthResponse login(AuthRequest request) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.username(), request.password()));
		} catch (org.springframework.security.core.AuthenticationException ex) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
		}

		User user = userRepository.findByUsername(request.username())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));
		String token = jwtService.generateToken(
				new org.springframework.security.core.userdetails.User(
						user.getUsername(),
						user.getPassword(),
						java.util.List.of()));

		return new AuthResponse(user.getId(), token, user.getUsername());
	}
}