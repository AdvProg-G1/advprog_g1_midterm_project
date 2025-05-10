package id.ac.ui.cs.advprog.perbaikiinaja.Auth.model;

public enum Role {
    CUSTOMER("ROLE_CUSTOMER"),
    TECHNICIAN("ROLE_TECHNICIAN"),
    ADMIN("ROLE_ADMIN");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public static Role fromAuthority(String authority) {
        for (Role role : Role.values()) {
            if (role.getAuthority().equalsIgnoreCase(authority)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown authority: " + authority);
    }
}