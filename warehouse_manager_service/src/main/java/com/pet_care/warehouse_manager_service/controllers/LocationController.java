package com.pet_care.warehouse_manager_service.controllers;

import com.pet_care.warehouse_manager_service.entity.Location;
import com.pet_care.warehouse_manager_service.service.serviceImpl.LocationServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Location - Service")
@RequestMapping("/warehouse-manager/location")
public class LocationController {
    @Autowired
    LocationServiceImpl locationService;

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocation(){
        return ResponseEntity.ok(locationService.getAllLocation());
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<Location>> getAll(){
        return ResponseEntity.ok(locationService.findAll());
    }

//    @GetMapping("/{caculationUnitId}")
//    public ResponseEntity<Caculation_Unit> getById(@PathVariable("caculationUnitId") int caculationUnitId){
//        return ResponseEntity.ok(caculationUnitService.);
//    }

    @PostMapping
    public ResponseEntity<Location> create(@RequestBody Location location){
        return ResponseEntity.ok(locationService.create(location));
    }

    @PutMapping("{id}")
    public ResponseEntity<Location> update(@PathVariable int id,@RequestBody Location location){
        return ResponseEntity.ok(locationService.update(location));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteLocations2(@RequestBody List<Long> locationIds) {
        locationService.deleteLocations(locationIds);
        return ResponseEntity.ok("Deleted products successfully");
    }


}
