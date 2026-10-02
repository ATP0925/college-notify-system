package com.college.collegenotify.config;

import com.college.collegenotify.model.Role;
import com.college.collegenotify.model.User;
import com.college.collegenotify.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner seedUsers(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@college.edu").isEmpty()) {
                User admin = new User();
                admin.setName("Admin User");
                admin.setEmail("admin@college.edu");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                admin.setCollege("Central College");
                userRepository.save(admin);
            }
        };
    }
}
