package com.example.usermanagement.config;

import com.example.usermanagement.entity.Role;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.repository.RoleRepository;
import com.example.usermanagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository, 
                                 UserRepository userRepository,
                                 PasswordEncoder passwordEncoder) {
        return args -> {
            // Create roles if they don't exist
            createRoleIfNotFound(roleRepository, "USER");
            createRoleIfNotFound(roleRepository, "MANAGER");
            createRoleIfNotFound(roleRepository, "ADMIN");

            // Create admin user if it doesn't exist
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEnabled(true);
                
                Set<Role> roles = new HashSet<>();
                roleRepository.findByName("ADMIN").ifPresent(roles::add);
                admin.setRoles(roles);
                
                userRepository.save(admin);
            }
        };
    }

    private void createRoleIfNotFound(RoleRepository roleRepository, String name) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
    }
}
