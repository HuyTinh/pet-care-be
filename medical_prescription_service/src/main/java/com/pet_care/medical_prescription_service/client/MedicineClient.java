package com.pet_care.medical_prescription_service.client;

import com.pet_care.medical_prescription_service.dto.response.APIResponse;
import com.pet_care.medical_prescription_service.dto.response.CalculationUnitResponse;
import com.pet_care.medical_prescription_service.dto.response.MedicineResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;

@Service
@FeignClient(name = "medicineClient", url = "http://localhost:8085/api/v1/medicine-service")
public interface MedicineClient {
    /**
     * @param medicineId
     * @return
     */
    @GetMapping("/medicine/{medicineId}")
    APIResponse<MedicineResponse> getMedicineById(@PathVariable("medicineId") Long medicineId);

    /**
     * @param medicineIds
     * @return
     */
    @GetMapping("/medicine/in/{medicineIds}")
    APIResponse<List<MedicineResponse>> getMedicineInIds(@PathVariable("medicineIds") Set<Long> medicineIds);


    @GetMapping("/calculation-unit/{calculationUnitId}")
    APIResponse<CalculationUnitResponse> getCalculationUnitById(@PathVariable("calculationUnitId") Long calculationUnitId);
}

