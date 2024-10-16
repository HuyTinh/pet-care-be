package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.Payment;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.PaymentImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/payments")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private PaymentImpl paymentService;

    @Autowired
    private ElasticSearchImpl elasticSearch;
    private static final String INDEXNAME = Payment.class.getAnnotation(Document.class).indexName();

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
//    public List<Payment> getAllPayments() {
//        return paymentService.getAllEntity();
//    }

    @PostMapping()
    public Payment addPayment(@RequestBody Payment payment) {
        return paymentService.createEntity(payment);
    }

//    @GetMapping("/{id}")
//    public Payment getPayment(@PathVariable long id) {
//        return paymentService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.PAYMENT_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deletePayment(@PathVariable long id) {
        paymentService.deleteEntity(id);
        return "Payment deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable long id, @RequestBody Payment payment) {
        if (payment.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        Payment updatedPayment = paymentService.updateEntity(payment);
        return ResponseEntity.ok(updatedPayment);
    }
}

