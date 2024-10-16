package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.entity.Appointment;
//import com.petcare.service.AppointmentService;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.request.SearchRequest;
import com.petcare.service.impl.AppointmentImpl;
import com.petcare.service.impl.ElasticSearchImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/appointment")
@CrossOrigin("*")
public class AppointmentController {

    @Autowired
    private AppointmentImpl appointmentService;

    @Autowired
    private ElasticSearchImpl elasticSearch;

    private static final String INDEXNAME = Appointment.class.getAnnotation(Document.class).indexName();

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
//    public List<Appointment> getAllAppointments() {
//
//        return appointmentService.getAllEntity();
//    }

    @PostMapping()
    public Appointment addAppointment(@RequestBody Appointment appointment) {

        Appointment createAppointment = appointmentService.createEntity(appointment);

        return createAppointment;
    }

//    @GetMapping("/{id}")
//    public Appointment getAppointment(@PathVariable long id) {
//
//        System.out.println("idd: " + id);
//        Appointment appointment = appointmentService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.APPOINTMENT_NOT_FOUND));
//
//        return appointment;
//    }

    @DeleteMapping("/{id}")
    public String deleteAppointment(@PathVariable long id) {

        appointmentService.deleteEntity(id);

        return "Deleted is successfully !";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable long id, @RequestBody Appointment appointment) {

        if (appointment.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        Appointment updatedAppointment = appointmentService.updateEntity(appointment);

        return ResponseEntity.ok(updatedAppointment);
    }

}
