package com.pet_care.appointment_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentBookingSuccessful extends SendTo {

    @JsonProperty("appointment_id")
    Long appointmentId;

    @Builder.Default
    String subject = "Appointment Confirmation!";

    String content;

    @JsonProperty("appointment_date")
    String appointmentDate;

    @JsonProperty("appointment_time")
    String appointmentTime;

    public String getContent() {
        return "{ \"to\": [ { \"email\": \"" + this.toEmail + " \"} ], \"subject\": \"" + this.subject + "\", \"params\": { \"username\": \"" + this.firstName + " " + this.lastName + "\", \"appointment_date\": \"" + this.appointmentDate + "\", \"appointment_time\": \"" + this.appointmentTime + "\", \"appointment_qr\": \"https://api.qrserver.com/v1/create-qr-code/?size=64x64&data="+this.appointmentId+"\"  }, \"templateId\": 1 }";
    }
}
