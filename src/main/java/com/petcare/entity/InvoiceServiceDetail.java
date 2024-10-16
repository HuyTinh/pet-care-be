package com.petcare.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(indexName = "invoice_service_details")
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceServiceDetail {

    @Field(type = FieldType.Long, name = "id")
    @Id
    long id;

    @Field(type = FieldType.Double, name = "discount")
    double discount;

    @Field(type = FieldType.Double, name = "price")
    double price;

    @Field(type = FieldType.Text, name = "note")
    String note;

    @Field(type = FieldType.Long, name = "service_id")
    long serviceId;

    @Field(type = FieldType.Long, name = "invoice_id")
    long invoiceId;



}
