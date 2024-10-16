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
@Document(indexName = "profiles")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile {

    @Field(type = FieldType.Long, name = "id")
    @Id
    long id;

    @Field(type = FieldType.Text, name = "last_name")
    String lastName;

    @Field(type = FieldType.Text, name = "first_name")
    String firstName;

    @Field(type = FieldType.Text, name = "email")
    String email;

    @Field(type = FieldType.Text, name = "phone_number")
    String phoneNumber;

    @Field(type = FieldType.Boolean, name = "gender")
    boolean gender;

    @Field(type = FieldType.Text, name = "address")
    String address;

    @Field(type = FieldType.Boolean, name = "status")
    boolean status;

    @Field(type = FieldType.Long, name = "role_id")
    long roleId;

    @Field(type = FieldType.Long, name = "account_id")
    long accountId;

}
