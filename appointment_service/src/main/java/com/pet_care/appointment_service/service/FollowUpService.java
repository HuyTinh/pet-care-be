package com.pet_care.appointment_service.service;

import com.pet_care.appointment_service.model.FollowUp;
import com.pet_care.appointment_service.repository.FollowUpRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Indicates that this is a service class
@RequiredArgsConstructor // Lombok annotation to automatically create a constructor with required fields
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Lombok to set field access level to private and final
public class FollowUpService {
    FollowUpRepository followUpRepository;

    public List<FollowUp> findAll() {
        return followUpRepository.findAll();
    }
}
