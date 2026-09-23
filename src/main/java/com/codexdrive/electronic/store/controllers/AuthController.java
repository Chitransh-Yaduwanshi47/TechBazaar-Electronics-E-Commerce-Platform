package com.codexdrive.electronic.store.controllers;

import com.codexdrive.electronic.store.dtos.JwtRequest;
import com.codexdrive.electronic.store.dtos.JwtResponse;
import com.codexdrive.electronic.store.dtos.UserDto;
import com.codexdrive.electronic.store.exceptions.BadApiRequestException;
import com.codexdrive.electronic.store.security.JwtHelper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.User;
import com.codexdrive.electronic.store.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    //method to generate token:

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ModelMapper modelMapper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        logger.info("Username: {}", request.getEmail());
        logger.info("Password: {}", request.getPassword());

        this.doAuthenticate(request.getEmail(), request.getPassword());

        User user = (User) userDetailsService.loadUserByUsername(request.getEmail());

        // .. generate token..
        String token = jwtHelper.generateToken(user);
        // send karna hai response

        JwtResponse jwtResponse = JwtResponse.builder().jwtToken(token).user(modelMapper.map(user, UserDto.class)).build();

        return ResponseEntity.ok(jwtResponse);
    }

    private void doAuthenticate(String email, String password) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid Username and Password !!");
        }
    }

}