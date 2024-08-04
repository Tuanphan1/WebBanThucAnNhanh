package com.example.foodshop.config;

import com.example.foodshop.Entity.Role;
import com.example.foodshop.Entity.User;
import com.example.foodshop.repository.RoleRepository;
import com.example.foodshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class DataInitializerConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            // Initialize ROLE_ADMIN
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName("ROLE_ADMIN");
                return roleRepository.save(newRole);
            });

            // Initialize ROLE_USER
            Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName("ROLE_USER");
                return roleRepository.save(newRole);
            });

            // Check if admin user exists, if not create it
            userRepository.findByUsername("admin").orElseGet(() -> {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setEmail("admin@example.com");
                admin.setRoles(Collections.singleton(adminRole)); // Admin gets only admin role
                return userRepository.save(admin);
            });
        };
    }
}
