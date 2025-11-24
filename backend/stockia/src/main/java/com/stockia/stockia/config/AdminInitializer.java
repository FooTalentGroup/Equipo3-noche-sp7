package com.stockia.stockia.config;

import com.stockia.stockia.enums.AccountStatus;
import com.stockia.stockia.enums.Role;
import com.stockia.stockia.models.User;
import com.stockia.stockia.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args){
        Optional<User> aux = userRepository.findByEmail("admin@stockia.com");

        // Caso 1: No existe usuario "admin" → crear
        if (aux.isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@stockia.com");
            admin.setName("System Admin");
            admin.setPassword(passwordEncoder.encode("Admin123@"));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
            System.out.println("Initial admin created: email=admin@stockia.com, password=Admin123@");
            return;
        }

        // Caso 2: Existe "admin" pero NO existe ningún user con rol ADMIN → corregir
        User admin = aux.get();
        if (!userRepository.existsByRole(Role.ADMIN)) {
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Existing 'admin' user role updated to ADMIN.");
        }
    }
}
