//src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/strategy/UserAuthStrategyTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth.strategy;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.dto.RegisterUserRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAuthStrategyTest {

    private UserRepository userRepository;
    private UserAuthStrategy userAuthStrategy;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userAuthStrategy = new UserAuthStrategy(userRepository);
    }

    @Test
    void testLoginSuccess() {
        User mockUser = new User();
        mockUser.setEmail("user@mail.com");
        mockUser.setPassword("secret123");

        when(userRepository.findByEmail("user@mail.com"))
                .thenReturn(Optional.of(mockUser));

        Object result = userAuthStrategy.login("user@mail.com", "secret123");

        assertNotNull(result);
        assertEquals(mockUser, result);
    }

    @Test
    void testLoginFailsWithInvalidPassword() {
        User mockUser = new User();
        mockUser.setEmail("user@mail.com");
        mockUser.setPassword("correctpass");

        when(userRepository.findByEmail("user@mail.com"))
                .thenReturn(Optional.of(mockUser));

        assertThrows(RuntimeException.class, () -> {
            userAuthStrategy.login("user@mail.com", "wrongpass");
        });
    }

    @Test
    void testLoginFailsWhenUserNotFound() {
        when(userRepository.findByEmail("missing@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userAuthStrategy.login("missing@mail.com", "any");
        });
    }

    @Test
    void testRegisterUserSuccessfully() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setFullName("Test User");
        request.setEmail("user@mail.com");
        request.setPassword("mypassword");
        request.setPhone("08123456789");
        request.setAddress("Bandung");

        User savedUser = new User();
        savedUser.setEmail("user@mail.com");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        Object result = userAuthStrategy.register(request);

        assertNotNull(result);
        assertTrue(result instanceof User);
        assertEquals("user@mail.com", ((User) result).getEmail());
    }
}
