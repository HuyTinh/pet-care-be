package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.dto.DataResponse;
import com.petcare.dto.PaginationResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.PrescriptionDetail;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.PrescriptionDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/prescription-details")
@CrossOrigin("*")
public class PrescriptionDetailController {

    @Autowired
    private PrescriptionDetailImpl prescriptionDetailService;

    @Autowired
    private ElasticSearchImpl elasticSearch;

    private static final String INDEXNAME = PrescriptionDetail.class.getAnnotation(Document.class).indexName();

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
//    public List<PrescriptionDetail> getAllPrescriptionDetails() {
//        return prescriptionDetailService.getAllEntity();
//    }

    @PostMapping()
    public PrescriptionDetail addPrescriptionDetail(@RequestBody PrescriptionDetail prescriptionDetail) {
        return prescriptionDetailService.createEntity(prescriptionDetail);
    }

//    @GetMapping("/{id}")
//    public PrescriptionDetail getPrescriptionDetail(@PathVariable long id) {
//        return prescriptionDetailService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.PRESCRIPTION_DETAIL_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deletePrescriptionDetail(@PathVariable long id) {
        prescriptionDetailService.deleteEntity(id);
        return "PrescriptionDetail deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionDetail> updatePrescriptionDetail(@PathVariable long id, @RequestBody PrescriptionDetail prescriptionDetail) {
        if (prescriptionDetail.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        PrescriptionDetail updatedPrescriptionDetail = prescriptionDetailService.updateEntity(prescriptionDetail);
        return ResponseEntity.ok(updatedPrescriptionDetail);
    }
}

