package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.entity.AppointmentService;
import com.petcare.entity.Role;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.RoleImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/roles")
@CrossOrigin("*")
public class RoleController {

    @Autowired
    private RoleImpl roleService;

    @Autowired
    private ElasticSearchImpl elasticSearch;
    private static final String INDEXNAME = Role.class.getAnnotation(Document.class).indexName();

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
//    public List<Role> getAllRoles() {
//        return roleService.getAllEntity();
//    }

    @PostMapping()
    public Role addRole(@RequestBody Role role) {
        return roleService.createEntity(role);
    }

//    @GetMapping("/{id}")
//    public Role getRole(@PathVariable long id) {
//        return roleService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.ROLE_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deleteRole(@PathVariable long id) {
        roleService.deleteEntity(id);
        return "Role deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable long id, @RequestBody Role role) {
        if (role.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        Role updatedRole = roleService.updateEntity(role);
        return ResponseEntity.ok(updatedRole);
    }
}
