package com.pet_care.medicine_service.service;

import com.pet_care.medicine_service.client.UploadImageClient;
import com.pet_care.medicine_service.dto.request.MedicineCreateRequest;
import com.pet_care.medicine_service.dto.request.MedicineUpdateRequest;
import com.pet_care.medicine_service.dto.response.MedicineResponse;
import com.pet_care.medicine_service.dto.response.PageableResponse;
import com.pet_care.medicine_service.enums.MedicineStatus;
import com.pet_care.medicine_service.enums.MedicineTypes;
import com.pet_care.medicine_service.exception.APIException;
import com.pet_care.medicine_service.exception.ErrorCode;
import com.pet_care.medicine_service.mapper.MedicineMapper;
import com.pet_care.medicine_service.model.CalculationUnit;
import com.pet_care.medicine_service.model.Location;
import com.pet_care.medicine_service.model.Manufacture;
import com.pet_care.medicine_service.model.Medicine;
import com.pet_care.medicine_service.repository.CalculationUnitRepository;
import com.pet_care.medicine_service.repository.LocationRepository;
import com.pet_care.medicine_service.repository.ManufactureRepository;
import com.pet_care.medicine_service.repository.MedicineRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MedicineService {
    MedicineRepository medicineRepository;

    LocationRepository locationRepository;

    CalculationUnitRepository calculationUnitRepository;

    ManufactureRepository manufactureRepository;

    MedicineMapper medicineMapper;

    UploadImageClient uploadImageClient;

    /**
     * @return
     */

    @Transactional(readOnly = true)
    public PageableResponse<MedicineResponse> filterMedicines(int pageNumber, int pageSize, String searchTerm, Date manufacturingDate, Date expiryDate, MedicineTypes types, MedicineStatus status, Double minPrice, Double maxPrice, String sortBy,
                                                              String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Medicine> medicinePage = medicineRepository.findByFilters(
                searchTerm, manufacturingDate, expiryDate, status,types ,minPrice, maxPrice, pageable
        );

        List<MedicineResponse> medicineList = medicinePage.getContent().stream()
                .map(medicineMapper::toDto)
                .toList();

        PageableResponse<MedicineResponse> pageableResponse = PageableResponse.<MedicineResponse>builder()
                .content(medicineList)
                .totalPages(medicinePage.getTotalPages())
                .pageNumber(medicinePage.getNumber())
                .pageSize(medicinePage.getSize())
                .build();

        log.info("Find all medicines with filters");
        return pageableResponse;
    }

    /**
     * @param id
     * @return
     */

    @Transactional(readOnly = true)
    public Medicine getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.MEDICINE_NOT_FOUND));
        log.info("Find medicine by id: {}", medicine.getId());
        return medicine;
    }

    /**
     * @param medicineIds
     * @return
     */

    @Transactional(readOnly = true)
    public List<Medicine> getMedicineInIds(Set<Long> medicineIds) {
        List<Medicine> medicineList = medicineRepository.findAllById(medicineIds);
        log.info("Find medicine by ids: {}", medicineIds);
        return medicineList;
    }

    /**
     * @param medicineCreateRequest
     * @return
     */

    @Transactional
    public Medicine createMedicine(MedicineCreateRequest medicineCreateRequest, MultipartFile imageFile) throws IOException {
        Medicine newMedicine = medicineMapper.toEntity(medicineCreateRequest);
        checkAndUploadImageMedicine(imageFile, newMedicine);

        findAndSetManufactureById(medicineCreateRequest.getManufactureId(), newMedicine);

        findAllAndSetCalculationUnitByIdIn(medicineCreateRequest.getCalculationUnits(), newMedicine);

        findAllAndSetLocationByIdIn(medicineCreateRequest.getLocations(), newMedicine);

        Medicine savedMedicine = medicineRepository.save(newMedicine);

        log.info("Create medicine: {}", savedMedicine);

        return savedMedicine;
    }

    /**
     * @param medicineId
     * @param medicineUpdateRequest
     * @return
     */

    @Transactional
    public Medicine updateMedicine(Long medicineId, MedicineUpdateRequest medicineUpdateRequest, MultipartFile imageFile) throws IOException {
        Medicine existingMedicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new APIException(ErrorCode.MEDICINE_NOT_FOUND));

        // Check if the user uploaded a new image
        checkAndUploadImageMedicine(imageFile, existingMedicine);

        // Update calculation units and locations
        findAllAndSetCalculationUnitByIdIn(medicineUpdateRequest.getCalculationUnits(), existingMedicine);

        findAllAndSetLocationByIdIn(medicineUpdateRequest.getLocations(), existingMedicine);

        findAndSetManufactureById(medicineUpdateRequest.getManufactureId(), existingMedicine);

        // Update the rest of the medicine fields
        medicineMapper.partialUpdate(medicineUpdateRequest, existingMedicine);
        Medicine updatedMedicine = medicineRepository.save(existingMedicine);

        log.info("Update medicine: {}", updatedMedicine);

        return updatedMedicine;
    }

    /**
     * @param medicineId
     */
    @Transactional
    public void deleteMedicine(Long medicineId) {
        medicineRepository.deleteById(medicineId);
        log.info("Delete medicine successful");
    }

    /**
     * @param manufactureId
     * @param medicine
     */
    private void findAndSetManufactureById(Long manufactureId, Medicine medicine) {
        Manufacture manufacture = manufactureRepository
                .findById(manufactureId)
                .orElseThrow(() -> new APIException(ErrorCode.MANUFACTURE_NOT_FOUND));

        medicine.setManufacture(manufacture);
    }

    /**
     * @param locationIds
     * @param medicine
     */
    private void findAllAndSetLocationByIdIn(Set<Long> locationIds, Medicine medicine) {
        Set<Location> locations = new HashSet<>
                (locationRepository.findAllById(locationIds));

        medicine.setLocations(locations);
    }

    /**
     * @param calculationUnitIds
     * @param medicine
     */
    private void findAllAndSetCalculationUnitByIdIn(Set<Long> calculationUnitIds, Medicine medicine) {
        Set<CalculationUnit> calculationUnits = new HashSet<>
                (calculationUnitRepository.findAllById(calculationUnitIds));

        medicine.setCalculationUnits(calculationUnits);

    }

    /**
     * @param imageFile
     * @param medicine
     * @throws IOException
     */
    private void checkAndUploadImageMedicine(MultipartFile imageFile, Medicine medicine) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            // Upload the new image and update the existing medicine's image URL
            String imageUrl = uploadImageClient
                    .uploadImage(List.of(imageFile)).get(0);
            medicine.setImage_url(imageUrl);
        }
    }

    /**
     * @param dateStr
     * @return
     */
    private Date parseDate(String dateStr) {
        try {
            if (dateStr != null) {
                LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
            return null;
        } catch (DateTimeParseException e) {
            log.error("Invalid date format: {}", dateStr);
            return null;
        }
    }

}
