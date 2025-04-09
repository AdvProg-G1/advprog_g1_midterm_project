package id.ac.ui.cs.advprog.perbaikiinaja.Auth.dto;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String address;
}
