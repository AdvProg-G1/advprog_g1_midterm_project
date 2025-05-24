// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/model/User.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;
    private String address;

    @Column(nullable = false)
    private String role; // "CUSTOMER", etc.

    @Column(nullable = true, name = "total_salary")
    private Integer totalSalary;

    @Column(nullable = true, name = "total_work")
    private Integer totalWork;

    // Role helpers
    public Role getRoleEnum()           { return Role.fromAuthority(this.role); }
    public void setRoleEnum(Role role ) { this.role = role.getAuthority(); }

    // UserDetails
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return role == null || role.isBlank()
                ? Collections.emptyList()
                : List.of(new SimpleGrantedAuthority(role));
    }
    @Override public String getPassword()             { return password; }
    @Override public String getUsername()             { return username; }
    @Override public boolean isAccountNonExpired()    { return true; }
    @Override public boolean isAccountNonLocked()     { return true; }
    @Override public boolean isCredentialsNonExpired(){ return true; }
    @Override public boolean isEnabled()              { return true; }
}