package com.example.ucms.authConfigerations;

import com.example.ucms.model.User;
import com.example.ucms.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminConfig implements CommandLineRunner {

    @Autowired
    private UserRepo userRepo;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public void run(String... args) throws Exception {
        // Check if admin exists
        if (userRepo.findByUserId(adminUsername).isEmpty()) {
            User admin = new User();
            admin.setUserId(adminUsername);
            admin.setEmail(adminUsername);
            admin.setName(adminUsername);
            admin.setRole_id(1L);
            admin.setNic("000000000");
            admin.setPassword(encoder.encode(adminPassword)); // hashed password
            userRepo.save(admin);
            System.out.println("✅ Default admin user created!");
        } else {
            System.out.println("ℹ️ Admin user already exists.");
        }
    }
}
