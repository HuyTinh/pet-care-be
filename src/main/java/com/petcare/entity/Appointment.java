package com.petcare.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(indexName = "appointments")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Appointment {

    @Id
    @Field(type = FieldType.Long, name = "id")
    long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Field(type = FieldType.Date, name = "appointment_date", pattern = "yyyy-MM-dd", format = DateFormat.date)
    Date appointmentDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Field(type = FieldType.Date, name = "appointment_time", pattern = "HH:mm", format = DateFormat.hour_minute)
    Date appointmentTime;

    @Field(type = FieldType.Boolean, name = "status", index = false)
    boolean status;

    @Field(type = FieldType.Text, name = "status_accept")
    String statusAccept;

    @Field(type = FieldType.Long, name = "owner_id")
    long ownerId;


//    @Field(type = FieldType.Nested, name = "appointment_services")
//    List<AppointmentService> appointmentServices;

}
