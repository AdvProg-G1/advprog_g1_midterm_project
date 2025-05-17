// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/strategy/UserAuthStrategy.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth.strategy;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.dto.RegisterUserRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.Role;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAuthStrategy implements AuthStrategy {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return user;
    }

    @Override
    public User register(RegisterUserRequest r) {
        if (userRepository.findByUsername(r.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }
        if (userRepository.findByEmail(r.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        User u = new User();
        u.setId(UUID.randomUUID().toString());
        u.setUsername(r.getUsername());
        u.setFullName(r.getFullName());
        u.setEmail(r.getEmail());
        u.setPassword(passwordEncoder.encode(r.getPassword()));
        u.setPhone(r.getPhone());
        u.setAddress(r.getAddress());
        u.setRoleEnum(Role.CUSTOMER);           // default role
        return userRepository.save(u);
    }
}