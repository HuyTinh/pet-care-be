package com.pet_care.search_service.util;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhrasePrefixQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.val;

import java.util.function.Supplier;

public class ESUtil {

    public static Supplier<Query> createSupplierAutoSuggest(String partialPetName) {

        return () -> Query.of(q -> q.matchPhrasePrefix(createAutoSuggestMatchQuery(partialPetName)));
    }

    public static MatchPhrasePrefixQuery createAutoSuggestMatchQuery(String partialPetName) {
        val autoSuggestQuery = new MatchPhrasePrefixQuery.Builder();
        return autoSuggestQuery
                .field("name")
                .query(partialPetName)
                .analyzer("standard")
                .build();
    }
}
