package com.example.backend;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.backend.models.Member;
import com.example.backend.models.Role;
import com.example.backend.services.AdService;
import com.example.backend.services.ChatroomService;
import com.example.backend.services.FriendService;
import com.example.backend.services.MemberService;
import com.example.backend.services.NotificationService;
import com.example.backend.services.PostService;

import lombok.extern.slf4j.Slf4j;


@SpringBootApplication
@Slf4j
public class ErgasiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ErgasiaApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

    @Bean
    CommandLineRunner run(MemberService memberService, FriendService friendService, ChatroomService chatService,
                       NotificationService notificationService, PostService postService, AdService adService) {
        return args -> {
            // Check if the admin user already exists
            if (Objects.isNull(memberService.findMemberByEmail("admin@admin.com"))) {
                log.info("Admin user not found. Creating default admin user...");
                
                // Create roles if they don't exist
                memberService.saveRole(new Role(null, "ROLE_MEMBER"));
                memberService.saveRole(new Role(null, "ROLE_ADMIN"));

                // Create the admin user
                memberService.saveMember(new Member("admin", "admin", "admin", "admin@admin.com", "9999999999", new ArrayList<>()));

                // Assign the admin role to the admin user
                memberService.addRole("admin@admin.com", "ROLE_ADMIN");

                log.info("Default admin user created successfully.");
            } else {
                log.info("Admin user already exists. Skipping initialization.");
            }
        };
    }

}
