package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.Prescription;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.PrescriptionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/prescriptions")
@CrossOrigin("*")
public class PrescriptionController {

    @Autowired
    private PrescriptionImpl prescriptionService;

    @Autowired
    private ElasticSearchImpl elasticSearch;
    private static final String INDEXNAME = Prescription.class.getAnnotation(Document.class).indexName();

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
//    public List<Prescription> getAllPrescriptions() {
//        return prescriptionService.getAllEntity();
//    }

    @PostMapping()
    public Prescription addPrescription(@RequestBody Prescription prescription) {
        return prescriptionService.createEntity(prescription);
    }

//    @GetMapping("/{id}")
//    public Prescription getPrescription(@PathVariable long id) {
//        return prescriptionService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.PRESCRIPTION_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deletePrescription(@PathVariable long id) {
        prescriptionService.deleteEntity(id);
        return "Prescription deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable long id, @RequestBody Prescription prescription) {
        if (prescription.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        Prescription updatedPrescription = prescriptionService.updateEntity(prescription);
        return ResponseEntity.ok(updatedPrescription);
    }
}

