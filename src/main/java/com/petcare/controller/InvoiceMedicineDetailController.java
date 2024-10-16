package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
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

