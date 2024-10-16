package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.Species;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.SpeciesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/species")
@CrossOrigin("*")
public class SpeciesController {

    @Autowired
    private SpeciesImpl speciesService;

    @Autowired
    private ElasticSearchImpl elasticSearch;
    private static final String INDEXNAME = Species.class.getAnnotation(Document.class).indexName();

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
//    public List<Species> getAllSpecies() {
//        return speciesService.getAllEntity();
//    }

    @PostMapping()
    public Species addSpecies(@RequestBody Species species) {
        return speciesService.createEntity(species);
    }

//    @GetMapping("/{id}")
//    public Species getSpecies(@PathVariable long id) {
//        return speciesService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.SPECIES_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deleteSpecies(@PathVariable long id) {
        speciesService.deleteEntity(id);
        return "Species deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Species> updateSpecies(@PathVariable long id, @RequestBody Species species) {
        if (species.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        Species updatedSpecies = speciesService.updateEntity(species);
        return ResponseEntity.ok(updatedSpecies);
    }
}

