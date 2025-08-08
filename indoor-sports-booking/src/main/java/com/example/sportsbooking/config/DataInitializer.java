package com.example.sportsbooking.config;

import com.example.sportsbooking.entity.User;
import com.example.sportsbooking.enums.Role;
import com.example.sportsbooking.repository.UserRepository;
import com.example.sportsbooking.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, UserService userService) {
        return args -> {
            String adminEmail = System.getenv().getOrDefault("ADMIN_EMAIL", "");
            String adminPassword = System.getenv().getOrDefault("ADMIN_PASSWORD", "");
            if (!adminEmail.isBlank() && !adminPassword.isBlank()) {
                userRepository.findByEmail(adminEmail).orElseGet(() ->
                        userService.createUser("Administrator", adminEmail, adminPassword, Role.ADMIN));
            }
        };
    }
}