package com.argusoft.form.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.argusoft.form.entity.DataSourceEntity;
import java.util.List;

@Repository
public interface DatasourceRepository extends JpaRepository<DataSourceEntity, Long> {
    @Query("SELECT d FROM DataSourceEntity d WHERE d.role = :role")
    DataSourceEntity findByRole(String role);

    // @Query("SELECT d FROM DataSourceEntity d WHERE d.role = :role")
    List<DataSourceEntity> findByRoleAndSchemaName(String role, String schemaName);
}
