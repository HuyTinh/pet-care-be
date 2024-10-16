package com.petcare.utils;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.util.ObjectBuilder;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ElasticSearchUtils {

    public static final String STATUS = "status";
    public static final String ID = "id";
    public static final Boolean STATUS_VALUE = true;

    public static String getStringValues(List<FieldValue> valuesSearch){

        return valuesSearch.stream()
                .map(FieldValue::stringValue)
                .collect(Collectors.joining(" "));
    }

    public static MatchAllQuery matchAllQuery() {

        val matchAllQuery = new MatchAllQuery.Builder();

        return matchAllQuery.build();
    }

    public static MatchQuery matchQuery(String fieldSearch, List<FieldValue> valuesSearch) {

        val matchQuery = new MatchQuery.Builder();
        matchQuery.field(fieldSearch);
        matchQuery.query(getStringValues(valuesSearch));

        return matchQuery.build();
    }

    public static MultiMatchQuery multiMatchQuery(List<String> fieldSearch, List<FieldValue> valuesSearch) {

        val multiMatchQuery = new MultiMatchQuery.Builder();

        multiMatchQuery.fields(fieldSearch);
        if (valuesSearch.size() > 1) {
            multiMatchQuery.query(getStringValues(valuesSearch));
        } else {
            multiMatchQuery.query(valuesSearch.get(0).stringValue());
        }

        return multiMatchQuery.build();
    }

    public static TermsQueryField termsQueryField(List<FieldValue> valuesSearch) {

        val termsQueryField = new TermsQueryField.Builder();
        termsQueryField.value(valuesSearch);

        return termsQueryField.build();
    }

    public static TermsQuery termsQuery(String fieldSearch, List<FieldValue> valuesSearch) {

        val termsQuery = new TermsQuery.Builder();
        termsQuery.field(fieldSearch);
        if (valuesSearch.size() > 1) {
            termsQueryField(valuesSearch);
        } else {
            termsQuery.field(valuesSearch.get(0).stringValue());
        }

        return termsQuery.build();
    }

    public static TermQuery termQuery(String fieldSearch, FieldValue valuesSearch) {

        val termQuery = new TermQuery.Builder();
        termQuery.field(fieldSearch);
        termQuery.value(valuesSearch);

        return termQuery.build();
    }

    public static WildcardQuery wildcardQuery(String fieldSearch, String valuesSearch) {

        val wildcardQuery = new WildcardQuery.Builder();
        wildcardQuery.field(fieldSearch);
        wildcardQuery.value(valuesSearch);

        return wildcardQuery.build();
    }

    public static Supplier<Query> supplierAll() {

        Supplier<Query> supplier = () -> Query.of(q -> q.matchAll(matchAllQuery()));

        return supplier;
    }

    public static Supplier<Query> supplierByMatchQuery(String fieldValue) {

        Supplier<Query> supplier = () -> Query.of(q -> q
                .multiMatch(m -> m
                        .fields("*")
                        .query(fieldValue)
                        .type(TextQueryType.BestFields)
                )
        );

        return supplier;
    }

    public static Supplier<Query> supplierById(long id){

        return () -> Query.of(q -> q
                .term(termQuery(ID, FieldValue.of(id)))
        );
    }

    public static Supplier<Query> supplierByAnyQuery(String indexName, List<String> fieldValues) {

        List<FieldValue> valuesSearch = fieldValues.stream()
                .map(FieldValue::of)
                .collect(Collectors.toList());

        List<String> fieldSearch = getFieldNameInEntity(indexName);

//        return () -> Query.of(q -> q
//                .multiMatch(multiMatchQuery(fieldSearch, valuesSearch))
//        );
        return () -> Query.of(q -> q
                .bool(b -> {
                    fieldSearch.forEach(field ->
                            valuesSearch.forEach(value -> {
                                String wildcardValue = "*" + value.stringValue() + "*";
                                b.should(s -> s.wildcard(wildcardQuery(field, wildcardValue)));
                            })
                    );

                    b.filter(f -> f
                            .term(termQuery(STATUS, FieldValue.of(STATUS_VALUE)))
                    );
                    return b;
                })
        );
    }

    public static List<String> getFieldNameInEntity(String indexName) {

        List<String> fieldNames = new ArrayList<>();
//        fieldNames.add("id");

        switch (indexName) {

            case "accounts":
                fieldNames.add("email");
                break;

            case "appointments":
                fieldNames.add("appointmentDate");
                fieldNames.add("appointmentTime");
                fieldNames.add("status_accept");
                break;

            case "appointment_services":
                break;

            case "caculation_units":
                fieldNames.add("name");
                break;

            case "invoices":
                fieldNames.add("createDate");
                fieldNames.add("note");
                fieldNames.add("total");
                fieldNames.add("paymentStatus");
                break;

            case "invoice_medicine_details":
                fieldNames.add("quantity");
                fieldNames.add("price");
                fieldNames.add("note");
                break;

            case "invoice_service_details":
                fieldNames.add("discount");
                fieldNames.add("price");
                fieldNames.add("note");
                break;

            case "locations":
                fieldNames.add("area");
                fieldNames.add("rowLocation");
                fieldNames.add("columnLocation");
                break;

            case "manufacturers":
                fieldNames.add("name");
                break;

            case "medicines":
                fieldNames.add("name");
                fieldNames.add("manufacturingDate");
                fieldNames.add("expiryDate");
                fieldNames.add("quantity");
                fieldNames.add("price");
                fieldNames.add("note");
                break;

            case "owners":
                fieldNames.add("firstName");
                fieldNames.add("lastName");
                fieldNames.add("phoneNumber");
                break;

            case "payments":
                fieldNames.add("name");
                break;

            case "pets":
                fieldNames.add("name");
                fieldNames.add("age");
                fieldNames.add("weight");
                fieldNames.add("note");
                break;

            case "prescriptions":
                fieldNames.add("createDate");
                fieldNames.add("note");
                break;

            case "prescription_details":
                fieldNames.add("note");
                fieldNames.add("quantity");
                break;

            case "profiles":
                fieldNames.add("lastName");
                fieldNames.add("firstName");
                fieldNames.add("email");
                fieldNames.add("phoneNumber");
                fieldNames.add("gender");
                fieldNames.add("address");
                break;

            case "roles":
                fieldNames.add("name");
                break;

            case "services":
                fieldNames.add("name");
                fieldNames.add("price");
                break;

            case "service_types":
                fieldNames.add("name");
                break;

            case "species":
                fieldNames.add("name");
                break;

            default:
                throw new APIException(ErrorCode.INVALID_INDEX_NAME);
        }
        System.out.println("f: " +fieldNames);
        return fieldNames;
    }

}
