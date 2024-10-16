package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.Medicine;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.MedicineImpl;
import com.petcare.service.impl.ServiceTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/medicines")
@CrossOrigin("*")
public class MedicineController {

    @Autowired
    private MedicineImpl medicineService;

    @Autowired
    private ElasticSearchImpl elasticSearch;
    private static final String INDEXNAME = Medicine.class.getAnnotation(Document.class).indexName();

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
//    public List<Medicine> getAllMedicines() {
//        return medicineService.getAllEntity();
//    }

    @PostMapping()
    public Medicine addMedicine(@RequestBody Medicine medicine) {
        return medicineService.createEntity(medicine);
    }

//    @GetMapping("/{id}")
//    public Medicine getMedicine(@PathVariable long id) {
//        return medicineService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.MEDICINE_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deleteMedicine(@PathVariable long id) {
        medicineService.deleteEntity(id);
        return "Medicine deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicine> updateMedicine(@PathVariable long id, @RequestBody Medicine medicine) {
        if (medicine.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        Medicine updatedMedicine = medicineService.updateEntity(medicine);
        return ResponseEntity.ok(updatedMedicine);
    }
}

