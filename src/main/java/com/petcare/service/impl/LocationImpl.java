package com.petcare.service.impl;

import com.petcare.entity.Location;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.LocationRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationImpl implements EntityService<Location, Long> {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public List<Location> getAllEntity() {

        List<Location> locations = ArrayMapper.mapperIterableToList(locationRepository.findAll());

        return locations;
    }

    @Override
    public Optional<Location> getEntityById(Long id) {

        Optional<Location> location = locationRepository.findById(id);

        return location;
    }

    @Override
    public Location createEntity(Location location) {

        Optional<Location> locationOptional = locationRepository.findById(location.getId());
        if (locationOptional.isPresent()) {
            throw new APIException(ErrorCode.LOCATION_ALREADY_EXISTS);
        }

        return locationRepository.save(location);
    }

    @Override
    public Location updateEntity(Location location) {

        Location locationOptional = locationRepository.findById(location.getId())
                .orElseThrow(() -> new APIException(ErrorCode.LOCATION_NOT_FOUND));

        return locationRepository.save(location);
    }

    @Override
    public void deleteEntity(Long id) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.LOCATION_NOT_FOUND));

        locationRepository.delete(location);
    }
}
