package com.pet_care.appointment_service.service;

import com.pet_care.appointment_service.dto.response.PetResponse;
import com.pet_care.appointment_service.exception.APIException;
import com.pet_care.appointment_service.exception.ErrorCode;
import com.pet_care.appointment_service.mapper.PetMapper;
import com.pet_care.appointment_service.repository.PetRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetService {
    @NotNull PetRepository petRepository;
    @NotNull PetMapper petMapper;

    /**
     * @return
     */
    @Transactional(readOnly = true)
    public @NotNull List<PetResponse> getAllPet() {
        return petRepository.findAll().stream()
                .map(petMapper::toDto).collect(toList());
    }

    /**
     * @param petId
     * @return
     */
    @Transactional(readOnly = true)
    public PetResponse getPetById(@NotNull Long petId) {
        return petRepository.findById(petId).map(petMapper::toDto).orElseThrow(() -> new APIException(ErrorCode.PET_NOT_FOUND));
    }
}
