package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.ServiceType;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.ServiceTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/service-types")
@CrossOrigin("*")
public class ServiceTypeController {

    @Autowired
    private ServiceTypeImpl serviceTypeService;

    @Autowired
    private ElasticSearchImpl elasticSearch;
    private static final String INDEXNAME = ServiceType.class.getAnnotation(Document.class).indexName();

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
//    public List<ServiceType> getAllServiceTypes() {
//        return serviceTypeService.getAllEntity();
//    }

    @PostMapping()
    public ServiceType addServiceType(@RequestBody ServiceType serviceType) {
        return serviceTypeService.createEntity(serviceType);
    }

//    @GetMapping("/{id}")
//    public ServiceType getServiceType(@PathVariable long id) {
//        return serviceTypeService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.SERVICE_TYPE_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deleteServiceType(@PathVariable long id) {
        serviceTypeService.deleteEntity(id);
        return "ServiceType deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceType> updateServiceType(@PathVariable long id, @RequestBody ServiceType serviceType) {
        if (serviceType.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        ServiceType updatedServiceType = serviceTypeService.updateEntity(serviceType);
        return ResponseEntity.ok(updatedServiceType);
    }
}
