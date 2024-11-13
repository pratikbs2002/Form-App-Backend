package com.argusoft.form.service;

import com.argusoft.form.entity.Location;
import com.argusoft.form.entity.UserInfo;
import com.argusoft.form.repository.LocationRepository;
import com.argusoft.form.repository.UserInfoRepository;
import com.argusoft.form.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private LocationRepository locationRepository;

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

    public List<UserInfo> getUsersByLocation(Long locationId) {
        return userInfoRepository.findByLocationId(locationId);
    }

    public List<UserInfo> getUsersByLocationAndChildLocations(Long locationId) {
        List<Long> locationIds = collectLocationIds(locationRepository.findById(locationId).orElse(null));
        return userInfoRepository.findByLocationIdIn(locationIds);
    }

    private List<Long> collectLocationIds(Location location) {
        List<Long> locationIds = new ArrayList<>();
        if (location != null) {
            locationIds.add(location.getId());
            for (Location child : location.getChildren()) {
                locationIds.add(child.getId());
                locationIds.addAll(collectLocationIds(child));
            }
        }
        return locationIds;
    }

}
