package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.dto.DataResponse;
import com.petcare.dto.PaginationResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.InvoiceMedicineDetail;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.InvoiceMedicineDetailImpl;
import com.petcare.service.impl.ServiceTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/invoice-medicine-details")
@CrossOrigin("*")
public class InvoiceMedicineDetailController {

    @Autowired
    private InvoiceMedicineDetailImpl invoiceMedicineDetailService;

    @Autowired
    private ElasticSearchImpl elasticSearch;

    private static final String INDEXNAME = InvoiceMedicineDetail.class.getAnnotation(Document.class).indexName();

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
//    public List<InvoiceMedicineDetail> getAllInvoiceMedicineDetails() {
//        return invoiceMedicineDetailService.getAllEntity();
//    }

    @PostMapping()
    public InvoiceMedicineDetail addInvoiceMedicineDetail(@RequestBody InvoiceMedicineDetail invoiceMedicineDetail) {
        return invoiceMedicineDetailService.createEntity(invoiceMedicineDetail);
    }

//    @GetMapping("/{id}")
//    public InvoiceMedicineDetail getInvoiceMedicineDetail(@PathVariable long id) {
//        return invoiceMedicineDetailService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.INVOICE_MEDICINE_DETAIL_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deleteInvoiceMedicineDetail(@PathVariable long id) {
        invoiceMedicineDetailService.deleteEntity(id);
        return "Invoice Medicine Detail deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceMedicineDetail> updateInvoiceMedicineDetail(@PathVariable long id, @RequestBody InvoiceMedicineDetail invoiceMedicineDetail) {
        if (invoiceMedicineDetail.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        InvoiceMedicineDetail updatedInvoiceMedicineDetail = invoiceMedicineDetailService.updateEntity(invoiceMedicineDetail);
        return ResponseEntity.ok(updatedInvoiceMedicineDetail);
    }
}

