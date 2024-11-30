package com.pet_care.medicine_service.controller;

import com.pet_care.medicine_service.dto.request.MedicineCreateRequest;
import com.pet_care.medicine_service.dto.request.MedicineUpdateQtyRequest;
import com.pet_care.medicine_service.dto.request.MedicineUpdateRequest;
import com.pet_care.medicine_service.dto.response.APIResponse;
import com.pet_care.medicine_service.dto.response.MedicineResponse;
import com.pet_care.medicine_service.dto.response.PageableResponse;
import com.pet_care.medicine_service.enums.MedicineStatus;
import com.pet_care.medicine_service.enums.MedicineTypes;
import com.pet_care.medicine_service.entity.Medicine;
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
     * Retrieves all medicines.
     *
     * @return A response containing the list of all medicines.
     */
    @GetMapping
    public APIResponse<List<MedicineResponse>> getAllMedicine() {
        return APIResponse.<List<MedicineResponse>>builder()
                .data(medicineService.getAllMedicines())
                .build();
    }

    /**
     * Filters medicines based on various parameters.
     *
     * @param pageNumber Page number for pagination.
     * @param pageSize Number of items per page.
     * @param types Type of medicine.
     * @param searchTerm Search term for filtering.
     * @param manufacturingDate Manufacturing date filter.
     * @param expiryDate Expiry date filter.
     * @param status Medicine status filter.
     * @param minPrice Minimum price filter.
     * @param maxPrice Maximum price filter.
     * @param sortBy Sorting field.
     * @param sortOrder Sorting order (asc/desc).
     * @return A paginated response containing filtered medicines.
     */
    @GetMapping("/filter")
    public APIResponse<PageableResponse<MedicineResponse>> filterMedicines(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "MEDICINE") MedicineTypes types,
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
     * Retrieves a medicine by its ID.
     *
     * @param medicineId The ID of the medicine to retrieve.
     * @return A response containing the medicine details.
     */
    @GetMapping("{medicineId}")
    public APIResponse<Medicine> getMedicineById(@PathVariable("medicineId") Long medicineId) {
        return APIResponse.<Medicine>builder()
                .data(medicineService.getMedicineById(medicineId))
                .build();
    }

    /**
     * Retrieves medicines by their IDs.
     *
     * @param medicineIds The IDs of the medicines to retrieve.
     * @return A response containing the list of medicines.
     */
    @GetMapping("/in/{medicineIds}")
    public APIResponse<List<Medicine>> getMedicineInIds(@PathVariable("medicineIds") Set<Long> medicineIds) {
        return APIResponse.<List<Medicine>>builder()
                .data(medicineService.getMedicineInIds(medicineIds))
                .build();
    }

    /**
     * Creates a new medicine.
     *
     * @param medicineCreateRequest The details of the medicine to create.
     * @param imageFile The image of the medicine.
     * @return A response containing the created medicine.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<Medicine> createMedicine(@ModelAttribute MedicineCreateRequest medicineCreateRequest, @RequestPart("image_url") MultipartFile imageFile) throws IOException {
        return APIResponse.<Medicine>builder()
                .data(medicineService.createMedicine(medicineCreateRequest, imageFile))
                .build();
    }

    /**
     * Updates an existing medicine.
     *
     * @param medicineId The ID of the medicine to update.
     * @param medicineUpdateRequest The updated details of the medicine.
     * @param imageFile The updated image of the medicine.
     * @return A response containing the updated medicine.
     */
    @PutMapping(value = "/{medicineId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<Medicine> updateMedicine(@PathVariable("medicineId") Long medicineId, @ModelAttribute MedicineUpdateRequest medicineUpdateRequest, @RequestPart(value = "image_url", required = false) MultipartFile imageFile) throws IOException {
        return APIResponse.<Medicine>builder()
                .data(medicineService.updateMedicine(medicineId, medicineUpdateRequest, imageFile))
                .build();
    }

    /**
     * Deletes a medicine.
     *
     * @param medicineId The ID of the medicine to delete.
     * @param medicineUpdateRequest The request to update medicine.
     * @return A response indicating the result of the delete operation.
     */
    @DeleteMapping("/{medicineId}")
    public APIResponse<Medicine> deleteMedicine(@PathVariable("medicineId") Long medicineId, @RequestBody MedicineUpdateRequest medicineUpdateRequest) {
        medicineService.deleteMedicine(medicineId);
        return APIResponse.<Medicine>builder()
                .message("Delete medicine successful")
                .build();
    }

    /**
     * Updates the quantity of a medicine.
     *
     * @param medicineUpdateQtyRequest The request to update medicine quantity.
     * @return A response indicating the result of the quantity update.
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
