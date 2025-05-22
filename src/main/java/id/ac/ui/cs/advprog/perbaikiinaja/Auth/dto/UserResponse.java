package id.ac.ui.cs.advprog.perbaikiinaja.Auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String id;
    private String username;
    private String fullName;
    private String email;
    private String role;
}
