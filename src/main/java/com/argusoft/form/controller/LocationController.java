package com.argusoft.form.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.dto.LocationDTO;
import com.argusoft.form.entity.Location;
import com.argusoft.form.service.LocationService;

@RestController
@RequestMapping("/api/locations/")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/root")
    public List<LocationDTO> getRootLocations() {

        List<Location> locations = locationService.findRootLocations();

        List<LocationDTO> locationDTOs = new ArrayList<>();

        for (Location location : locations) {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setId(location.getId());
            locationDTO.setName(location.getName());
            locationDTO.setHavingChild(location.getChildren().size() > 0 ? true : false);

            if (location.getParent() != null) {
                locationDTO.setParent_id(location.getParent().getId());
            }
            locationDTOs.add(locationDTO);
        }
        return locationDTOs;
    }

    @GetMapping("/{parentId}")
    public List<LocationDTO> getChildren(@PathVariable Long parentId) {

        List<Location> locations = locationService.getChildren(parentId);

        List<LocationDTO> locationDTOs = new ArrayList<>();

        for (Location location : locations) {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setId(location.getId());
            locationDTO.setName(location.getName());
            locationDTO.setHavingChild(location.getChildren().size() > 0 ? true : false);

            if (location.getParent() != null) {
                locationDTO.setParent_id(location.getParent().getId());
            }
            locationDTOs.add(locationDTO);
        }
        return locationDTOs;
    }

}
