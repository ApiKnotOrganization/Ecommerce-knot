package com.knot.gateway.controller;

import com.knot.gateway.model.request.AuthenticationRequest;
import com.knot.gateway.model.dto.UserDTO;
import com.knot.gateway.model.response.AuthenticationResponse;
import com.knot.gateway.model.response.ValidationResponse;
import com.knot.gateway.service.JwtService;
import com.knot.gateway.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                                       HttpServletResponse response) throws Exception {
        final String jwt = jwtService.createJwtToken(authenticationRequest);
        ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", jwt)
                .httpOnly(true)
                .path("/")
                .maxAge(3600)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO user) {
        ValidationResponse validationResponse = userService.validateUserSignUp(user);
        if (!validationResponse.isValid()) {
            return ResponseEntity.badRequest().body(validationResponse.getMessage());
        } else {
            UserDTO registeredUser = userService.save(user);
            return ResponseEntity.ok(registeredUser);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", null)
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        return ResponseEntity.ok().build();
    }
}
