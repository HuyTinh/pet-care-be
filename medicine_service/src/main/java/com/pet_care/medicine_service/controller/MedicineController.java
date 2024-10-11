package com.pet_care.medicine_service.controller;

import com.pet_care.medicine_service.dto.request.MedicineCreateRequest;
import com.pet_care.medicine_service.dto.request.MedicineUpdateRequest;
import com.pet_care.medicine_service.dto.response.APIResponse;
import com.pet_care.medicine_service.model.Medicine;
import com.pet_care.medicine_service.service.MedicineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("medicine")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MedicineController {

    @NotNull MedicineService medicineService;

    @GetMapping
    public APIResponse<List<Medicine>> getAllMedicine() {
        return APIResponse.<List<Medicine>>builder()
                .data(medicineService.getAllMedicine())
                .build();
    }

    @GetMapping("{medicineId}")
    public APIResponse<Medicine> getMedicineById(@NotNull @PathVariable("medicineId") Long medicineId) {
        return APIResponse.<Medicine>builder()
                .data(medicineService.getMedicineById(medicineId))
                .build();
    }

    @PostMapping
    public APIResponse<Medicine> createMedicine(@NotNull @RequestBody MedicineCreateRequest medicineCreateRequest) {
        return APIResponse.<Medicine>builder()
                .data(medicineService.createMedicine(medicineCreateRequest))
                .build();
    }

    @PutMapping("/{medicineId}")
    public APIResponse<Medicine> updateMedicine(@NotNull @PathVariable("medicineId") Long medicineId, @NotNull @RequestBody MedicineUpdateRequest medicineUpdateRequest) {
        return APIResponse.<Medicine>builder()
                .data(medicineService.updateMedicine(medicineId, medicineUpdateRequest))
                .build();
    }

    @DeleteMapping("/{medicineId}")
    public APIResponse<Medicine> deleteMedicine(@NotNull @PathVariable("medicineId") Long medicineId, @RequestBody MedicineUpdateRequest medicineUpdateRequest) {
        medicineService.deleteMedicine(medicineId);
        return APIResponse.<Medicine>builder()
                .message("Delete medicine successful")
                .build();
    }

}
