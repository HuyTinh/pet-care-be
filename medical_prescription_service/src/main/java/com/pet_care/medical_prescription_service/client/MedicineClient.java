package com.pet_care.medical_prescription_service.client;

import com.pet_care.medical_prescription_service.dto.response.APIResponse;
import com.pet_care.medical_prescription_service.model.Medicine;
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
    APIResponse<Medicine> getMedicineById(@PathVariable("medicineId") Long medicineId);

    /**
     * @param medicineIds
     * @return
     */
    @GetMapping("/medicine/in/{medicineIds}")
    APIResponse<List<Medicine>> getMedicineInIds(@PathVariable("medicineIds") Set<Long> medicineIds);
}

