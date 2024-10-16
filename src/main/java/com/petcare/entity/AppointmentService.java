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
@Document(indexName = "appointment_services")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentService {

    @Id
    @Field(type = FieldType.Long, name = "id")
    long id;

    @Field(type = FieldType.Long, name = "appointment_id")
    long appointmentId;

    @Field(type = FieldType.Long, name = "service_id")
    long serviceId;

}
