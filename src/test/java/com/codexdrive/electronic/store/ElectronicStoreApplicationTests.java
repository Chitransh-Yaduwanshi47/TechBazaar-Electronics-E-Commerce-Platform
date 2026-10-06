package com.codexdrive.electronic.store;

import com.codexdrive.electronic.store.entities.User;
import com.codexdrive.electronic.store.repositories.UserRepository;
import com.codexdrive.electronic.store.security.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class ElectronicStoreApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtHelper jwtHelper;

	@Test
	void contextLoads() {
	}

	@Test
	void testToken(){

		User user = userRepository.findByEmail("chitranshu2411@gmail.com").get();

		String token= jwtHelper.generateToken(user);
		System.out.println(token);

		System.out.println("getting username from token");

		System.out.println(jwtHelper.getUsernameFromToken(token));

		System.out.println("Token expired: " );
		System.out.println(jwtHelper.isTokenExpired(token));
	}

}