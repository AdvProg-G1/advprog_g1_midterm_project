// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/dto/RegisterUserRequest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String phone;

    @NotBlank(message = "Password is required")
    private String password;

    private String address;
}