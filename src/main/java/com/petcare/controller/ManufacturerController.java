package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.Manufacturer;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.ManufacturerImpl;
import com.petcare.service.impl.ServiceTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/manufacturers")
@CrossOrigin("*")
public class ManufacturerController {

    @Autowired
    private ManufacturerImpl manufacturerService;

    @Autowired
    private ElasticSearchImpl elasticSearch;
    private static final String INDEXNAME = Manufacturer.class.getAnnotation(Document.class).indexName();

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
//    public List<Manufacturer> getAllManufacturers() {
//        return manufacturerService.getAllEntity();
//    }

    @PostMapping()
    public Manufacturer addManufacturer(@RequestBody Manufacturer manufacturer) {
        return manufacturerService.createEntity(manufacturer);
    }

//    @GetMapping("/{id}")
//    public Manufacturer getManufacturer(@PathVariable long id) {
//        return manufacturerService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.MANUFACTURER_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deleteManufacturer(@PathVariable long id) {
        manufacturerService.deleteEntity(id);
        return "Manufacturer deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Manufacturer> updateManufacturer(@PathVariable long id, @RequestBody Manufacturer manufacturer) {
        if (manufacturer.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        Manufacturer updatedManufacturer = manufacturerService.updateEntity(manufacturer);
        return ResponseEntity.ok(updatedManufacturer);
    }
}

