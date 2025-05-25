// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/config/AuthConfig.java
package id.ac.ui.cs.advprog.perbaikiinaja.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AuthConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}