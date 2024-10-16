package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.Pet;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.PetImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/pets")
@CrossOrigin("*")
public class PetController {

    @Autowired
    private PetImpl petService;

    @Autowired
    private ElasticSearchImpl elasticSearch;
    private static final String INDEXNAME = Pet.class.getAnnotation(Document.class).indexName();

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
//    public List<Pet> getAllPets() {
//        return petService.getAllEntity();
//    }

    @PostMapping()
    public Pet addPet(@RequestBody Pet pet) {
        return petService.createEntity(pet);
    }

//    @GetMapping("/{id}")
//    public Pet getPet(@PathVariable long id) {
//        return petService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.PET_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deletePet(@PathVariable long id) {
        petService.deleteEntity(id);
        return "Pet deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable long id, @RequestBody Pet pet) {
        if (pet.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        Pet updatedPet = petService.updateEntity(pet);
        return ResponseEntity.ok(updatedPet);
    }
}

