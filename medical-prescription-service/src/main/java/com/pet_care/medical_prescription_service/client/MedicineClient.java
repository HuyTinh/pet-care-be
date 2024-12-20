package com.pet_care.medical_prescription_service.client;

import com.pet_care.medical_prescription_service.dto.request.MedicineUpdateQtyRequest;
import com.pet_care.medical_prescription_service.dto.response.APIResponse;
import com.pet_care.medical_prescription_service.dto.response.CalculationUnitResponse;
import com.pet_care.medical_prescription_service.dto.response.MedicineResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Service
@FeignClient(name = "medicine-service")
public interface MedicineClient {
    /**
     * @param medicineId
     * @return
     */
    @GetMapping("${service.medicine-client.path}/medicine/{medicineId}")
    APIResponse<MedicineResponse> getMedicineById(@PathVariable("medicineId") Long medicineId);

    /**
     * @param medicineIds
     * @return
     */
    @GetMapping("${service.medicine-client.path}/medicine/in/{medicineIds}")
    APIResponse<List<MedicineResponse>> getMedicineInIds(@PathVariable("medicineIds") Set<Long> medicineIds);


    /**
     * @param calculationUnitId
     * @return
     */
    @GetMapping("${service.medicine-client.path}/calculation-unit/{calculationUnitId}")
    APIResponse<CalculationUnitResponse> getCalculationUnitById(@PathVariable("calculationUnitId") Long calculationUnitId);

    /**
     * @param calculationUnitIds
     * @return
     */
    @GetMapping("${service.medicine-client.path}/calculation-unit/in/{calculationUnitIds}")
    APIResponse<List<CalculationUnitResponse>> getCalculationUnitByIds(@PathVariable("calculationUnitIds") Set<Long> calculationUnitIds);

    /**
     * @param medicineUpdateQtyRequest
     * @return
     */
    @PutMapping("${service.medicine-client.path}/medicine/update-qty")
    APIResponse<?> updateQuantity(@RequestBody MedicineUpdateQtyRequest medicineUpdateQtyRequest);
}

