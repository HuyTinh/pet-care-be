package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.Owner;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.OwnerImpl;
import com.petcare.service.impl.ServiceTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/owners")
@CrossOrigin("*")
public class OwnerController {

    @Autowired
    private OwnerImpl ownerService;

    @Autowired
    private ElasticSearchImpl elasticSearch;
    private static final String INDEXNAME = Owner.class.getAnnotation(Document.class).indexName();

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
//    public List<Owner> getAllOwners() {
//        return ownerService.getAllEntity();
//    }

    @PostMapping()
    public Owner addOwner(@RequestBody Owner owner) {
        return ownerService.createEntity(owner);
    }

//    @GetMapping("/{id}")
//    public Owner getOwner(@PathVariable long id) {
//        return ownerService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.OWNER_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deleteOwner(@PathVariable long id) {
        ownerService.deleteEntity(id);
        return "Owner deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Owner> updateOwner(@PathVariable long id, @RequestBody Owner owner) {
        if (owner.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        Owner updatedOwner = ownerService.updateEntity(owner);
        return ResponseEntity.ok(updatedOwner);
    }
}

