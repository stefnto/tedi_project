package com.example.backend.repositories;


import com.example.backend.models.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    @Query(value = "SELECT e.id FROM experience e, member m WHERE e.id = m.resume_id AND m.email = :email",
            nativeQuery = true)
    String getMemberExperienceId(String email);

    @Query(value = "SELECT e.text FROM experience e, member m WHERE e.id = m.resume_id AND m.email = :email",
            nativeQuery = true)
    String getMemberExperienceText(String email);

    @Modifying
    @Query(value = "Update experience SET experience.text = :text WHERE experience.id = :id",
            nativeQuery = true)
    void updateExperienceById(String text, Long id);
}
