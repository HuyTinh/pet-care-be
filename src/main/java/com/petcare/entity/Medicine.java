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

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(indexName = "medicines")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Medicine {

    @Field(type = FieldType.Long, name = "id")
    @Id
    long id;

    @Field(type = FieldType.Text, name = "name")
    String name;

    @Field(type = FieldType.Date, name = "manufacturing_date", pattern = "yyyy-MM-dd")
    Date manufacturingDate;

    @Field(type = FieldType.Date, name = "expiry_date", pattern = "yyyy-MM-dd")
    Date expiryDate;

    @Field(type = FieldType.Integer, name = "quantity")
    int quantity;

    @Field(type = FieldType.Double, name = "price")
    double price;

    @Field(type = FieldType.Text, name = "note")
    String note;

    @Field(type = FieldType.Boolean, name = "status")
    boolean status;

    @Field(type = FieldType.Long, name = "caculation_unit_id")
    long caculationUnit;

    @Field(type = FieldType.Long, name = "location_id")
    long locationId;

    @Field(type = FieldType.Long, name = "manufacturer_id")
    long manufacturerId;

}
