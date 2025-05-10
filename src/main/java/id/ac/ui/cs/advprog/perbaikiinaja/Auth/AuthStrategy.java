// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/strategy/AuthStrategy.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.dto.RegisterUserRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;

public interface AuthStrategy {
    User login(String email, String password);
    User register(RegisterUserRequest request);
}