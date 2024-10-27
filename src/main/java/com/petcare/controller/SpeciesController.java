package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.dto.DataResponse;
import com.petcare.dto.PaginationResponse;
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

    @GetMapping("/getAll/{records}")
    public ResponseEntity<?> matchAll(@PathVariable int records) throws IOException {

        SearchResponse<Map> searchResponse = elasticSearch.matchAllService(INDEXNAME, records);
        List<Map> listSearch = SearchResponseMapper.mappSearchResponseToListMap(searchResponse);

        SearchResponse<Map> matchAllServiceGetPage = elasticSearch.matchAllServiceGetPage(INDEXNAME);
        int totalPages = elasticSearch.matchTotalPages(matchAllServiceGetPage);

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setCurrentPage(records);
        paginationResponse.setTotalPages(totalPages);
        paginationResponse.setDataResponse(listSearch);

        DataResponse response = new DataResponse();
        response.setData(paginationResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> matchAllNoRecords() throws IOException {

        SearchResponse<Map> searchResponse = elasticSearch.matchAllService(INDEXNAME, 1);
        List<Map> listSearch = SearchResponseMapper.mappSearchResponseToListMap(searchResponse);

        SearchResponse<Map> matchAllServiceGetPage = elasticSearch.matchAllServiceGetPage(INDEXNAME);
        int totalPages = elasticSearch.matchTotalPages(matchAllServiceGetPage);

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setCurrentPage(1);
        paginationResponse.setTotalPages(totalPages);
        paginationResponse.setDataResponse(listSearch);

        DataResponse response = new DataResponse();
        response.setData(paginationResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> matchById(@PathVariable long id) throws IOException {

        SearchResponse<Map> searchResponse = elasticSearch.matchById(INDEXNAME, id);
        List<Map> listSearchResponses = SearchResponseMapper.mappSearchResponseToListMap(searchResponse);

        DataResponse response = new DataResponse();
        response.setData(listSearchResponses);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/getByAny/{records}")
    public ResponseEntity<?> matchByAny(@PathVariable int records,
                                        @RequestBody List<String> searchRequest) throws IOException {
        SearchResponse<Map> searchResponse = elasticSearch.matchByAny(INDEXNAME, searchRequest, records);
        List<Map> listSearchResponses = SearchResponseMapper.mappSearchResponseToListMap(searchResponse);

        DataResponse response = new DataResponse();
        response.setData(listSearchResponses);

        return ResponseEntity.ok(response);
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

