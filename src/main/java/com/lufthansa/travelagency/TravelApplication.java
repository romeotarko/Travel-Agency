package com.lufthansa.travelagency;

import com.lufthansa.travelagency.role.Role;
import com.lufthansa.travelagency.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static com.lufthansa.travelagency.role.ERoleType.ROLE_ADMIN;
import static com.lufthansa.travelagency.role.ERoleType.ROLE_USER;

@SpringBootApplication
@EnableWebMvc
public class TravelApplication implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(TravelApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findByName(ROLE_USER).isEmpty()) {
            roleRepository.saveAndFlush(new Role(ROLE_USER));
        }
        if (roleRepository.findByName(ROLE_ADMIN).isEmpty()) {
            roleRepository.saveAndFlush(new Role(ROLE_ADMIN));
        }
    }
}
