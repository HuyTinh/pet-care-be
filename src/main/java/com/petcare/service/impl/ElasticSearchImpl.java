package com.petcare.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.petcare.request.SearchRequest;
import com.petcare.utils.ElasticSearchUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class ElasticSearchImpl {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public SearchResponse<Map> matchAllService(String indexName) throws IOException {

        Supplier<Query> supplier = ElasticSearchUtils.supplierAll();
        SearchResponse<Map> searchResponse = elasticsearchClient.search(s -> s
                .index(indexName)
                .size(10000)
                .query(supplier.get()), Map.class);

        return searchResponse;
    }

    public SearchResponse<Map> matchByAny(String indexName, List<String> fieldValue) throws IOException {

        Supplier<Query> supplier = ElasticSearchUtils.supplierByAnyQuery(indexName, fieldValue);
        SearchResponse<Map> searchResponse = elasticsearchClient.search(s -> s
                .index(indexName)
                .size(1000)
                .query(supplier.get()), Map.class);

        return searchResponse;
    }


    public SearchResponse<Map> matchById(String indexName, long id) throws IOException {

        Supplier<Query> supplier = ElasticSearchUtils.supplierById(id);
        SearchResponse<Map> searchResponse = elasticsearchClient.search(s -> s
                .index(indexName)
                .query(supplier.get()), Map.class);

        return searchResponse;
    }

}
