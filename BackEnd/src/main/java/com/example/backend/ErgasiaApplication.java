package com.example.backend;

import com.example.backend.models.Member;
import com.example.backend.models.MemberInfo;
import com.example.backend.models.Resume;
import com.example.backend.models.Role;
import com.example.backend.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;


@SpringBootApplication
@Slf4j
public class ErgasiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ErgasiaApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

    @Bean
    CommandLineRunner run (MemberService memberService, FriendService friendService, ChatroomService chatService,
                           NotificationService notificationService, PostService postService, AdService adService) {
		return args -> {

//		     REQUIRED WHEN FIRST STARTING THE BACKEND
//            memberService.saveRole(new Role(null, "ROLE_MEMBER"));
//            memberService.saveRole(new Role(null, "ROLE_ADMIN"));
//            memberService.saveMember(new Member("admin", "admin", "admin", "admin@admin.com", "9999999999", new ArrayList<>()));
//            memberService.addRole("admin@admin.com", "ROLE_ADMIN");
		};
	}

}
