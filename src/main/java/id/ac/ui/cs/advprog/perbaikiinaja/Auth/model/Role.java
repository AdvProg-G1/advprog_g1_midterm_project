// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/model/Role.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth.model;

public enum Role {
    USER("USER"),
    TECHNICIAN("TECHNICIAN"),
    ADMIN("ADMIN");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public static Role fromAuthority(String authority) {
        for (Role r : values()) {
            if (r.authority.equalsIgnoreCase(authority)) return r;
        }
        throw new IllegalArgumentException("Unknown role: " + authority);
    }
}