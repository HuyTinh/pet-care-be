package com.pet_care.medicine_service.controller;

import com.pet_care.medicine_service.dto.response.APIResponse;
import com.pet_care.medicine_service.dto.response.CalculationUnitResponse;
import com.pet_care.medicine_service.service.CalculationUnitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("calculation-unit")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CalculationUnitController {

    CalculationUnitService calculationUnitService;

    /**
     * @return
     */
    @GetMapping
    public APIResponse<List<CalculationUnitResponse>> getAllCalculationUnits() {
        return APIResponse.<List<CalculationUnitResponse>>builder()
                .data(calculationUnitService.getAllCalculationUnit())
                .build();
    }

    /**
     * @param calculationUnitId
     * @return
     */
    @GetMapping("/{calculationUnitId}")
    public APIResponse<CalculationUnitResponse> getCalculationUnitById(@PathVariable("calculationUnitId") Long calculationUnitId) {
        return APIResponse.<CalculationUnitResponse>builder()
                .data(calculationUnitService.getCalculationUnitById(calculationUnitId))
                .build();
    }


    @GetMapping("/in/{calculationUnitIds}")
    public APIResponse<List<CalculationUnitResponse>> getCalculationUnitByIds(@PathVariable("calculationUnitIds") Set<Long> calculationUnitIds) {
        return APIResponse.<List<CalculationUnitResponse>>builder()
                .data(calculationUnitService.getCalculationUnitsInIds(calculationUnitIds))
                .build();
    }
}
