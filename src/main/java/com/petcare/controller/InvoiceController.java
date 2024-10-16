package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.Invoice;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.InvoiceImpl;
import com.petcare.service.impl.ServiceTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/invoices")
@CrossOrigin("*")
public class InvoiceController {

    @Autowired
    private InvoiceImpl invoiceService;

    @Autowired
    private ElasticSearchImpl elasticSearch;
    private static final String INDEXNAME = Invoice.class.getAnnotation(Document.class).indexName();

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
//    public List<Invoice> getAllInvoices() {
//        return invoiceService.getAllEntity();
//    }

    @PostMapping()
    public Invoice addInvoice(@RequestBody Invoice invoice) {
        return invoiceService.createEntity(invoice);
    }

//    @GetMapping("/{id}")
//    public Invoice getInvoice(@PathVariable long id) {
//        return invoiceService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.INVOICE_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deleteInvoice(@PathVariable long id) {
        invoiceService.deleteEntity(id);
        return "Invoice deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable long id, @RequestBody Invoice invoice) {
        if (invoice.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        Invoice updatedInvoice = invoiceService.updateEntity(invoice);
        return ResponseEntity.ok(updatedInvoice);
    }
}

