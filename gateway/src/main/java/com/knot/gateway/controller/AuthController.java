package com.knot.gateway.controller;

import com.knot.gateway.model.User;
import com.knot.gateway.model.request.AuthenticationRequest;
import com.knot.gateway.model.request.UserDTO;
import com.knot.gateway.model.response.AuthenticationResponse;
import com.knot.gateway.model.response.ValidationResponse;
import com.knot.gateway.service.JwtService;
import com.knot.gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        final String jwt = jwtService.createJwtToken(authenticationRequest);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO user) {
        ValidationResponse validationResponse = userService.validateUserSignUp(user);
        if (!validationResponse.isValid()) {
            return ResponseEntity.badRequest().body(validationResponse.getMessage());
        } else {
            userService.save(user);
            return ResponseEntity.ok("User registered successfully.");
        }
    }
}
