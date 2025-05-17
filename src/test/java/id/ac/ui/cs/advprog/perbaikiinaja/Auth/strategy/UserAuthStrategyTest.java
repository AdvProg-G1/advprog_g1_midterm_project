// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/strategy/UserAuthStrategyTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth.strategy;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.UserAuthStrategy;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.dto.RegisterUserRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.Role;
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
    private BCryptPasswordEncoder passwordEncoder;
    private UserAuthStrategy userAuthStrategy;

    @BeforeEach
    void setUp() {
        userRepository  = mock(UserRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userAuthStrategy = new UserAuthStrategy(userRepository, passwordEncoder);
    }

    /* ─────────────── Login ─────────────── */

    @Test
    void loginSuccess() {
        String username = "shopper01";
        String raw      = "secret123";
        String hash     = passwordEncoder.encode(raw);

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(hash);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(mockUser));

        User result = userAuthStrategy.login(username, raw);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void loginFailsWithInvalidPassword() {
        String username = "shopper01";
        String good = "correctpass";
        String bad  = "wrongpass";
        String hash = passwordEncoder.encode(good);

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(hash);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(mockUser));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userAuthStrategy.login(username, bad));
        assertEquals("Invalid credentials", ex.getMessage());
    }

    @Test
    void loginFailsWhenUserNotFound() {
        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userAuthStrategy.login("missing", "any"));
        assertEquals("User not found", ex.getMessage());
    }

    /* ─────────────── Register ─────────────── */

    @Test
    void registerUserSuccessfully() {
        RegisterUserRequest req = new RegisterUserRequest();
        req.setUsername("newuser01");
        req.setFullName("Test User");
        req.setEmail("user@mail.com");
        req.setPassword("mypassword");
        req.setPhone("08123456789");
        req.setAddress("Bandung");

        when(userRepository.findByUsername("newuser01")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.empty());

        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> {
                    User u = inv.getArgument(0);
                    u.setId("generated-id");
                    return u;
                });

        User result = userAuthStrategy.register(req);

        assertEquals("generated-id", result.getId());
        assertEquals(Role.USER.getAuthority(), result.getRole());
        assertTrue(passwordEncoder.matches("mypassword", result.getPassword()));
    }

    @Test
    void registerFailsIfUsernameTaken() {
        RegisterUserRequest req = new RegisterUserRequest();
        req.setUsername("taken");
        req.setFullName("Test User");
        req.setEmail("free@mail.com");
        req.setPassword("pw");

        when(userRepository.findByUsername("taken"))
                .thenReturn(Optional.of(new User()));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userAuthStrategy.register(req));
        assertEquals("Username already taken", ex.getMessage());
    }

    @Test
    void registerFailsIfEmailAlreadyRegistered() {
        RegisterUserRequest req = new RegisterUserRequest();
        req.setUsername("freeuser");
        req.setFullName("Test User");
        req.setEmail("existing@mail.com");
        req.setPassword("pw");

        when(userRepository.findByUsername("freeuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("existing@mail.com"))
                .thenReturn(Optional.of(new User()));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userAuthStrategy.register(req));
        assertEquals("Email already registered", ex.getMessage());
    }
}