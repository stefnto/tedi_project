package com.example.backend.repositories;

import com.example.backend.models.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EducationRepository extends JpaRepository<Education, Long> {

    @Query(value = "SELECT e.id FROM education e, member m WHERE e.id = m.resume_id AND m.email = :email",
            nativeQuery = true)
    String getMemberEducationId(String email);

    @Query(value = "SELECT e.text FROM education e, member m WHERE e.id = m.resume_id AND m.email = :email",
            nativeQuery = true)
    String getMemberEducationText(String email);

    @Modifying
    @Query(value = "Update education SET education.text = :text WHERE education.id = :id",
            nativeQuery = true)
    void updateEducationById(String text, Long id);
}
