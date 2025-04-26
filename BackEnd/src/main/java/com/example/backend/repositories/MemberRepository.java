package com.example.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.backend.models.Member;
import com.example.backend.models.MemberInfo;
import com.example.backend.models.MemberPersonalDataDTO;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT new com.example.backend.models.MemberInfo(m.id, m.name, m.surname, m.email) " +
           "FROM Member m WHERE m.email = :email")
    MemberInfo findSpecificMemberInfo(String email);

    @Modifying
    @Query(value = "UPDATE member m SET m.email = :new_email " +
            "WHERE m.email = :old_email",
            nativeQuery = true)
    void updateEmail(String old_email, String new_email);

    @Modifying
    @Query(value = "UPDATE member m SET m.password = :new_password " +
            "WHERE m.email = :email",
            nativeQuery = true)
    void updatePassword(String email, String new_password);

    @Query(
        " SELECT new com.example.backend.models.MemberPersonalDataDTO(" +
        " 	new com.example.backend.models.ExperienceDTO(e.isPublic, CASE WHEN e.isPublic = true THEN e.text ELSE null END), " +
        " 	new com.example.backend.models.EducationDTO(ed.isPublic, CASE WHEN ed.isPublic = true THEN ed.text ELSE null END), " +
        " 	new com.example.backend.models.ResumeDTO(r.isPublic, CASE WHEN r.isPublic = true THEN r.text ELSE null END), " +
        " 	new com.example.backend.models.SkillsDTO(s.isPublic, CASE WHEN s.isPublic = true THEN s.text ELSE null END)) " +
        " FROM Member m " +
        " JOIN m.experience e " +
        " JOIN m.education ed " +
        " JOIN m.resume r " +
        " JOIN m.skills s " +
        " WHERE m.email = :email")
	MemberPersonalDataDTO getMemberPersonalData(String email);

}
