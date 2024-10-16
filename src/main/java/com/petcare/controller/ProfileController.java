package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.Profile;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.ProfileImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/profiles")
@CrossOrigin("*")
public class ProfileController {

    @Autowired
    private ProfileImpl profileService;

    @Autowired
    private ElasticSearchImpl elasticSearch;
    private static final String INDEXNAME = Profile.class.getAnnotation(Document.class).indexName();

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
//    public List<Profile> getAllProfiles() {
//        return profileService.getAllEntity();
//    }

    @PostMapping()
    public Profile addProfile(@RequestBody Profile profile) {
        return profileService.createEntity(profile);
    }

//    @GetMapping("/{id}")
//    public Profile getProfile(@PathVariable long id) {
//        return profileService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.PROFILE_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deleteProfile(@PathVariable long id) {
        profileService.deleteEntity(id);
        return "Profile deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateProfile(@PathVariable long id, @RequestBody Profile profile) {
        if (profile.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        Profile updatedProfile = profileService.updateEntity(profile);
        return ResponseEntity.ok(updatedProfile);
    }
}

