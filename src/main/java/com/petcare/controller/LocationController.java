package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.Location;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.LocationImpl;
import com.petcare.service.impl.ServiceTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/locations")
@CrossOrigin("*")
public class LocationController {

    @Autowired
    private LocationImpl locationService;

    @Autowired
    private ElasticSearchImpl elasticSearch;
    private static final String INDEXNAME = Location.class.getAnnotation(Document.class).indexName();

    @GetMapping()
    public ResponseEntity<?> matchAll() throws IOException {
        SearchResponse<Map> searchResponse = elasticSearch.matchAllService(INDEXNAME);
        List<Map> responses = SearchResponseMapper.mappSearchResponseToListMap(searchResponse);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Map>> matchById(@PathVariable long id) throws IOException {
        SearchResponse<Map> searchResponse = elasticSearch.matchById(INDEXNAME, id);
        List<Map> responses = SearchResponseMapper.mappSearchResponseToListMap(searchResponse);

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/getByAny")
    public ResponseEntity<?> matchByAny(@RequestBody List<String> searchRequest) throws IOException {
        SearchResponse<Map> searchResponse = elasticSearch.matchByAny(INDEXNAME, searchRequest);
        List<Map> responses = SearchResponseMapper.mappSearchResponseToListMap(searchResponse);

        return ResponseEntity.ok(responses);
    }

//    @GetMapping()
//    public List<Location> getAllLocations() {
//        return locationService.getAllEntity();
//    }

    @PostMapping()
    public Location addLocation(@RequestBody Location location) {
        return locationService.createEntity(location);
    }

//    @GetMapping("/{id}")
//    public Location getLocation(@PathVariable long id) {
//        return locationService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.LOCATION_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deleteLocation(@PathVariable long id) {
        locationService.deleteEntity(id);
        return "Location deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable long id, @RequestBody Location location) {
        if (location.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        Location updatedLocation = locationService.updateEntity(location);
        return ResponseEntity.ok(updatedLocation);
    }
}

