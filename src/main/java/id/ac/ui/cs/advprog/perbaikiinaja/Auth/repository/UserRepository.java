package id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
