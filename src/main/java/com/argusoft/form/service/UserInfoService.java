package com.argusoft.form.service;

import com.argusoft.form.entity.UserInfo;
import com.argusoft.form.repository.UserInfoRepository;
import com.argusoft.form.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    public List<UserInfo> getAllUsers() {
        return userInfoRepository.findAll();
    }

    public Optional<UserInfo> getUserById(Long id) {
        return userInfoRepository.findById(id);
    }

    public UserInfo createUser(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }

    public UserInfo updateUser(Long id, UserInfo userInfo) {
        userInfo.setId(id);
        return userInfoRepository.save(userInfo);
    }

    public void deleteUser(Long id) {
        userInfoRepository.deleteById(id);
    }
}
