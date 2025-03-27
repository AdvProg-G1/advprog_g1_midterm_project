// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/model/Technician.java
package id.ac.ui.cs.advprog.perbaikiinaja.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Technician {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;
    private String experience;  // Optional; can be updated
    private String address;     // Can be updated
    private int totalJobsCompleted = 0; // Updated only after job completion
    private double totalEarnings = 0.0;   // Updated only after job completion
}