package com.codexdrive.electronic.store;

import com.codexdrive.electronic.store.config.AppConstants;
import com.codexdrive.electronic.store.entities.Role;
import com.codexdrive.electronic.store.entities.User;
import com.codexdrive.electronic.store.repositories.RoleRepository;
import com.codexdrive.electronic.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

    public static void main(String[] args) {

        SpringApplication.run(ElectronicStoreApplication.class, args);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {

        // =========================
        // Create Admin Role
        // =========================

        Role roleAdmin = roleRepository
                .findByName("ROLE_" + AppConstants.ROLE_ADMIN)
                .orElse(null);

        if (roleAdmin == null) {

            Role role1 = new Role();

            role1.setRoleId(UUID.randomUUID().toString());
            role1.setName("ROLE_" + AppConstants.ROLE_ADMIN);

            // Save role and assign returned object to roleAdmin
            roleAdmin = roleRepository.save(role1);
        }


        // =========================
        // Create Normal Role
        // =========================

        Role roleNormal = roleRepository
                .findByName("ROLE_" + AppConstants.ROLE_NORMAL)
                .orElse(null);

        if (roleNormal == null) {

            Role role2 = new Role();

            role2.setRoleId(UUID.randomUUID().toString());
            role2.setName("ROLE_" + AppConstants.ROLE_NORMAL);

            // Save role and assign returned object to roleNormal
            roleNormal = roleRepository.save(role2);
        }


        // =========================
        // Create Admin User
        // =========================

        User user = userRepository
                .findByEmail("chitranshu2411@gmail.com")
                .orElse(null);

        if (user == null) {

            user = new User();

            user.setName("chitranshu");

            user.setEmail("chitranshu2411@gmail.com");

            user.setPassword(
                    passwordEncoder.encode("chitranshu")
            );

            user.setRoles(
                    List.of(roleAdmin)
            );

            user.setUserId(
                    UUID.randomUUID().toString()
            );

            userRepository.save(user);
        }
    }
}