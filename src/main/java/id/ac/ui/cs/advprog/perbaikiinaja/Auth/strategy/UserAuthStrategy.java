package id.ac.ui.cs.advprog.perbaikiinaja.Auth.strategy;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.dto.RegisterUserRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserAuthStrategy implements AuthStrategy {

    private final UserRepository userRepository;

    @Override
    public Object login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password)) // replace with BCrypt in prod
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

    @Override
    public Object register(Object request) {
        RegisterUserRequest req = (RegisterUserRequest) request;
        User user = new User(
                null,
                req.getFullName(),
                req.getEmail(),
                req.getPhone(),
                req.getPassword(),
                req.getAddress()
        );
        return userRepository.save(user);
    }
}
