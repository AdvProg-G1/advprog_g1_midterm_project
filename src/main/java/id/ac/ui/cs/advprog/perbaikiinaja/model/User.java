// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/model/User.java
package id.ac.ui.cs.advprog.perbaikiinaja.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber; // Can be updated
    private String password;    // Can be updated
    private String address;     // Can be updated
}