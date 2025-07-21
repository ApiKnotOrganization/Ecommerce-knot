package com.knot.gateway.service;

import com.knot.gateway.model.User;
import com.knot.gateway.model.request.UserDTO;
import com.knot.gateway.model.response.ValidationResponse;
import com.knot.gateway.repository.UserRepository;
import com.knot.gateway.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(UserDTO user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User appUser = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .first_name(user.getFirst_name())
                .last_name(user.getLast_name())
                .email(user.getEmail())
                .phone_country_code(user.getPhone_country_code())
                .phone_number(user.getPhone_number())
                .build();
        return userRepository.save(appUser);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public ValidationResponse validateUserSignUp(UserDTO user) {
        if (user == null || user.getUsername() == null || user.getEmail() == null)
            throw new NullPointerException("User, email and username cannot be null.");

        Optional<User> existingUserByUsername = userRepository.findByUsername(user.getUsername());
        if (existingUserByUsername.isPresent()) {
            return new ValidationResponse(false, "Username is already taken.");
        }

        // Validate email format
        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            return new ValidationResponse(false, "Invalid email format.");
        }

        Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserByEmail.isPresent()) {
            return new ValidationResponse(false, "Email is already taken.");
        }

        return new ValidationResponse(true, "User is valid.");
    }
}
