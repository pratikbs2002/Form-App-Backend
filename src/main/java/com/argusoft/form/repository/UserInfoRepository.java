package com.argusoft.form.repository;

import com.argusoft.form.entity.Location;
import com.argusoft.form.entity.UserInfo;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    List<UserInfo> findByLocationId(Long locationId);

    List<UserInfo> findByLocationIdIn(List<Long> locationIds);

    @Modifying
    @Transactional
    @Query("UPDATE UserInfo u SET u.location = :location WHERE u.id = :userId")
    void updateUserLocation(Long userId, Location location);
}
