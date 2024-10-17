package com.pet_care.warehouse_manager_service.service.serviceImpl;

import com.pet_care.warehouse_manager_service.entity.Location;
import com.pet_care.warehouse_manager_service.repositories.LocationRepository;
import com.pet_care.warehouse_manager_service.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    LocationRepository locationRepository;

    public List<Location> getAllLocation() {
        return locationRepository.findByStatusTrue();
    }

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public <S extends Location> S create(S entity) {
        return locationRepository.save(entity);
    }

    public <S extends Location> S update(S entity) {
        return locationRepository.save(entity);
    }

//    public void deleteLocations(List<Long> locationIds) {
//        for(Long updateLocations : locationIds){
//            Optional<Location> locationOptinal = locationRepository.findById(updateLocations);
//            locationOptinal.ifPresent(location -> {
//                location.setStatus(false);
//                locationRepository.save(location);
//            });
//        }
//    }



//    public void deleteLocations1(List<Long> locationIds) {
//        List<Location> locationList = locationRepository.findAllById(locationIds);
//        locationList.stream().map(location -> {
//                location.setStatus(false);
//                return location;
//            });
//        locationRepository.saveAll(locationList);
//    }

    public void deleteLocations(List<Long> locationIds) {
        locationRepository.deleteLocations(locationIds);
    }
}
