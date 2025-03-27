// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/model/Admin.java
package id.ac.ui.cs.advprog.perbaikiinaja.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;
}