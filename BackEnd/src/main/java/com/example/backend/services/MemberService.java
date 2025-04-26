package com.example.backend.services;

import java.util.List;

import com.example.backend.models.Education;
import com.example.backend.models.Experience;
import com.example.backend.models.Member;
import com.example.backend.models.MemberInfo;
import com.example.backend.models.MemberPersonalDataDTO;
import com.example.backend.models.Resume;
import com.example.backend.models.Role;
import com.example.backend.models.Skills;

public interface MemberService {

    // save member into DB
    Member saveMember(Member member);

    // get list of members
    List<MemberInfo> getMembers();

    // get Member specified by email
    Member findMemberByEmail(String email);

    Boolean userWithEmailExists(String email);

    MemberInfo getSpecifiedMemberInfo(String email);

    // get list of members of the specified emails
    List<Member> getSpecifiedMembers(List<String> emails);

    MemberPersonalDataDTO getMemberPersonalData(String email);

    // save role into DB
    Role saveRole(Role role);

    // add role to user
    void addRole(String email, String roleName);

    // save resume, experience, education, skills into DB
    Resume saveResume(Resume resume);

    Experience saveExperience(Experience experience);

    Education saveEducation(Education education);

    Skills saveSkills(Skills skills);


    // update resume if already exists in DB
    void updateResume(String text, Long resume_id);

    void updateExperience(String text, Long resume_id);

    void updateEducation(String text, Long resume_id);

    void updateSkills(String text, Long resume_id);

    void addExperience(String email, Experience experience);

    void addEducation(String email, Education education);

    void addSkills(String email, Skills skills);

    // add resume to user
    void addResume(String email, Resume resume);

    void publicResume(Long id);
    void privateResume(Long id);

    void publicEduc(Long id);
    void privateEduc(Long id);

    void publicExp(Long id);
    void privateExp(Long id);

    void publicSkills(Long id);
    void privateSkills(Long id);

    // change email
    void updateMemberEmail(String old_email, String new_email);

    // change password
    String updateMemberPassword(String email, String new_password, String jwt);

}
