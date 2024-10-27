package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.dto.DataResponse;
import com.petcare.dto.PaginationResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.CaculationUnit;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.CaculationUnitImpl;
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
@RequestMapping("api/v1/caculation-units")
@CrossOrigin("*")
public class CaculationUnitController {

    @Autowired
    private CaculationUnitImpl caculationUnitService;

    @Autowired
    private ElasticSearchImpl elasticSearch;

    private static final String INDEXNAME = CaculationUnit.class.getAnnotation(Document.class).indexName();

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
//    public List<CaculationUnit> getAllCaculationUnits() {
//        return caculationUnitService.getAllEntity();
//    }

    @PostMapping()
    public CaculationUnit addCaculationUnit(@RequestBody CaculationUnit caculationUnit) {
        return caculationUnitService.createEntity(caculationUnit);
    }

//    @GetMapping("/{id}")
//    public CaculationUnit getCaculationUnit(@PathVariable long id) {
//        return caculationUnitService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.CACULATION_UNIT_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deleteCaculationUnit(@PathVariable long id) {
        caculationUnitService.deleteEntity(id);
        return "Caculation Unit deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<CaculationUnit> updateCaculationUnit(@PathVariable long id, @RequestBody CaculationUnit caculationUnit) {
        if (caculationUnit.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        CaculationUnit updatedCaculationUnit = caculationUnitService.updateEntity(caculationUnit);
        return ResponseEntity.ok(updatedCaculationUnit);
    }
}

