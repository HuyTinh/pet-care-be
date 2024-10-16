package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.AppointmentServiceImpl;
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
@RequestMapping("api/v1/appointment-services")
@CrossOrigin("*")
public class AppointmentServiceController {

    @Autowired
    private AppointmentServiceImpl appointmentServiceService;

    @Autowired
    private ElasticSearchImpl elasticSearch;
    private static final String INDEXNAME = AppointmentService.class.getAnnotation(Document.class).indexName();

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
//    public List<AppointmentService> getAllAppointmentServices() {
//        return appointmentServiceService.getAllEntity();
//    }

    @PostMapping()
    public AppointmentService addAppointmentService(@RequestBody AppointmentService appointmentService) {
        return appointmentServiceService.createEntity(appointmentService);
    }

//    @GetMapping("/{id}")
//    public AppointmentService getAppointmentService(@PathVariable long id) {
//        return appointmentServiceService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.APPOINTMENT_SERVICE_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deleteAppointmentService(@PathVariable long id) {
        appointmentServiceService.deleteEntity(id);
        return "Appointment Service deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentService> updateAppointmentService(@PathVariable long id, @RequestBody AppointmentService appointmentService) {
        if (appointmentService.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        AppointmentService updatedAppointmentService = appointmentServiceService.updateEntity(appointmentService);
        return ResponseEntity.ok(updatedAppointmentService);
    }
}

