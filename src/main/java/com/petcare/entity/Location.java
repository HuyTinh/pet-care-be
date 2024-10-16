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
@Document(indexName = "locations")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {

    @Field(type = FieldType.Long, name = "id")
    @Id
    long id;

    @Field(type = FieldType.Text, name = "area")
    String area;

    @Field(type = FieldType.Text, name = "row_location")
    String rowLocation;

    @Field(type = FieldType.Text, name = "column_location")
    String columnLocation;

    @Field(type = FieldType.Boolean, name = "status")
    boolean status;

}
