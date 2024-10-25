package com.argusoft.form.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.argusoft.form.entity.CreateForm;

import jakarta.transaction.Transactional;

@Repository
public interface CreateFormRepository extends JpaRepository<CreateForm, Long> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO create_form (title, admin_id, created_at, questions) VALUES (:title, :adminId, :createdAt, CAST(:questions AS jsonb))", nativeQuery = true)
    void insertCreateForm(String title, Long adminId, LocalDateTime createdAt, String questions);

    @Transactional
    @Modifying
    @Query(value = "UPDATE create_form SET title = :title, admin_id = :adminId, created_at = :createdAt, questions = CAST(:questions AS jsonb) WHERE id = :id", nativeQuery = true)
    void updateCreateForm(Long id, String title,  Long adminId, LocalDateTime createdAt, String questions);

}
