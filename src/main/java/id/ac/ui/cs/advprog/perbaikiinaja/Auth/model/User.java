// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/model/User.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
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

    /** main 6-arg constructor **/
    public User(String id,
                String fullName,
                String email,
                String phone,
                String password,
                String address) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
    }

    /** test-friendly 7-arg overload (last param ignored) **/
    public User(String id,
                String fullName,
                String email,
                String phone,
                String password,
                String address,
                String unused) {
        this(id, fullName, email, phone, password, address);
    }
}