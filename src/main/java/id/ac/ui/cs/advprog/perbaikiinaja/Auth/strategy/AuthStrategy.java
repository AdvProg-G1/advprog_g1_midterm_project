//src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/strategy/AuthStrategy.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth.strategy;

public interface AuthStrategy {
    Object login(String email, String password);
    Object register(Object request);
}
