package com.example.backend.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.backend.models.Education;
import com.example.backend.models.Experience;
import com.example.backend.models.Member;
import com.example.backend.models.MemberInfo;
import com.example.backend.models.Resume;
import com.example.backend.models.Role;
import com.example.backend.models.Skills;
import com.example.backend.services.MemberServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/api")
class MemberController {
    private final MemberServiceImpl memberService;
    
    // Show all members only accessible by admin
    @GetMapping("/members")
    public ResponseEntity<List<MemberInfo>> getMembers() {
        // ok() gives a 200 status message
        return ResponseEntity.ok().body(memberService.getMembers());
    }

    // Get list of members of emails that were specified as a list
    @GetMapping("/members/get/list")
    public ResponseEntity<List<Member>> getSpecMembers(@RequestHeader List<String> emails){
        return ResponseEntity.ok().body(memberService.getSpecifiedMembers(emails));
    }

    // checkEmailExists returns True if email exists else returns false
    @GetMapping("/members/get-email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        // ok() gives a 200 status message
        // tmp will be null object if member doesn't exist
        MemberInfo tmp = memberService.getSpecifiedMemberInfo(email);
        return ResponseEntity.ok().body(Objects.nonNull(tmp));
    }

    // get member specified by email
    @GetMapping("/members/get/{email}")
    public ResponseEntity<Member> getMemberByEmail(@PathVariable String email) {
        // ok() gives a 200 status message
        return ResponseEntity.ok().body(memberService.findMemberByEmail(email));
    }

    // get memberInfo specified by email
    @GetMapping("/members/getInfo/{email}")
    public ResponseEntity<MemberInfo> getMemberInfoByEmail(@PathVariable String email) {
        return ResponseEntity.ok().body(memberService.getSpecifiedMemberInfo(email));
    }


    // update member CV
    @PostMapping("/members/resume/update")
    public ResponseEntity<String> updateResumeByMemberEmail(@RequestHeader String email, @RequestBody String text) {
        memberService.addResume(email, new Resume(null, text, null, true));
        return ResponseEntity.status(201).body("CV updated");
    }

    // update member experience
    @PostMapping("member/setExperience")
    public ResponseEntity<String> updateExperienceByMemberEmail(@RequestHeader String email, @RequestBody String text) {
        memberService.addExperience(email, new Experience(null, text, null, true));
        return ResponseEntity.status(201).body("Experience updated");
    }

    // update member education
    @PostMapping("member/setEducation")
    public ResponseEntity<String> updateEducationByMemberEmail(@RequestHeader String email, @RequestBody String text) {
        memberService.addEducation(email, new Education(null, text, null, true));
        return ResponseEntity.status(201).body("Education updated");
    }

    // update member skills
    @PostMapping("member/setSkills")
    public ResponseEntity<String> updateSkillsByMemberEmail(@RequestHeader String email, @RequestBody String text) {
        memberService.addSkills(email, new Skills(null, text.toLowerCase(), null, true));
        return ResponseEntity.status(201).body("Skills updated");
    }

    @PutMapping("member/resume_public")
    public ResponseEntity<?> makeResumePublic(@RequestBody String email){
        Member member = memberService.findMemberByEmail(email);
        memberService.publicResume(member.getResume().getId());
        return ResponseEntity.status(201).body("Resume is public");
    }

    @PutMapping("member/resume_private")
    public ResponseEntity<?> makeResumePrivate(@RequestBody String email){
        Member member = memberService.findMemberByEmail(email);
        memberService.privateResume(member.getResume().getId());
        return ResponseEntity.status(201).body("Resume is private");
    }

    @PutMapping("member/experience_public")
    public ResponseEntity<?> makeExperiencePublic(@RequestBody String email){
        Member member = memberService.findMemberByEmail(email);
        memberService.publicExp(member.getExperience().getId());
        return ResponseEntity.status(201).body("Experience is public");
    }

    @PutMapping("member/experience_private")
    public ResponseEntity<?> makeExperiencePrivate(@RequestBody String email){
        Member member = memberService.findMemberByEmail(email);
        memberService.privateExp(member.getExperience().getId());
        return ResponseEntity.status(201).body("Experience is private");
    }

    @PutMapping("member/education_public")
    public ResponseEntity<?> makeEducationPublic(@RequestBody String email){
        Member member = memberService.findMemberByEmail(email);
        memberService.publicEduc(member.getEducation().getId());
        return ResponseEntity.status(201).body("Education is public");
    }

    @PutMapping("member/education_private")
    public ResponseEntity<?> makeEducationPrivate(@RequestBody String email){
        Member member = memberService.findMemberByEmail(email);
        memberService.privateEduc(member.getEducation().getId());
        return ResponseEntity.status(201).body("Education is private");
    }

    @PutMapping("member/skills_public")
    public ResponseEntity<?> makeSkillsPublic(@RequestBody String email){
        Member member = memberService.findMemberByEmail(email);
        memberService.publicSkills(member.getSkills().getId());
        return ResponseEntity.status(201).body("Skills is public");
    }

    @PutMapping("member/skills_private")
    public ResponseEntity<?> makeSkillsPrivate(@RequestBody String email){
        Member member = memberService.findMemberByEmail(email);
        memberService.privateSkills(member.getSkills().getId());
        return ResponseEntity.status(201).body("Skills is private");
    }

    @GetMapping("member/resume/{email}")
    public ResponseEntity<Resume> getResumeFromEmail(@PathVariable String email){
        Member member = memberService.findMemberByEmail(email);
        return ResponseEntity.ok().body(member.getResume());
    }

    @GetMapping("member/experience/{email}")
    public ResponseEntity<Experience> getExperienceFromEmail(@PathVariable String email){
        Member member = memberService.findMemberByEmail(email);
        return ResponseEntity.ok().body(member.getExperience());
    }

    @GetMapping("member/education/{email}")
    public ResponseEntity<Education> getEducationFromEmail(@PathVariable String email){
        Member member = memberService.findMemberByEmail(email);
        return ResponseEntity.ok().body(member.getEducation());
    }

    @GetMapping("member/skills/{email}")
    public ResponseEntity<Skills> getSkillsFromEmail(@PathVariable String email){
        Member member = memberService.findMemberByEmail(email);
        return ResponseEntity.ok().body(member.getSkills());
    }
    
    // update member email
    @PostMapping("/members/change/email")
    public ResponseEntity<String> updateMemberEmail(@RequestHeader String old_email, @RequestBody String new_email){

        memberService.updateMemberEmail(old_email, new_email);
        return ResponseEntity.status(201).body("Email updated");
    }

    // update member password
    @PostMapping("/members/change/password")
    public ResponseEntity<String> updateMemberPassword(@RequestHeader String Authorization, @RequestHeader String email, @RequestBody String password){

        return ResponseEntity.status(201).body(
                memberService.updateMemberPassword(Authorization.substring("Bearer ".length()), email, password));
    }

    // register new member
    @PostMapping("/register")
    public ResponseEntity<String> registerMember(@RequestBody Member member){
        // gives a 201 status message
        memberService.saveMember(member); // add member
        memberService.addRole(member.getEmail(),"ROLE_MEMBER");
        memberService.addResume(member.getEmail(), new Resume(null, null, null, null));
        memberService.addSkills(member.getEmail(), new Skills(null, "", null, true));
        memberService.addEducation(member.getEmail(), new Education(null, "", null, true));
        memberService.addExperience(member.getEmail(), new Experience(null, "", null, true));
        return ResponseEntity.status(201).body("Member registered");

    }


    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorization = request.getHeader(AUTHORIZATION);
        if(authorization != null && authorization.startsWith("Bearer ")){
            try {
                String refresh_token = authorization.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("backendapi".getBytes()); // " 'backendapi' will be used to sign the JSON web tokens
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                Member member = memberService.findMemberByEmail(username); // username actually refers to the email
                String access_token = JWT.create()
                        .withSubject(member.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() +  15 * 60 * 1000)) // access token will remain for 15 minutes
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", member.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (JWTCreationException | JWTVerificationException | IOException | IllegalArgumentException exception){
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
