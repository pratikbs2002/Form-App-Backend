package com.argusoft.form.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.argusoft.form.entity.Location;
import com.argusoft.form.repository.LocationRepository;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    // Fetch root
    public List<Location> getRootLocations() {
        return locationRepository.findByParentId(null);
    }

    // Fetch children
    public List<Location> getChildren(Long parentId) {
        return locationRepository.findByParentId(parentId);
    }

    // Fetch root
    public List<Location> findRootLocations() {
        return locationRepository.findRootLocations();
    }

    // Fetch children
    public List<Location> findDirectChildren(Long parentId) {
        return locationRepository.findDirectChildren(parentId);
    }
}
