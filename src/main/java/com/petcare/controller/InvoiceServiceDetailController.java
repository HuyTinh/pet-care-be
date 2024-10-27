package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.dto.DataResponse;
import com.petcare.dto.PaginationResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.InvoiceServiceDetail;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.InvoiceServiceDetailImpl;
import com.petcare.service.impl.ServiceTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/invoice-service-details")
@CrossOrigin("*")
public class InvoiceServiceDetailController {

    @Autowired
    private InvoiceServiceDetailImpl invoiceServiceDetailService;

    @Autowired
    private ElasticSearchImpl elasticSearch;

    private static final String INDEXNAME = InvoiceServiceDetail.class.getAnnotation(Document.class).indexName();

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
//    public List<InvoiceServiceDetail> getAllInvoiceServiceDetails() {
//        return invoiceServiceDetailService.getAllEntity();
//    }

    @PostMapping()
    public InvoiceServiceDetail addInvoiceServiceDetail(@RequestBody InvoiceServiceDetail invoiceServiceDetail) {
        return invoiceServiceDetailService.createEntity(invoiceServiceDetail);
    }

//    @GetMapping("/{id}")
//    public InvoiceServiceDetail getInvoiceServiceDetail(@PathVariable long id) {
//        return invoiceServiceDetailService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.INVOICE_SERVICE_DETAIL_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deleteInvoiceServiceDetail(@PathVariable long id) {
        invoiceServiceDetailService.deleteEntity(id);
        return "Invoice Service Detail deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceServiceDetail> updateInvoiceServiceDetail(@PathVariable long id, @RequestBody InvoiceServiceDetail invoiceServiceDetail) {
        if (invoiceServiceDetail.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        InvoiceServiceDetail updatedInvoiceServiceDetail = invoiceServiceDetailService.updateEntity(invoiceServiceDetail);
        return ResponseEntity.ok(updatedInvoiceServiceDetail);
    }
}

