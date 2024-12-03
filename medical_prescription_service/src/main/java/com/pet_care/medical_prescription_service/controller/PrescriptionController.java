package com.pet_care.medical_prescription_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pet_care.medical_prescription_service.client.UploadImageClient;
import com.pet_care.medical_prescription_service.dto.request.PrescriptionCreateRequest;
import com.pet_care.medical_prescription_service.dto.request.PrescriptionUpdateRequest;
import com.pet_care.medical_prescription_service.dto.response.APIResponse;
import com.pet_care.medical_prescription_service.dto.response.PageableResponse;
import com.pet_care.medical_prescription_service.dto.response.PrescriptionResponse;
import com.pet_care.medical_prescription_service.enums.PrescriptionStatus;
import com.pet_care.medical_prescription_service.repository.PrescriptionRepository;
import com.pet_care.medical_prescription_service.service.PrescriptionService;
import com.pet_care.medical_prescription_service.utils.fileUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.redis.connection.convert.StringToDataTypeConverter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("prescription")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrescriptionController {

    // Service layer responsible for handling prescription-related business logic
    PrescriptionService prescriptionService;

    // Repository layer responsible for interacting with the database for prescriptions
    PrescriptionRepository prescriptionRepository;

    UploadImageClient uploadImageClient;

    /**
     * Endpoint to retrieve all prescriptions.
     *
     * @return APIResponse containing a list of all prescriptions.
     */
    @GetMapping
    public APIResponse<List<PrescriptionResponse>> getAllPrescription() {
        return APIResponse.<List<PrescriptionResponse>>builder()
                .data(prescriptionService.getAllPrescriptions())
                .build();
    }

    /**
     * Endpoint to retrieve filtered prescriptions based on pagination and optional filter criteria.
     *
     * @param page               Page number for pagination (default is 0).
     * @param size               Number of items per page (default is 50).
     * @param startDate          Optional filter for prescriptions starting from this date.
     * @param endDate            Optional filter for prescriptions ending at this date.
     * @param prescriptionStatus Status of prescriptions to filter (default is "APPROVED").
     * @return APIResponse containing filtered prescriptions in a paginated response.
     * @throws JsonProcessingException if there is an issue processing the filter criteria.
     */
    @GetMapping("/filter")
    public APIResponse<PageableResponse<PrescriptionResponse>> getFilteredPrescription(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "50") int size,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "statues", required = false) Set<String> prescriptionStatus,
            @RequestParam(value = "accountId", required = false) Long accountId
    ) throws JsonProcessingException {
        return APIResponse.<PageableResponse<PrescriptionResponse>>builder()
                .data(prescriptionService
                        .filteredPrescription(
                                page,
                                size,
                                Objects.requireNonNullElse(startDate, LocalDate.now()),
                                Objects.requireNonNullElse(endDate, LocalDate.now()),
                                prescriptionStatus,
                                accountId
                        )
                )
                .build();
    }

    /**
     * Endpoint to retrieve a prescription by its unique ID.
     *
     * @param prescriptionId ID of the prescription to retrieve.
     * @return APIResponse containing the details of the specified prescription.
     * @throws JsonProcessingException if there is an issue processing the prescription data.
     */
    @GetMapping("/{prescriptionId}")
    public APIResponse<PrescriptionResponse> getPrescriptionById(@PathVariable("prescriptionId") Long prescriptionId) throws JsonProcessingException {
        return APIResponse.<PrescriptionResponse>builder()
                .data(prescriptionService.getPrescriptionById(prescriptionId))
                .build();
    }

    /**
     * Endpoint to retrieve a prescription by its associated appointment ID.
     *
     * @param appointmentId ID of the appointment to retrieve the prescription for.
     * @return APIResponse containing the prescription associated with the appointment.
     */
    @GetMapping("/{appointmentId}/appointment")
    public APIResponse<PrescriptionResponse> getPrescriptionByAppointmentId(@PathVariable("appointmentId") Long appointmentId) {
        return APIResponse.<PrescriptionResponse>builder()
                .data(prescriptionService.getPrescriptionByAppointmentId(appointmentId))
                .build();
    }

    /**
     * Endpoint to create a new prescription.
     *
     * @param prescriptionCreateRequest Request object containing the details of the prescription to be created.
     * @return APIResponse containing the created prescription.
     */
    @PostMapping
    public APIResponse<PrescriptionResponse> createPrescription(@RequestBody PrescriptionCreateRequest prescriptionCreateRequest) {
        return APIResponse.<PrescriptionResponse>builder()
                .data(prescriptionService.createPrescription(prescriptionCreateRequest))
                .build();
    }

    /**
     * Endpoint to update an existing prescription.
     *
     * @param prescriptionUpdateRequest Request object containing updated prescription details.
     * @return APIResponse containing the updated prescription.
     */
    @PutMapping("{prescriptionId}")
    public APIResponse<PrescriptionResponse> updatePrescription(@PathVariable("prescriptionId") Long prescriptionId,@RequestBody PrescriptionUpdateRequest prescriptionUpdateRequest) {
        return APIResponse.<PrescriptionResponse>builder()
                .data(prescriptionService.updatePrescription(prescriptionId, prescriptionUpdateRequest))
                .build();
    }

    /**
     * Endpoint to delete a prescription by its unique ID.
     *
     * @param prescriptionId ID of the prescription to delete.
     * @return APIResponse with a success message after deletion.
     */
    @DeleteMapping("/{prescriptionId}")
    public APIResponse<String> deletePrescription(@PathVariable("prescriptionId") Long prescriptionId) {
        prescriptionRepository.deleteById(prescriptionId);
        return APIResponse.<String>builder()
                .message("Delete prescription successfully")
                .build();
    }

    @PostMapping("/tinyMCE")
    public ResponseEntity<String> testingTinyMCE(@RequestBody String result) {
        Document doc = Jsoup.parse(result);
        String base64 = doc.select("img").attr("src").split(",")[1];
        // Convert Base64 string to byte array
//        byte[] data = Base64.getDecoder().decode(base64);

////        System.out.println(base64);
//        try {
//            uploadImageClient.uploadImage(List.of(Objects.requireNonNull(fileUtil.convertBase64ToMultipartFile(data))));
//        } catch (Exception e){
//            e.printStackTrace();
//        }
////        System.out.println();

        return ResponseEntity.ok("Done");
    }

    public static String UPLOAD_DIRECTORY = fileUtil.class.getProtectionDomain()
            .getCodeSource().getLocation().getPath() + "uploads";


    @GetMapping("/image/{filename}")
    public ResponseEntity<Resource> getPicture(@PathVariable String filename) {
        try {
            // Resolve the path to the file
            File file = new File(UPLOAD_DIRECTORY + File.separator + filename);

            // Create a Resource object for the file
            Resource resource = new UrlResource(file.toURI());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // Return the image as a ResponseEntity
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Set the appropriate image type (JPEG in this case)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
