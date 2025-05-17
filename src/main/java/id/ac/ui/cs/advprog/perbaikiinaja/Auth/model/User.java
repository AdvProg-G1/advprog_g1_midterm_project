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

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;
    private String address;

    @Column(nullable = false)
    private String role; // stored in DB as string: "ROLE_CUSTOMER", etc.

    // Role Authorization Methods

    public Role getRoleEnum() {
        return Role.fromAuthority(this.role);
    }

    public void setRoleEnum(Role roleEnum) {
        this.role = roleEnum.getAuthority();
    }

    // UserDetails methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null || role.isBlank()) {
            return Collections.emptyList();
        }

        // remove ROLE prefix if present
        String formattedRole = role.startsWith("ROLE_") ? role.substring(5) : role;
        return List.of(new SimpleGrantedAuthority(formattedRole));
    }


    // password getter is provided by Lombok

    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}