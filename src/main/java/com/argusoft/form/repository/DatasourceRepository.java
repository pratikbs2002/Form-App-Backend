package com.argusoft.form.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.argusoft.form.entity.DataSourceEntity;

@Repository
public interface DatasourceRepository extends JpaRepository<DataSourceEntity, Long> {
    @Query("SELECT d FROM DataSourceEntity d WHERE d.role = :role")
    DataSourceEntity findByRole(String role);
}
