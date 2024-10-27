package com.pet_care.medicine_service.controller;

import com.pet_care.medicine_service.dto.request.MedicineCreateRequest;
import com.pet_care.medicine_service.dto.request.MedicineUpdateRequest;
import com.pet_care.medicine_service.dto.response.APIResponse;
import com.pet_care.medicine_service.dto.response.MedicineResponse;
import com.pet_care.medicine_service.model.Medicine;
import com.pet_care.medicine_service.service.MedicineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("medicine")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MedicineController {

    @NotNull MedicineService medicineService;

    /**
     * @return
     */
    @GetMapping
    public APIResponse<List<MedicineResponse>> getAllMedicine() {
        return APIResponse.<List<MedicineResponse>>builder()
                .data(medicineService.getAllMedicine())
                .build();
    }

    /**
     * @param medicineId
     * @return
     */
    @GetMapping("{medicineId}")
    public APIResponse<Medicine> getMedicineById(@NotNull @PathVariable("medicineId") Long medicineId) {
        return APIResponse.<Medicine>builder()
                .data(medicineService.getMedicineById(medicineId))
                .build();
    }

    /**
     * @param medicineIds
     * @return
     */
    @GetMapping("/in/{medicineIds}")
    public APIResponse<List<Medicine>> getMedicineInIds(@NotNull @PathVariable("medicineIds") Set<Long> medicineIds) {
        return APIResponse.<List<Medicine>>builder()
                .data(medicineService.getMedicineInIds(medicineIds))
                .build();
    }

    /**
     * @param medicineCreateRequest
     * @return
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<?> createMedicine(@NotNull @ModelAttribute MedicineCreateRequest medicineCreateRequest, @RequestPart("image_url") MultipartFile imageFile) throws IOException {
        return APIResponse.builder()
                .data(medicineService.createMedicine(medicineCreateRequest, imageFile))
                .build();
    }

    /**
     * @param medicineId
     * @param medicineUpdateRequest
     * @return
     */
    @PutMapping(value = "/{medicineId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<Medicine> updateMedicine(@NotNull @PathVariable("medicineId") Long medicineId, @NotNull @ModelAttribute MedicineUpdateRequest medicineUpdateRequest, @RequestPart(value = "image_url", required = false) MultipartFile imageFile) throws IOException {
        return APIResponse.<Medicine>builder()
                .data(medicineService.updateMedicine(medicineId, medicineUpdateRequest, imageFile))
                .build();
    }

    /**
     * @param medicineId
     * @param medicineUpdateRequest
     * @return
     */
    @DeleteMapping("/{medicineId}")
    public APIResponse<Medicine> deleteMedicine(@NotNull @PathVariable("medicineId") Long medicineId, @RequestBody MedicineUpdateRequest medicineUpdateRequest) {
        medicineService.deleteMedicine(medicineId);
        return APIResponse.<Medicine>builder()
                .message("Delete medicine successful")
                .build();
    }


}
