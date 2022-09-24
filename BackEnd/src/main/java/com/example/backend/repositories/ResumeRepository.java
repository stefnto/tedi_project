package com.example.backend.repositories;

import com.example.backend.models.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface ResumeRepository extends JpaRepository<Resume, Long> {



    @Query(value = "SELECT r.id FROM resume r, member m WHERE r.id = m.resume_id AND m.email = :email",
            nativeQuery = true)
    String getMemberResumeId(String email);

    @Query(value = "SELECT r.text FROM resume r, member m WHERE r.id = m.resume_id AND m.email = :email",
            nativeQuery = true)
    String getMemberResumeText(String email);


    @Modifying
    @Query(value = "Update resume SET resume.text = :text WHERE resume.id = :id",
            nativeQuery = true)
    void updateCVById(String text, Long id);

}
