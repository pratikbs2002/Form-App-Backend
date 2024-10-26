package com.argusoft.form.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.argusoft.form.entity.FillForm;

import jakarta.transaction.Transactional;

@Repository
public interface FillFormRepository extends JpaRepository<FillForm, Long> {

        @Transactional
        @Modifying
        @Query(value = "INSERT INTO fill_form (form_id, user_id, answers, created_at, location, location_id) VALUES (:formId, :userId, CAST(:answers AS jsonb), :createdAt, CAST(:location AS loc), :locationId)", nativeQuery = true)
        void insertFillForm(Long formId, Long userId, String answers, LocalDateTime createdAt, String location,
                        Long locationId);

        // @Transactional
        // @Modifying
        // @Query(value = "UPDATE fill_form SET form_id = :formId, user_id = :userId,
        // answers = CAST(:answers AS jsonb), created_at = :createdAt, location =
        // :location, location_id = :locationId WHERE form_id = :formId AND user_id =
        // :userId", nativeQuery = true)
        // void updateFillForm(Long formId, Long userId, String answers, LocalDateTime
        // createdAt, String location,
        // Integer locationId);

        @Query("SELECT f FROM FillForm f")
        Page<FillForm> getAllFillForm(Pageable p);

        // @Query(value = "SELECT * FROM fill_form WHERE id = :id", nativeQuery = true)
        // Optional<FillForm> getFillFormById(Long id);

        @Transactional
        @Modifying
        @Query(value = "DELETE FROM fill_form WHERE id = :id", nativeQuery = true)
        void deleteFillFormById(Long id);

}
