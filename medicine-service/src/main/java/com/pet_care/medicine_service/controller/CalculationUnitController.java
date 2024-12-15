package com.pet_care.medicine_service.controller;

import com.pet_care.medicine_service.dto.response.APIResponse;
import com.pet_care.medicine_service.dto.response.CalculationUnitResponse;
import com.pet_care.medicine_service.service.CalculationUnitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
@CrossOrigin("*")
@RestController
@RequestMapping("calculation-unit")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CalculationUnitController {

    CalculationUnitService calculationUnitService;

    /**
     * Retrieves all calculation units.
     *
     * @return A response containing the list of all calculation units.
     */
    @GetMapping
    public APIResponse<List<CalculationUnitResponse>> getAllCalculationUnits() {
        return APIResponse.<List<CalculationUnitResponse>>builder()
                .data(calculationUnitService.getAllCalculationUnit())
                .build();
    }

    /**
     * Retrieves a calculation unit by its ID.
     *
     * @param calculationUnitId The ID of the calculation unit to retrieve.
     * @return A response containing the calculation unit details.
     */
    @GetMapping("/{calculationUnitId}")
    public APIResponse<CalculationUnitResponse> getCalculationUnitById(@PathVariable("calculationUnitId") Long calculationUnitId) {
        return APIResponse.<CalculationUnitResponse>builder()
                .data(calculationUnitService.getCalculationUnitById(calculationUnitId))
                .build();
    }

    /**
     * Retrieves calculation units by their IDs.
     *
     * @param calculationUnitIds A set of calculation unit IDs to retrieve.
     * @return A response containing the list of calculation units matching the provided IDs.
     */
    @GetMapping("/in/{calculationUnitIds}")
    public APIResponse<List<CalculationUnitResponse>> getCalculationUnitByIds(@PathVariable("calculationUnitIds") Set<Long> calculationUnitIds) {
        return APIResponse.<List<CalculationUnitResponse>>builder()
                .data(calculationUnitService.getCalculationUnitsInIds(calculationUnitIds))
                .build();
    }
}
