package com.argusoft.form.repository;

import com.argusoft.form.entity.UserInfo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    List<UserInfo> findByLocationId(Long locationId);

    List<UserInfo> findByLocationIdIn(List<Long> locationIds);
}
