package com.pet_care.medicine_service.service;

import com.pet_care.medicine_service.client.UploadImageClient;
import com.pet_care.medicine_service.dto.request.MedicineCreateRequest;
import com.pet_care.medicine_service.dto.request.MedicineUpdateQtyRequest;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MedicineService {
    // Repositories for accessing data related to medicines, locations, calculation units, and manufacturers
    MedicineRepository medicineRepository;
    LocationRepository locationRepository;
    CalculationUnitRepository calculationUnitRepository;
    ManufactureRepository manufactureRepository;

    // Mapper to convert between Medicine entity and MedicineResponse DTO
    MedicineMapper medicineMapper;

    // Client for handling image uploads related to medicine
    UploadImageClient uploadImageClient;

    /**
     * Retrieves all medicines from the repository and maps them to response DTOs.
     *
     * @return A list of MedicineResponse DTOs representing all medicines
     */
    public List<MedicineResponse> getAllMedicines() {
        // Retrieves all medicines, converts them to DTOs and stores in medicineResponses
        List<MedicineResponse> medicineResponses = medicineRepository.getAllMedicines()
                .stream().map(medicineMapper::toDto).toList();

        // Logs the successful retrieval of all medicines
        log.info("Get all medicines successful");

        // Returns the list of medicine responses
        return medicineResponses;
    }

    /**
     * Filters medicines based on various parameters such as page number, search term, dates, types, status, and price range.
     *
     * @param pageNumber The page number for pagination
     * @param pageSize The number of items per page
     * @param searchTerm A search term for filtering by medicine name or other fields
     * @param manufacturingDate The manufacturing date filter
     * @param expiryDate The expiry date filter
     * @param types The types of medicine (e.g., tablet, syrup)
     * @param status The status of the medicine (e.g., available, out of stock)
     * @param minPrice The minimum price filter
     * @param maxPrice The maximum price filter
     * @param sortBy The field by which to sort the results
     * @param sortOrder The sort direction ("asc" or "desc")
     * @return A PageableResponse containing the filtered list of medicines and pagination details
     */
    @Transactional(readOnly = true)  // This method is marked read-only for performance optimization
    public PageableResponse<MedicineResponse> filterMedicines(int pageNumber, int pageSize, String searchTerm, Date manufacturingDate, Date expiryDate, MedicineTypes types, MedicineStatus status, Double minPrice, Double maxPrice, String sortBy,
                                                              String sortOrder) {
        // Creates a Sort object based on the provided sort field and order
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);

        // Creates a Pageable object with the specified page number, size, and sort
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Retrieves a Page of medicines that match the given filters
        Page<Medicine> medicinePage = medicineRepository.findByFilters(
                searchTerm, manufacturingDate, expiryDate, status, types, minPrice, maxPrice, pageable
        );

        // Converts the page content (list of medicines) to a list of MedicineResponse DTOs
        List<MedicineResponse> medicineList = medicinePage.getContent().stream()
                .map(medicineMapper::toDto)
                .toList();

        // Builds a PageableResponse object containing the filtered medicines and pagination details
        PageableResponse<MedicineResponse> pageableResponse = PageableResponse.<MedicineResponse>builder()
                .content(medicineList)
                .totalPages(medicinePage.getTotalPages())
                .pageNumber(medicinePage.getNumber())
                .pageSize(medicinePage.getSize())
                .build();

        // Logs the successful filtering of medicines
        log.info("Find all medicines with filters");

        // Returns the pageable response containing the filtered list and pagination information
        return pageableResponse;
    }

    /**
     * Retrieves a medicine record by its ID.
     *
     * @param id The ID of the medicine to be retrieved
     * @return The medicine record
     * @throws APIException If the medicine with the given ID is not found
     */
    @Transactional(readOnly = true)  // Marks the method as read-only for better performance in read operations
    public Medicine getMedicineById(Long id) {
        // Retrieves the medicine by ID, throws an exception if not found
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.MEDICINE_NOT_FOUND));

        // Logs the retrieval of the medicine by ID
        log.info("Find medicine by id: {}", medicine.getId());

        // Returns the found medicine record
        return medicine;
    }

    /**
     * Retrieves a list of medicines by their IDs.
     *
     * @param medicineIds A set of medicine IDs to retrieve
     * @return A list of medicines matching the provided IDs
     */
    @Transactional(readOnly = true)  // Marks the method as read-only for better performance in read operations
    public List<Medicine> getMedicineInIds(Set<Long> medicineIds) {
        // Retrieves all medicines by the provided set of IDs
        List<Medicine> medicineList = medicineRepository.findAllById(medicineIds);

        // Logs the retrieval of medicines by the provided IDs
        log.info("Find medicine by ids: {}", medicineIds);

        // Returns the list of medicines
        return medicineList;
    }

    /**
     * Creates a new medicine record based on the provided request data and image file.
     *
     * @param medicineCreateRequest The request containing the details for the new medicine
     * @param imageFile The image file for the medicine (if provided)
     * @return The created medicine record
     * @throws IOException If an error occurs during the image upload process
     */
    @Transactional  // Marks the method as transactional to ensure the creation process is atomic
    public Medicine createMedicine(MedicineCreateRequest medicineCreateRequest, MultipartFile imageFile) throws IOException {
        // Converts the request data to a new Medicine entity
        Medicine newMedicine = medicineMapper.toEntity(medicineCreateRequest);

        // Checks if a new image is uploaded and updates the medicine's image URL
        checkAndUploadImageMedicine(imageFile, newMedicine);

        // Sets the manufacture, calculation units, and locations for the new medicine
        findAndSetManufactureById(medicineCreateRequest.getManufactureId(), newMedicine);
        findAllAndSetCalculationUnitByIdIn(medicineCreateRequest.getCalculationUnits(), newMedicine);
        findAllAndSetLocationByIdIn(medicineCreateRequest.getLocations(), newMedicine);

        // Saves the new medicine record to the repository
        Medicine savedMedicine = medicineRepository.save(newMedicine);

        // Logs the creation of the new medicine record
        log.info("Create medicine: {}", savedMedicine);

        // Returns the created medicine
        return savedMedicine;
    }

    /**
     * Updates the details of an existing medicine, including its image, calculation units, locations, and other fields.
     *
     * @param medicineId The ID of the medicine to be updated
     * @param medicineUpdateRequest The request containing updated information for the medicine
     * @param imageFile The new image file to upload (if provided)
     * @return The updated Medicine object after the changes have been applied
     * @throws IOException If an error occurs during the image upload process
     */
    @Transactional  // Ensures that all database operations are performed within a transaction
    public Medicine updateMedicine(Long medicineId, MedicineUpdateRequest medicineUpdateRequest, MultipartFile imageFile) throws IOException {
        // Retrieves the existing medicine by ID, throws an exception if not found
        Medicine existingMedicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new APIException(ErrorCode.MEDICINE_NOT_FOUND));

        // Checks if a new image is uploaded and updates the medicine's image if so
        checkAndUploadImageMedicine(imageFile, existingMedicine);

        // Updates the calculation units for the medicine based on the provided IDs
        findAllAndSetCalculationUnitByIdIn(medicineUpdateRequest.getCalculationUnits(), existingMedicine);

        // Updates the locations for the medicine based on the provided IDs
        findAllAndSetLocationByIdIn(medicineUpdateRequest.getLocations(), existingMedicine);

        // Sets the manufacture information for the medicine based on the provided ID
        findAndSetManufactureById(medicineUpdateRequest.getManufactureId(), existingMedicine);

        // Partially updates the other fields of the existing medicine with the provided request data
        medicineMapper.partialUpdate(medicineUpdateRequest, existingMedicine);

        // Saves and returns the updated medicine record
        Medicine updatedMedicine = medicineRepository.save(existingMedicine);

        // Logs the successful update of the medicine
        log.info("Update medicine: {}", updatedMedicine);

        // Returns the updated medicine object
        return updatedMedicine;
    }

    /**
     * Deletes a medicine record by its ID from the repository.
     *
     * @param medicineId The ID of the medicine to be deleted
     */
    @Transactional  // Ensures the operation is performed within a transaction
    public void deleteMedicine(Long medicineId) {
        // Deletes the medicine record by its ID from the repository
        medicineRepository.deleteById(medicineId);

        // Logs the successful deletion of the medicine
        log.info("Delete medicine successful");
    }

    /**
     * Retrieves a Manufacture entity by its ID and sets it to the given medicine object.
     *
     * @param manufactureId The ID of the Manufacture entity to be retrieved
     * @param medicine The medicine object where the retrieved Manufacture entity will be set
     */
    private void findAndSetManufactureById(Long manufactureId, Medicine medicine) {
        // Retrieves the Manufacture entity by ID, throws an exception if not found
        Manufacture manufacture = manufactureRepository
                .findById(manufactureId)
                .orElseThrow(() -> new APIException(ErrorCode.MANUFACTURE_NOT_FOUND));

        // Sets the retrieved Manufacture entity to the medicine object
        medicine.setManufacture(manufacture);
    }

    /**
     * Retrieves all Location entities by their IDs and sets them to the given medicine object.
     *
     * @param locationIds A set of IDs for the Location entities to be retrieved
     * @param medicine The medicine object where the retrieved Location entities will be set
     */
    private void findAllAndSetLocationByIdIn(Set<Long> locationIds, Medicine medicine) {
        // Retrieves all Location entities by the provided set of IDs and stores them in a HashSet
        Set<Location> locations = new HashSet<>
                (locationRepository.findAllById(locationIds));

        // Sets the retrieved Location entities to the medicine object
        medicine.setLocations(locations);
    }

    /**
     * Retrieves all CalculationUnit entities by their IDs and sets them to the given medicine object.
     *
     * @param calculationUnitIds A set of IDs for the CalculationUnit entities to be retrieved
     * @param medicine The medicine object where the retrieved CalculationUnit entities will be set
     */
    private void findAllAndSetCalculationUnitByIdIn(Set<Long> calculationUnitIds, Medicine medicine) {
        // Retrieves all CalculationUnit entities by the provided set of IDs and stores them in a HashSet
        Set<CalculationUnit> calculationUnits = new HashSet<>
                (calculationUnitRepository.findAllById(calculationUnitIds));

        // Sets the retrieved CalculationUnit entities to the medicine object
        medicine.setCalculationUnits(calculationUnits);
    }

    /**
     * Checks if an image file is provided, uploads the image, and updates the medicine's image URL.
     *
     * @param imageFile The image file to be uploaded
     * @param medicine The medicine object whose image URL will be updated
     * @throws IOException If an I/O error occurs during the image upload process
     */
    private void checkAndUploadImageMedicine(MultipartFile imageFile, Medicine medicine) throws IOException {
        // Checks if the image file is not null and is not empty
        if (imageFile != null && !imageFile.isEmpty()) {
            // Uploads the image file and retrieves the image URL
            String imageUrl = uploadImageClient
                    .uploadImage(List.of(imageFile)).get(0);
            // Sets the uploaded image URL to the medicine object
            medicine.setImageUrl(imageUrl);
        }
    }

    /**
     * Parses a date string in the format "yyyy/MM/dd" and converts it to a Date object.
     *
     * @param dateStr The date string to be parsed
     * @return The corresponding Date object, or null if the string is invalid or null
     */
    private Date parseDate(String dateStr) {
        try {
            // Checks if the date string is not null
            if (dateStr != null) {
                // Parses the date string into a LocalDate using the specified format
                LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                // Converts the LocalDate to a Date object by applying the system's default time zone
                return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
            // Returns null if the date string is null
            return null;
        } catch (DateTimeParseException e) {
            // Logs an error if the date format is invalid
            log.error("Invalid date format: {}", dateStr);
            // Returns null if parsing fails
            return null;
        }
    }

    /**
     * Updates the quantity of a specific medicine in the repository.
     *
     * @param medicineUpdateQtyRequest The request containing the medicine ID and quantity to update
     * @return The number of rows affected (indicating if the update was successful)
     */
    public Integer updateQuantity(MedicineUpdateQtyRequest medicineUpdateQtyRequest) {
        // Retrieves the existing medicine record by ID, throws an exception if not found
        Medicine existingMedicine = medicineRepository.findById(medicineUpdateQtyRequest.getMedicineId())
                .orElseThrow(() -> new APIException(ErrorCode.MEDICINE_NOT_FOUND));

        // Updates the quantity of the medicine and returns the number of rows affected
        Integer isUpdateQty = medicineRepository.updateQuantity(medicineUpdateQtyRequest.getMedicineId(),
                existingMedicine.getQuantity() - medicineUpdateQtyRequest.getQty());

        // Logs the successful update of the medicine quantity
        log.info("Update medicine {} successful", medicineUpdateQtyRequest.getMedicineId());

        // Returns the number of rows affected by the update
        return isUpdateQty;
    }
}
