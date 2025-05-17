package id.ac.ui.cs.advprog.perbaikiinaja.Auth.strategy;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.UserAuthStrategy;
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
        userRepository  = mock(UserRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        // now using the two-arg constructor:
        userAuthStrategy = new UserAuthStrategy(userRepository, passwordEncoder);
    }

    @Test
    void testLoginSuccess() {
        String plain = "secret123";
        String hash  = passwordEncoder.encode(plain);

        User mockUser = new User();
        mockUser.setEmail("user@mail.com");
        mockUser.setPassword(hash);

        when(userRepository.findByEmail("user@mail.com"))
                .thenReturn(Optional.of(mockUser));

        User result = userAuthStrategy.login("user@mail.com", plain);

        assertNotNull(result);
        assertEquals(mockUser.getEmail(), result.getEmail());
    }

    @Test
    void testLoginFailsWithInvalidPassword() {
        String good = "correctpass", bad = "wrongpass";
        String hash = passwordEncoder.encode(good);

        User mockUser = new User();
        mockUser.setEmail("user@mail.com");
        mockUser.setPassword(hash);

        when(userRepository.findByEmail("user@mail.com"))
                .thenReturn(Optional.of(mockUser));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userAuthStrategy.login("user@mail.com", bad)
        );
        assertEquals("Invalid credentials", ex.getMessage());
    }

    @Test
    void testLoginFailsWhenUserNotFound() {
        when(userRepository.findByEmail("missing@mail.com"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userAuthStrategy.login("missing@mail.com", "any")
        );
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void testRegisterUserSuccessfully() {
        // prepare request
        RegisterUserRequest req = new RegisterUserRequest();
        req.setFullName("Test User");
        req.setEmail("user@mail.com");
        req.setPassword("mypassword");
        req.setPhone("08123456789");
        req.setAddress("Bandung");

        // no existing user
        when(userRepository.findByEmail("user@mail.com"))
                .thenReturn(Optional.empty());

        // stub save(...) to return the same User, but also set id & email
        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> {
                    User u = inv.getArgument(0);
                    u.setId("generated-id");
                    // ensure email is populated for the test
                    u.setEmail(req.getEmail());
                    return u;
                });

        User result = userAuthStrategy.register(req);

        assertNotNull(result.getId());
        assertEquals("user@mail.com", result.getEmail());
        assertNotEquals("mypassword", result.getPassword());
        assertTrue(passwordEncoder.matches("mypassword", result.getPassword()));
    }

    @Test
    void testRegisterFailsIfUserAlreadyExists() {
        RegisterUserRequest req = new RegisterUserRequest();
        req.setFullName("Test User");
        req.setEmail("existing@mail.com");
        req.setPassword("mypassword");
        req.setPhone("08123456789");
        req.setAddress("Bandung");

        User existing = new User();
        existing.setEmail("existing@mail.com");

        when(userRepository.findByEmail("existing@mail.com"))
                .thenReturn(Optional.of(existing));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userAuthStrategy.register(req)
        );
        assertEquals("User already exists", ex.getMessage());
    }
}
