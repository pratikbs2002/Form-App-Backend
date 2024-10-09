package com.argusoft.form.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.entity.Location;
import com.argusoft.form.service.LocationService;

@RestController
@RequestMapping("/api/locations/")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/root")
    public List<Location> getRootLocations() {
        return locationService.findRootLocations();
    }

    @GetMapping("/{parentId}")
    public List<Location> getChildren(@PathVariable Long parentId) {
        System.out.println(locationService.findDirectChildren(parentId));
        return locationService.getChildren(parentId);
    }
}
