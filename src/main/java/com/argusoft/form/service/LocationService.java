package com.argusoft.form.service;

import java.util.List;
import java.util.Optional;

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

    // add location
    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }

    // find single row location
    public Optional<Location> findLocationById(Long id) {
        return locationRepository.findById(id);
    }

    // delete single row location
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    // update single row location
    public int updateLocation(Long id, String name) {
        return locationRepository.updateLocationName(id, name);
    }
}
