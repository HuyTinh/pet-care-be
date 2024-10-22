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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    @PostMapping
    public APIResponse<Medicine> createMedicine(@NotNull @RequestBody MedicineCreateRequest medicineCreateRequest) {
        return APIResponse.<Medicine>builder()
                .data(medicineService.createMedicine(medicineCreateRequest))
                .build();
    }

    /**
     * @param medicineId
     * @param medicineUpdateRequest
     * @return
     */
    @PutMapping("/{medicineId}")
    public APIResponse<Medicine> updateMedicine(@NotNull @PathVariable("medicineId") Long medicineId, @NotNull @RequestBody MedicineUpdateRequest medicineUpdateRequest) {
        return APIResponse.<Medicine>builder()
                .data(medicineService.updateMedicine(medicineId, medicineUpdateRequest))
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

    /**
     * Search medicines based on criteria and sort results
     *
     * @param searchQuery (Optional) part of medicine name or exact quantity
     * @param manufacturingDate (Optional) filter by manufacturing date
     * @param expiryDate (Optional) filter by expiry date
     * @param status (Optional) filter by medicine status (ACTIVE, INACTIVE, etc.)
     * @param minPrice (Optional) minimum price filter
     * @param maxPrice (Optional) maximum price filter
     * @param sortBy (Optional) field to sort by (name, price, quantity)
     * @param sortOrder (Optional) order to sort (asc, desc)
     * @return APIResponse with list of filtered and sorted medicines
     */
    @GetMapping("/search")
    public APIResponse<List<MedicineResponse>> searchMedicines(
            @RequestParam(value = "searchQuery", required = false) String searchQuery,
            @RequestParam(value = "manufacturingDate", required = false) Date manufacturingDate,
            @RequestParam(value = "expiryDate", required = false) Date expiryDate,
            @RequestParam(value = "status", required = false) MedicineStatus status,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "asc") String sortOrder
    ) {
        return APIResponse.<List<MedicineResponse>>builder()
                .data(medicineService.searchMedicines(manufacturingDate, expiryDate, status, minPrice, maxPrice, searchQuery, sortBy, sortOrder))
                .build();
    }

}
