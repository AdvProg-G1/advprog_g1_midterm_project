// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/AuthStrategy.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.dto.RegisterUserRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.dto.UserResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;

import java.util.List;

public interface AuthStrategy {
    User login(String username, String password);
    User register(RegisterUserRequest request);
    List<UserResponse> getAllTechnicians();

}