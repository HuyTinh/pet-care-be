package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.Services;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/services")
@CrossOrigin("*")
public class ServicesController {

    @Autowired
    private ServiceImpl serviceService;

    @Autowired
    private ElasticSearchImpl elasticSearch;
    private static final String INDEXNAME = Services.class.getAnnotation(Document.class).indexName();

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
//    public List<Services> getAllServices() {
//        return serviceService.getAllEntity();
//    }

    @PostMapping()
    public Services addService(@RequestBody Services service) {
        return serviceService.createEntity(service);
    }

//    @GetMapping("/{id}")
//    public Services getService(@PathVariable long id) {
//        return serviceService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.SERVICE_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deleteService(@PathVariable long id) {
        serviceService.deleteEntity(id);
        return "Service deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Services> updateService(@PathVariable long id, @RequestBody Services service) {
        if (service.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        Services updatedService = serviceService.updateEntity(service);
        return ResponseEntity.ok(updatedService);
    }
}

