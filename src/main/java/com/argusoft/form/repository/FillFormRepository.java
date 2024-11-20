package com.argusoft.form.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.argusoft.form.dto.FillFormDTO;
import com.argusoft.form.entity.FillForm;

import jakarta.transaction.Transactional;

@Repository
public interface FillFormRepository extends JpaRepository<FillForm, Long> {

        @Transactional
        @Modifying
        @Query(value = "INSERT INTO fill_form (form_id, user_id, title, answers, created_at, location, location_id, is_submitted) VALUES (:formId, :userId, :title, CAST(:answers AS jsonb), :createdAt, CAST(:location AS loc), :locationId, :isSubmitted)", nativeQuery = true)
        void insertFillForm(Long formId, Long userId, String title, String answers, LocalDateTime createdAt,
                        String location,
                        Long locationId, boolean isSubmitted);

        @Query("SELECT f FROM FillForm f WHERE f.isSubmitted = false")
        Page<FillForm> findAllFillForm(Pageable p);

        @Query(value = "SELECT * FROM fill_form WHERE id = :id", nativeQuery = true)
        FillForm findFillFormByFillFormId(Long id);

        @Query(value = "SELECT * FROM fill_form WHERE form_id = :id", nativeQuery = true)
        List<FillForm> findFillFormByFormId(Long id);

        @Query(value = "SELECT * FROM fill_form WHERE form_id = :formId AND user_id = :userId", nativeQuery = true)
        Optional<FillForm> findFillFormByFormIdAndUserId(Long formId, Long userId);

        @Query(value = "UPDATE fill_form SET answers = CAST(:answers AS jsonb),created_at = :createdAt, is_submitted = :isSubmitted WHERE id = :fillFormId", nativeQuery = true)
        int updateFillForm(@Param("fillFormId") Long fillFormId, @Param("answers") String answers,
                        @Param("createdAt") LocalDateTime createdAt, @Param("isSubmitted") boolean isSubmitted);

        @Transactional
        @Modifying
        @Query(value = "DELETE FROM fill_form WHERE id = :id", nativeQuery = true)
        void deleteFillFormById(Long id);

        @Query("SELECT f FROM FillForm f WHERE f.isSubmitted = false AND f.user.id = :userId")
        Page<FillForm> findAllFillFormByReportingUserId(Pageable p, Long userId);

        @Query(value = "SELECT * FROM fill_form WHERE form_id IN :formIds AND is_submitted = true", nativeQuery = true)
        Page<FillForm> findAllFillFormByFormIdsForAdmin(Pageable pageable, List<Long> formIds);

}
