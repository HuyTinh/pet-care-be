package com.petcare.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.entity.Account;
import com.petcare.entity.Appointment;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.SearchResponseMapper;
import com.petcare.request.SearchRequest;
import com.petcare.service.impl.AccountImpl;
import com.petcare.service.impl.ElasticSearchImpl;
import com.petcare.service.impl.ServiceTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/accounts")
@CrossOrigin("*")
public class AccountController {

    @Autowired
    private AccountImpl accountService;

    @Autowired
    private ElasticSearchImpl elasticSearch;

    private static final String INDEXNAME = Account.class.getAnnotation(Document.class).indexName();

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
//    public List<Account> getAllAccounts() {
//        return accountService.getAllEntity();
//    }

    @PostMapping()
    public Account addAccount(@RequestBody Account account) {
        return accountService.createEntity(account);
    }

//    @GetMapping("/{id}")
//    public Account getAccount(@PathVariable long id) {
//        return accountService.getEntityById(id)
//                .orElseThrow(() -> new APIException(ErrorCode.ACCOUNT_NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable long id) {
        accountService.deleteEntity(id);
        return "Account deleted successfully!";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable long id, @RequestBody Account account) {
        if (account.getId() != id) {
            throw new APIException(ErrorCode.INVALID_ID);
        }
        Account updatedAccount = accountService.updateEntity(account);

        return ResponseEntity.ok(updatedAccount);
    }
}