//src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/model/User.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String phone;
    private String password;
    private String address;
}
