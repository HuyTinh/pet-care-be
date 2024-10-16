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

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(indexName = "pets")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pet {

    @Field(type = FieldType.Long, name = "id")
    @Id
    long id;

    @Field(type = FieldType.Text, name = "name")
    String name;

    @Field(type = FieldType.Scaled_Float, name = "age", scalingFactor = 100)
    BigDecimal age;

    @Field(type = FieldType.Scaled_Float, name = "weight", scalingFactor = 100)
    BigDecimal weight;

    @Field(type = FieldType.Text, name = "note")
    String note;

    @Field(type = FieldType.Boolean, name = "status")
    boolean status;

    @Field(type = FieldType.Long, name = "owner_id")
    long ownerId;

}
