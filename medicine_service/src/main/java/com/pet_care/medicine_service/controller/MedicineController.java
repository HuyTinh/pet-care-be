package com.pet_care.medicine_service.controller;

import com.pet_care.medicine_service.dto.request.MedicineCreateRequest;
import com.pet_care.medicine_service.dto.request.MedicineUpdateQtyRequest;
import com.pet_care.medicine_service.dto.request.MedicineUpdateRequest;
import com.pet_care.medicine_service.dto.response.APIResponse;
import com.pet_care.medicine_service.dto.response.MedicineResponse;
import com.pet_care.medicine_service.dto.response.PageableResponse;
import com.pet_care.medicine_service.enums.MedicineStatus;
import com.pet_care.medicine_service.enums.MedicineTypes;
import com.pet_care.medicine_service.model.Medicine;
import com.pet_care.medicine_service.service.MedicineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("medicine")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MedicineController {

    MedicineService medicineService;

    /**
     * @return
     */
    @GetMapping
    public APIResponse<List<MedicineResponse>> getAllMedicine() {
        return APIResponse.<List<MedicineResponse>>builder()
                .data(medicineService.getAllMedicines())
                .build();
    }


    /**
     * @param pageNumber
     * @param pageSize
     * @param types
     * @param searchTerm
     * @param manufacturingDate
     * @param expiryDate
     * @param status
     * @param minPrice
     * @param maxPrice
     * @param sortBy
     * @param sortOrder
     * @return
     */
    @GetMapping("/filter")
    public APIResponse<PageableResponse<MedicineResponse>> filterMedicines(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "MEDICINE") MedicineTypes types ,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date manufacturingDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date expiryDate,
            @RequestParam(required = false) MedicineStatus status,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {

        return APIResponse.<PageableResponse<MedicineResponse>>builder()
                .data(medicineService.filterMedicines(
                        pageNumber,
                        pageSize,
                        searchTerm,
                        manufacturingDate,
                        expiryDate,
                        types,
                        status,
                        minPrice,
                        maxPrice,
                        sortBy,
                        sortOrder))
                .build();
    }

    /**
     * @param medicineId
     * @return
     */
    @GetMapping("{medicineId}")
    public APIResponse<Medicine> getMedicineById(@PathVariable("medicineId") Long medicineId) {
        return APIResponse.<Medicine>builder()
                .data(medicineService.getMedicineById(medicineId))
                .build();
    }

    /**
     * @param medicineIds
     * @return
     */
    @GetMapping("/in/{medicineIds}")
    public APIResponse<List<Medicine>> getMedicineInIds(@PathVariable("medicineIds") Set<Long> medicineIds) {
        return APIResponse.<List<Medicine>>builder()
                .data(medicineService.getMedicineInIds(medicineIds))
                .build();
    }

    /**
     * @param medicineCreateRequest
     * @return
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<?> createMedicine(@ModelAttribute MedicineCreateRequest medicineCreateRequest, @RequestPart("image_url") MultipartFile imageFile) throws IOException {
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
    public APIResponse<Medicine> updateMedicine(@PathVariable("medicineId") Long medicineId, @ModelAttribute MedicineUpdateRequest medicineUpdateRequest, @RequestPart(value = "image_url", required = false) MultipartFile imageFile) throws IOException {
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
    public APIResponse<Medicine> deleteMedicine(@PathVariable("medicineId") Long medicineId, @RequestBody MedicineUpdateRequest medicineUpdateRequest) {
        medicineService.deleteMedicine(medicineId);
        return APIResponse.<Medicine>builder()
                .message("Delete medicine successful")
                .build();
    }

    /**
     * @param medicineUpdateQtyRequest
     * @return
     */
    @PutMapping("update-qty")
    public APIResponse<?> updateQuantity(@RequestBody MedicineUpdateQtyRequest medicineUpdateQtyRequest) {

        Integer isUpdateQty = medicineService.updateQuantity(medicineUpdateQtyRequest);

        String message = "Update medicine " + medicineUpdateQtyRequest.getMedicineId() + " successful";

        if(isUpdateQty == 0) {
            message = "Update medicine failed";
        }

        return APIResponse.builder()
                .message(message)
                .build();
    }
}
