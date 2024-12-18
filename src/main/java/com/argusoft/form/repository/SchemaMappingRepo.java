package com.argusoft.form.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.argusoft.form.entity.Schema;

@Repository
public interface SchemaMappingRepo extends JpaRepository<Schema, String> {

    List<Schema> findBySchemaName(String schemaName);

    Optional<Schema> findByUuidName(String uuidName);

    @Query("SELECT s FROM Schema s")
    Page<List<Schema>> findPageableAll(Pageable pageable);
}
