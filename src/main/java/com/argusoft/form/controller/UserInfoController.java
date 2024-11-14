package com.argusoft.form.controller;

import com.argusoft.form.dto.RoleDTO;
import com.argusoft.form.dto.UserInfoDTO;
import com.argusoft.form.entity.UserInfo;
import com.argusoft.form.service.LocationService;
import com.argusoft.form.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.AccessFlag.Location;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/userinfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private LocationService locationService;

    @GetMapping
    public ResponseEntity<List<UserInfo>> getAllUsers() {
        List<UserInfo> users = userInfoService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInfo> getUserById(@PathVariable Long id) {
        return userInfoService.getUserById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<UserInfo> createUser(@RequestBody UserInfo userInfo) {
        UserInfo createdUser = userInfoService.createUser(userInfo);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserInfo> updateUser(@PathVariable Long id, @RequestBody UserInfo userInfo) {
        UserInfo updatedUser = userInfoService.updateUser(id, userInfo);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userInfoService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<UserInfoDTO>> getUsersByLocation(@PathVariable Long locationId) {
        List<UserInfo> users = userInfoService.getUsersByLocation(locationId);
        List<UserInfoDTO> userDtos = users.stream()
                .map(user -> new UserInfoDTO(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        new RoleDTO(user.getRole().getRoleId(), user.getRole().getRoleName()),
                        user.getAddress(),
                        user.getLocation().getId()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    // @Query(value = """
    // WITH RECURSIVE location_hierarchy AS (
    // SELECT id FROM location WHERE id = :locationId
    // UNION ALL
    // SELECT l.id FROM location l
    // INNER JOIN location_hierarchy lh ON l.parent_id = lh.id
    // )
    // SELECT * FROM users u WHERE u.location_id IN (SELECT id FROM
    // location_hierarchy)
    // """, nativeQuery = true)
    // List<User> findAllUsersByLocationId(@Param("locationId") Long locationId);

    @GetMapping("/location/child/{locationId}")
    public ResponseEntity<List<UserInfoDTO>> getUsersByLocationAndChildLocations(@PathVariable Long locationId) {
        List<UserInfo> users = userInfoService.getUsersByLocationAndChildLocations(locationId);
        List<UserInfoDTO> userDtos = users.stream()
                .map(user -> new UserInfoDTO(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        new RoleDTO(user.getRole().getRoleId(), user.getRole().getRoleName()),
                        user.getAddress(),
                        user.getLocation().getId()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @PutMapping("/{userId}/location/{locationId}")
    public ResponseEntity<String> updateUserLocation(
            @PathVariable Long userId,
            @PathVariable Long locationId) {

        com.argusoft.form.entity.Location newLocation = locationService.findLocationById(locationId).orElse(null);

        if (newLocation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Location not found");
        }

        // Update user location
        try {
            userInfoService.updateLocation(userId, newLocation);
            return ResponseEntity.ok("User location updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update user location: " + e.getMessage());
        }
    }

}
