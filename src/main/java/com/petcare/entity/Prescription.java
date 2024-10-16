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

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(indexName = "prescriptions")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Prescription {

    @Field(type = FieldType.Long, name = "id")
    @Id
    long id;

    @Field(type = FieldType.Date, name = "create_date", pattern = "yyyy-MM-dd")
    Date createDate;

    @Field(type = FieldType.Text, name = "note")
    String note;

    @Field(type = FieldType.Boolean, name = "status")
    boolean status;

    @Field(type = FieldType.Long, name = "pet_id")
    long petId;

    @Field(type = FieldType.Long, name = "profile_id")
    long profileId;

}
