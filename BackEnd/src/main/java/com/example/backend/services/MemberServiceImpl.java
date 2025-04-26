package com.example.backend.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.backend.models.Education;
import com.example.backend.models.Experience;
import com.example.backend.models.Member;
import com.example.backend.models.MemberInfo;
import com.example.backend.models.MemberPersonalDataDTO;
import com.example.backend.models.Resume;
import com.example.backend.models.Role;
import com.example.backend.models.Skills;
import com.example.backend.repositories.EducationRepository;
import com.example.backend.repositories.ExperienceRepository;
import com.example.backend.repositories.MemberRepository;
import com.example.backend.repositories.ResumeRepository;
import com.example.backend.repositories.RoleRepository;
import com.example.backend.repositories.SkillsRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRep;
    private final RoleRepository roleRep;
    private final PasswordEncoder passwordEncoder;
    private final ResumeRepository resumeRep;
    private final ExperienceRepository experienceRep;
    private final EducationRepository educationRep;
    private final SkillsRepository skillsRep;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRep.findByEmail(email);
        if (member == null){
            log.error("Member doesn't exist");
            throw new UsernameNotFoundException("Member doesn't exist");
        } else {
            log.info("User {} found", email);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // get all roles the member has
        member.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(member.getEmail(), member.getPassword(), authorities);
    }


    @Override
    public Member saveMember(Member member) {
        log.info("Saving new member to the db");
        member.setPassword(passwordEncoder.encode(member.getPassword())); // encode the password of the member before saving it to the database
        return memberRep.save(member);
    }

    @Override
    public List<MemberInfo> getMembers() {
        List<Member> list = memberRep.findAll();
        List<MemberInfo> members = new ArrayList<>();
        for (Member member : list){
            members.add(new MemberInfo(member.getId(), member.getName(), member.getSurname(),
                    member.getEmail(), member.getPhone()));
        }
        return members;
    }

    @Override
    public Member findMemberByEmail(String email){
        return memberRep.findByEmail(email);
    }

    @Override
    public Boolean userWithEmailExists(String email) {
        return memberRep.existsByEmail(email);
    }

    @Override
    public MemberInfo getSpecifiedMemberInfo(String email) {
        try {

            MemberInfo memberInfo = memberRep.findSpecificMemberInfo(email);

            return memberInfo;

        } catch (Exception e) {

            log.error("Error retrieving member info with email {}: {}", email, e);
            return null;
        }
    }

    @Override
    public List<Member> getSpecifiedMembers(List<String> emails){
        List<Member> members = new ArrayList<>();
        emails.forEach(email ->
                {
                    Member member = memberRep.findByEmail(email);
                    members.add( new Member(member.getId(), member.getEmail(), member.getName(),
                            member.getSurname(), member.getPassword(), member.getPhone(),
                            member.getPosts(),member.getAds(),member.getRoles(),
                            member.getResume(), member.getEducation(), member.getSkills(), member.getExperience()));
                }
        );
        return members;
    }

    @Override
    public MemberPersonalDataDTO getMemberPersonalData(String email) {
        MemberPersonalDataDTO memberPersonalData = memberRep.getMemberPersonalData(email);
        return memberPersonalData;
    }
    
    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the db", role.getName());
        return roleRep.save(role);
    }

    @Override
    public void addRole(String email, String roleName) {
        Member member = memberRep.findByEmail(email);
        Role role = roleRep.findByName(roleName);
        member.getRoles().add(role); // Transactional annotation will save the changes to the database
    }


    // helper methods

    @Override
    public Resume saveResume(Resume resume) {
        return resumeRep.save(resume);
    }

    @Override
    public Experience saveExperience(Experience experience) {
        return experienceRep.save(experience);
    }

    @Override
    public Education saveEducation(Education education) {
        return educationRep.save(education);
    }

    @Override
    public Skills saveSkills(Skills skills) {
        return skillsRep.save(skills);
    }

    @Override
    public void updateResume(String text, Long id) {
        resumeRep.updateCVById(text, id);
    }

    @Override
    public void updateExperience(String text, Long id) {
        experienceRep.updateExperienceById(text, id);
    }

    @Override
    public void updateEducation(String text, Long id) {
        educationRep.updateEducationById(text, id);
    }

    @Override
    public void updateSkills(String text, Long id) {
        skillsRep.updateSkillsById(text, id);
    }

    // main methods

    @Override
    public void addResume(String email, Resume resume) {
        Member member = memberRep.findByEmail(email);

        // a CV has already been created for the member thus only update it
        if (member.getResume() != null){
            this.updateResume(resume.getText(), member.getResume().getId());
        }
        // CV doesn't exist, create one
        else {
            Resume saved_resume = this.saveResume(resume);
            member.setResume(saved_resume);
        }
    }

    @Override
    public void addExperience(String email, Experience experience) {
        Member member = memberRep.findByEmail(email);

        // Experience has already been created for the member thus only update it
        if (member.getExperience() != null){
            this.updateExperience(experience.getText(), member.getExperience().getId());
        }
        // Experience doesn't exist, create one
        else {
            Experience saved_experience = this.saveExperience(experience);
            member.setExperience(saved_experience);
        }
    }

    @Override
    public void addEducation(String email, Education education) {
        Member member = memberRep.findByEmail(email);

        // Education has already been created for the member thus only update it
        if (member.getEducation() != null){
            this.updateEducation(education.getText(), member.getEducation().getId());
        }
        // Education doesn't exist, create one
        else {
            Education saved_education = this.saveEducation(education);
            member.setEducation(saved_education);
        }
    }

    @Override
    public void addSkills(String email, Skills skills){
        Member member = memberRep.findByEmail(email);

        // Skills have already been added for the member thus only update them
        if (member.getSkills() != null){
            this.updateSkills(skills.getText(), member.getSkills().getId());
        }
        // Skills haven't been added, add them
        else {
            Skills saved_skills = this.saveSkills(skills);
            member.setSkills(saved_skills);
        }
    }

    // make information public/private methods

    @Override
    public void publicResume(Long id){ resumeRep.getReferenceById(id).setIsPublic(true);}

    @Override
    public void privateResume(Long id){ resumeRep.getReferenceById(id).setIsPublic(false);}

    @Override
    public void publicEduc(Long id){ educationRep.getReferenceById(id).setIsPublic(true); }

    @Override
    public void privateEduc(Long id){
        educationRep.getReferenceById(id).setIsPublic(false);
    }

    @Override
    public void publicExp(Long id){ experienceRep.getReferenceById(id).setIsPublic(true);
    }

    @Override
    public void privateExp(Long id){
        experienceRep.getReferenceById(id).setIsPublic(false);
    }

    @Override
    public void publicSkills(Long id){
        skillsRep.getReferenceById(id).setIsPublic(true);
    }

    @Override
    public void privateSkills(Long id){
        skillsRep.getReferenceById(id).setIsPublic(false);
    }


    // update email and password methods

    @Override
    public void updateMemberEmail(String old_email, String new_email){
        memberRep.updateEmail(old_email, new_email);
        log.info("Email: " + old_email + " changed to : " + new_email);
    }

    @Override
    public String updateMemberPassword(String jwt, String email, String new_password){

        // checking if the member that owns the JWT is the member with the registered 'email'
        Algorithm algorithm = Algorithm.HMAC256("backendapi".getBytes()); // " 'backendapi' will be used to sign the JSON web tokens
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(jwt); // authorization = token
        String username = decodedJWT.getSubject();
        if (username.equals(email)){
            memberRep.updatePassword(email, passwordEncoder.encode(new_password));
            return "Password changed successfully";
        } else {
            return "You are trying to change another user's password, access denied";
        }
    }

}
