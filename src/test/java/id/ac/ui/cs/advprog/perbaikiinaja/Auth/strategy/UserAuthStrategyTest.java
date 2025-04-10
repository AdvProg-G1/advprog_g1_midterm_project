// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/strategy/UserAuthStrategyTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth.strategy;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.dto.RegisterUserRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserAuthStrategyTest {

    private UserRepository userRepository;
    private UserAuthStrategy userAuthStrategy;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userAuthStrategy = new UserAuthStrategy(userRepository);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void testLoginSuccess() {
        // Prepare a user with an encoded password
        String plainPassword = "secret123";
        String encodedPassword = passwordEncoder.encode(plainPassword);
        User mockUser = new User();
        mockUser.setEmail("user@mail.com");
        mockUser.setPassword(encodedPassword);

        when(userRepository.findByEmail("user@mail.com"))
                .thenReturn(Optional.of(mockUser));

        User result = userAuthStrategy.login("user@mail.com", plainPassword);

        assertNotNull(result);
        assertEquals(mockUser.getEmail(), result.getEmail());
    }

    @Test
    void testLoginFailsWithInvalidPassword() {
        String correctPassword = "correctpass";
        String wrongPassword = "wrongpass";
        String encodedPassword = passwordEncoder.encode(correctPassword);
        User mockUser = new User();
        mockUser.setEmail("user@mail.com");
        mockUser.setPassword(encodedPassword);

        when(userRepository.findByEmail("user@mail.com"))
                .thenReturn(Optional.of(mockUser));

        Exception exception = assertThrows(RuntimeException.class, () ->
                userAuthStrategy.login("user@mail.com", wrongPassword)
        );
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void testLoginFailsWhenUserNotFound() {
        when(userRepository.findByEmail("missing@mail.com"))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                userAuthStrategy.login("missing@mail.com", "any")
        );
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testRegisterUserSuccessfully() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setFullName("Test User");
        request.setEmail("user@mail.com");
        request.setPassword("mypassword");
        request.setPhone("08123456789");
        request.setAddress("Bandung");

        // Simulate that no user exists with this email
        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.empty());

        // Mimic the repository's save behavior
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User argUser = invocation.getArgument(0);
            argUser.setId("generated-id");
            return argUser;
        });

        User result = userAuthStrategy.register(request);

        assertNotNull(result.getId());
        assertEquals("user@mail.com", result.getEmail());
        // Ensure that the plain password is not stored
        assertNotEquals("mypassword", result.getPassword());
        // Confirm the encoded password matches the original plain password
        assertTrue(passwordEncoder.matches("mypassword", result.getPassword()));
    }

    @Test
    void testRegisterFailsIfUserAlreadyExists() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setFullName("Test User");
        request.setEmail("existing@mail.com");
        request.setPassword("mypassword");
        request.setPhone("08123456789");
        request.setAddress("Bandung");

        User existingUser = new User();
        existingUser.setEmail("existing@mail.com");

        when(userRepository.findByEmail("existing@mail.com")).thenReturn(Optional.of(existingUser));

        Exception exception = assertThrows(RuntimeException.class, () ->
                userAuthStrategy.register(request)
        );
        assertEquals("User already exists", exception.getMessage());
    }
}