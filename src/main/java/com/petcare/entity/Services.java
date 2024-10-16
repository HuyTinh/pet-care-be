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
@Document(indexName = "services")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Services {

    @Field(type = FieldType.Long, name = "id")
    @Id
    long id;

    @Field(type = FieldType.Text, name = "name")
    String name;

    @Field(type = FieldType.Double, name = "price")
    double price;

    @Field(type = FieldType.Text, name = "status")
    String status;

    @Field(type = FieldType.Long, name = "service_type_id")
    long serviceTypeId;

}