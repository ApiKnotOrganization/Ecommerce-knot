package com.knot.gateway.service;

import com.knot.gateway.model.User;
import com.knot.gateway.model.dto.UserDTO;
import com.knot.gateway.model.response.ValidationResponse;
import com.knot.gateway.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService; // Assuming the method is in UserService class

    private UserDTO validUser;
    private User existingUser;

    @BeforeEach
    void setUp() {
        validUser = new UserDTO();
        validUser.setUsername("newuser");
        validUser.setEmail("newuser@example.com");

        existingUser = new User();
        existingUser.setUsername("existinguser");
        existingUser.setEmail("existing@example.com");
    }

    @Test
    void validateUserSignUp_ConsecutiveDotsInEmail_ReturnsFalseResponse() {
        // Test specifically for consecutive dots issue
        String[] emailsWithConsecutiveDots = {
                "email@domain..com",
                "email..test@domain.com",
                "email@sub..domain.com",
                "email@domain.com.."
        };

        for (String email : emailsWithConsecutiveDots) {
            // Given
            validUser.setEmail(email);
            when(userRepository.findByUsername(validUser.getUsername()))
                    .thenReturn(Optional.empty());

            // When
            ValidationResponse response = userService.validateUserSignUp(validUser);

            // Then
            assertFalse(response.isValid(),
                    "Email with consecutive dots should be invalid: " + email);
            assertEquals("Invalid email format.", response.getMessage());

            // Reset mocks for next iteration
            reset(userRepository);
        }
    }

    @Test
    void validateUserSignUp_ValidUser_ReturnsSuccessResponse() {
        // Given
        when(userRepository.findByUsername(validUser.getUsername()))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmail(validUser.getEmail()))
                .thenReturn(Optional.empty());

        // When
        ValidationResponse response = userService.validateUserSignUp(validUser);

        // Then
        assertTrue(response.isValid());
        assertEquals("User is valid.", response.getMessage());
        verify(userRepository).findByUsername(validUser.getUsername());
        verify(userRepository).findByEmail(validUser.getEmail());
    }

    @Test
    void validateUserSignUp_ExistingUsername_ReturnsFalseResponse() {
        // Given
        when(userRepository.findByUsername(validUser.getUsername()))
                .thenReturn(Optional.of(existingUser));

        // When
        ValidationResponse response = userService.validateUserSignUp(validUser);

        // Then
        assertFalse(response.isValid());
        assertEquals("Username is already taken.", response.getMessage());
        verify(userRepository).findByUsername(validUser.getUsername());
        // Email check should not be called since username validation failed first
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void validateUserSignUp_ExistingEmail_ReturnsFalseResponse() {
        // Given
        when(userRepository.findByUsername(validUser.getUsername()))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmail(validUser.getEmail()))
                .thenReturn(Optional.of(existingUser));

        // When
        ValidationResponse response = userService.validateUserSignUp(validUser);

        // Then
        assertFalse(response.isValid());
        assertEquals("Email is already taken.", response.getMessage());
        verify(userRepository).findByUsername(validUser.getUsername());
        verify(userRepository).findByEmail(validUser.getEmail());
    }

    @Test
    void validateUserSignUp_ExistingUsernameAndEmail_ReturnsUsernameError() {
        // Given - both username and email exist
        when(userRepository.findByUsername(validUser.getUsername()))
                .thenReturn(Optional.of(existingUser));

        // When
        ValidationResponse response = userService.validateUserSignUp(validUser);

        // Then
        // Should return username error since it's checked first
        assertFalse(response.isValid());
        assertEquals("Username is already taken.", response.getMessage());
        verify(userRepository).findByUsername(validUser.getUsername());
        // Email check should not be called since username validation failed first
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void validateUserSignUp_NullUser_ThrowsException() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            userService.validateUserSignUp(null);
        });

        // Verify no repository calls were made
        verifyNoInteractions(userRepository);
    }

    @Test
    void validateUserSignUp_NullUsername_ThrowsException() {
        // Given
        validUser.setUsername(null);

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            userService.validateUserSignUp(validUser);
        });

        // Verify no repository calls were made
        verifyNoInteractions(userRepository);
    }

    @Test
    void validateUserSignUp_NullEmail_ThrowsException() {
        // Given
        validUser.setEmail(null);
        validUser.setUsername("newuser");

        // When
        assertThrows(NullPointerException.class, () -> {
            userService.validateUserSignUp(validUser);
        });

        verifyNoInteractions(userRepository);
    }

    @Test
    void validateUserSignUp_EmptyUsername_ValidatesSuccessfully() {
        // Given
        validUser.setUsername("");
        when(userRepository.findByUsername(""))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmail(validUser.getEmail()))
                .thenReturn(Optional.empty());

        // When
        ValidationResponse response = userService.validateUserSignUp(validUser);

        // Then
        assertTrue(response.isValid());
        assertEquals("User is valid.", response.getMessage());
        verify(userRepository).findByUsername("");
        verify(userRepository).findByEmail(validUser.getEmail());
    }

    @Test
    void validateUserSignUp_ExistingEmptyUsername_ReturnsFalseResponse() {
        // Given
        validUser.setUsername("");
        when(userRepository.findByUsername(""))
                .thenReturn(Optional.of(existingUser));

        // When
        ValidationResponse response = userService.validateUserSignUp(validUser);

        // Then
        assertFalse(response.isValid());
        assertEquals("Username is already taken.", response.getMessage());
        verify(userRepository).findByUsername("");
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void validateUserSignUp_CaseVariation_TreatedAsDifferent() {
        // Given - testing if the method treats case variations as different
        validUser.setUsername("TestUser");
        validUser.setEmail("TEST@EXAMPLE.COM");

        User existingUserLowerCase = new User();
        existingUserLowerCase.setUsername("testuser");
        existingUserLowerCase.setEmail("test@example.com");

        when(userRepository.findByUsername("TestUser"))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmail("TEST@EXAMPLE.COM"))
                .thenReturn(Optional.empty());

        // When
        ValidationResponse response = userService.validateUserSignUp(validUser);

        // Then
        assertTrue(response.isValid());
        assertEquals("User is valid.", response.getMessage());
        verify(userRepository).findByUsername("TestUser");
        verify(userRepository).findByEmail("TEST@EXAMPLE.COM");
    }

    @Test
    void validateUserSignUp_WhitespaceInInputs_PassedAsIs() {
        // Given
        validUser.setUsername("  spaced username  ");
        validUser.setEmail("  spaced@email.com  ");

        when(userRepository.findByUsername("  spaced username  "))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmail("  spaced@email.com  "))
                .thenReturn(Optional.empty());

        // When
        ValidationResponse response = userService.validateUserSignUp(validUser);

        // Then
        assertTrue(response.isValid());
        assertEquals("User is valid.", response.getMessage());
        verify(userRepository).findByUsername("  spaced username  ");
        verify(userRepository).findByEmail("  spaced@email.com  ");
    }

    // Email Format Validation Tests
    @Test
    void validateUserSignUp_InvalidEmailFormat_ReturnsFalseResponse() {
        // Given
        validUser.setEmail("invalid-email");
        when(userRepository.findByUsername(validUser.getUsername()))
                .thenReturn(Optional.empty());

        // When
        ValidationResponse response = userService.validateUserSignUp(validUser);

        // Then
        assertFalse(response.isValid());
        assertEquals("Invalid email format.", response.getMessage());
        verify(userRepository).findByUsername(validUser.getUsername());
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void validateUserSignUp_EmailWithoutAtSymbol_ReturnsFalseResponse() {
        // Given
        validUser.setEmail("emailwithoutat.com");
        when(userRepository.findByUsername(validUser.getUsername()))
                .thenReturn(Optional.empty());

        // When
        ValidationResponse response = userService.validateUserSignUp(validUser);

        // Then
        assertFalse(response.isValid());
        assertEquals("Invalid email format.", response.getMessage());
        verify(userRepository).findByUsername(validUser.getUsername());
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void validateUserSignUp_EmailWithoutDomain_ReturnsFalseResponse() {
        // Given
        validUser.setEmail("email@");
        when(userRepository.findByUsername(validUser.getUsername()))
                .thenReturn(Optional.empty());

        // When
        ValidationResponse response = userService.validateUserSignUp(validUser);

        // Then
        assertFalse(response.isValid());
        assertEquals("Invalid email format.", response.getMessage());
        verify(userRepository).findByUsername(validUser.getUsername());
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void validateUserSignUp_EmailWithoutLocalPart_ReturnsFalseResponse() {
        // Given
        validUser.setEmail("@domain.com");
        when(userRepository.findByUsername(validUser.getUsername()))
                .thenReturn(Optional.empty());

        // When
        ValidationResponse response = userService.validateUserSignUp(validUser);

        // Then
        assertFalse(response.isValid());
        assertEquals("Invalid email format.", response.getMessage());
        verify(userRepository).findByUsername(validUser.getUsername());
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void validateUserSignUp_EmailWithMultipleAtSymbols_ReturnsFalseResponse() {
        // Given
        validUser.setEmail("email@domain@.com");
        when(userRepository.findByUsername(validUser.getUsername()))
                .thenReturn(Optional.empty());

        // When
        ValidationResponse response = userService.validateUserSignUp(validUser);

        // Then
        assertFalse(response.isValid());
        assertEquals("Invalid email format.", response.getMessage());
        verify(userRepository).findByUsername(validUser.getUsername());
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void validateUserSignUp_EmptyEmailAfterUsernameValidation_ReturnsInvalidEmailFormat() {
        // Given
        validUser.setEmail("");
        when(userRepository.findByUsername(validUser.getUsername()))
                .thenReturn(Optional.empty());

        // When
        ValidationResponse response = userService.validateUserSignUp(validUser);

        // Then
        assertFalse(response.isValid());
        assertEquals("Invalid email format.", response.getMessage());
        verify(userRepository).findByUsername(validUser.getUsername());
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void validateUserSignUp_ValidEmailFormats_ReturnsSuccessResponse() {
        // Test various valid email formats
        String[] validEmails = {
                "test@example.com",
                "user.name@domain.co.uk",
                "user+tag@example.org",
                "firstname.lastname@company.com",
                "user123@test-domain.com",
                "a@b.co"
        };

        for (String email : validEmails) {
            // Given
            validUser.setEmail(email);
            when(userRepository.findByUsername(validUser.getUsername()))
                    .thenReturn(Optional.empty());
            when(userRepository.findByEmail(email))
                    .thenReturn(Optional.empty());

            // When
            ValidationResponse response = userService.validateUserSignUp(validUser);

            // Then
            assertTrue(response.isValid(),
                    "Email should be valid: " + email);
            assertEquals("User is valid.", response.getMessage());

            // Reset mocks for next iteration
            reset(userRepository);
        }
    }

    @Test
    void validateUserSignUp_InvalidEmailFormats_ReturnsFalseResponse() {
        // Test various invalid email formats
        String[] invalidEmails = {
                "",
                "plainaddress",
                "@missinglocal.com",
                "missing@.com",
                "missing@domain",
                "spaces @domain.com",
                "email@domain..com",
                "email@domain.",
                ".email@domain.com",
                "email.@domain.com",
                "email@-domain.com",
                "email@domain-.com"
        };

        for (String email : invalidEmails) {
            // Given
            validUser.setEmail(email);
            when(userRepository.findByUsername(validUser.getUsername()))
                    .thenReturn(Optional.empty());

            // When
            ValidationResponse response = userService.validateUserSignUp(validUser);

            // Then
            assertFalse(response.isValid(),
                    "Email should be invalid: " + email);
            assertEquals("Invalid email format.", response.getMessage());

            // Reset mocks for next iteration
            reset(userRepository);
        }
    }

    @Test
    void validateUserSignUp_ValidEmailButExistsInDatabase_ReturnsEmailTakenError() {
        // Given
        validUser.setEmail("valid@example.com");
        when(userRepository.findByUsername(validUser.getUsername()))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmail("valid@example.com"))
                .thenReturn(Optional.of(existingUser));

        // When
        ValidationResponse response = userService.validateUserSignUp(validUser);

        // Then
        assertFalse(response.isValid());
        assertEquals("Email is already taken.", response.getMessage());
        verify(userRepository).findByUsername(validUser.getUsername());
        verify(userRepository).findByEmail("valid@example.com");
    }
}
