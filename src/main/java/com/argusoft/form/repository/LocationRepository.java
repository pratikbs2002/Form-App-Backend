package com.argusoft.form.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.argusoft.form.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByParentId(Long parentId);

    @Query("SELECT l FROM Location l WHERE l.parent.id = :parentId")
    List<Location> findDirectChildren(Long parentId);

    @Query("SELECT l FROM Location l WHERE l.parent IS NULL")
    List<Location> findRootLocations();

    Optional<Location> findById(Long id);

}