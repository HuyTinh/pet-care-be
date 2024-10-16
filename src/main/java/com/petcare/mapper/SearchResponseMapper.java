package com.petcare.mapper;

import co.elastic.clients.elasticsearch.core.SearchResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchResponseMapper {

    public static List<Map> mappSearchResponseToListMap(SearchResponse<Map> searchResponse) {

        List<Map> maps = searchResponse.hits().hits().stream()
                .map(data -> data.source())
                .collect(Collectors.toList());

        return maps;
    }

}
