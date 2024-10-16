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
@Document(indexName = "prescription_details")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrescriptionDetail {

    @Field(type = FieldType.Long, name = "id")
    @Id
    long id;

    @Field(type = FieldType.Text, name = "note")
    String note;

    @Field(type = FieldType.Text, name = "quantity")
    String quantity;

    @Field(type = FieldType.Long, name = "medicine_id")
    long medicineId;

    @Field(type = FieldType.Long, name = "prescription_id")
    long prescriptionId;

}
