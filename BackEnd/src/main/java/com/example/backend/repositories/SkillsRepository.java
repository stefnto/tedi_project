package com.example.backend.repositories;

import com.example.backend.models.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SkillsRepository extends JpaRepository<Skills, Long> {

    @Query(value = "SELECT s.id FROM skills s, member m WHERE s.id = m.resume_id AND m.email = :email",
            nativeQuery = true)
    String getMemberSkillsId(String email);

    @Query(value = "SELECT s.text FROM skills s, member m WHERE s.id = m.resume_id AND m.email = :email",
            nativeQuery = true)
    String getMemberSkillsText(String email);

    @Modifying
    @Query(value = "Update skills SET skills.text = :text WHERE skills.id = :id",
            nativeQuery = true)
    void updateSkillsById(String text, Long id);

    @Query(value = "SELECT s.text FROM member m, skills s WHERE m.email= :email AND m.skills_id = s.id", nativeQuery = true)
    String getSkills(String email);
}


