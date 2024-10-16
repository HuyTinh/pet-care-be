package com.petcare.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(indexName = "owners")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Owner {

    @Id
    long id;

    @Field(type = FieldType.Text, name = "first_name")
    String firstName;

    @Field(type = FieldType.Text, name = "last_name")
    String lastName;

    @Field(type = FieldType.Text, name = "phone_number")
    String phoneNumber;

    @Field(type = FieldType.Boolean, name = "status")
    boolean status;

    @Field(type = FieldType.Long, name = "account_id")
    long accountId;

//    @Field(type = FieldType.Nested, name = "appointment")
////    @JsonIgnore
//    List<Appointment> appointment;

}
