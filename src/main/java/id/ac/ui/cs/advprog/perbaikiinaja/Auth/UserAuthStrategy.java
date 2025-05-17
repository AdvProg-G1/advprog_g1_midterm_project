// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/UserAuthStrategy.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth;

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

    private final UserRepository repo;
    private final BCryptPasswordEncoder enc;

    // ─── Helper ──────────────────────────────────────────────────────────
    private static boolean looksBCrypt(String s) {
        return s != null && s.startsWith("$2");
    }

    @Override
    public User login(String username, String raw) {

        User u = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean ok;
        if (looksBCrypt(u.getPassword())) {          // normal case
            ok = enc.matches(raw, u.getPassword());
        } else {                                     // legacy plain-text row
            ok = raw.equals(u.getPassword());
            if (ok) {                                // upgrade to BCrypt
                u.setPassword(enc.encode(raw));
                repo.save(u);
            }
        }

        if (!ok) throw new RuntimeException("Invalid credentials");
        return u;
    }

    @Override
    public User register(RegisterUserRequest r) {
        if (repo.findByUsername(r.getUsername()).isPresent())
            throw new RuntimeException("Username already taken");
        if (repo.findByEmail(r.getEmail()).isPresent())
            throw new RuntimeException("Email already registered");

        User u = new User();
        u.setId(UUID.randomUUID().toString());
        u.setUsername(r.getUsername());
        u.setFullName(r.getFullName());
        u.setEmail(r.getEmail());
        u.setPassword(enc.encode(r.getPassword()));   // always BCrypt
        u.setPhone(r.getPhone());
        u.setAddress(r.getAddress());
        u.setRoleEnum(Role.USER);
        return repo.save(u);
    }
}