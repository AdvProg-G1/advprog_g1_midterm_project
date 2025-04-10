//src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/strategy/UserAuthStrategy.java
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
                .filter(user -> user.getPassword().equals(password)) // Replace with BCrypt in production!
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

    @Override
    public Object register(Object request) {
        RegisterUserRequest req = (RegisterUserRequest) request;

        User user = new User();
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword()); // Hash in production
        user.setPhone(req.getPhone());
        user.setAddress(req.getAddress());

        return userRepository.save(user);
    }
}
