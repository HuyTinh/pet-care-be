package com.pet_care.search_service.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.pet_care.search_service.model.Pet;
import com.pet_care.search_service.util.ESUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ESService {

    ElasticsearchClient elasticsearchClient;

    public SearchResponse<Pet> autoSuggestPet(String partialPetName) throws IOException {
        Supplier<Query> supplier = ESUtil.createSupplierAutoSuggest(partialPetName);
        SearchResponse<Pet> searchResponse = elasticsearchClient.search(s -> s.index("pets").query(supplier.get()), Pet.class);

        log.info("Elasticsearch auto suggestion query: {}", searchResponse.toString());
        return searchResponse;
    }
}
