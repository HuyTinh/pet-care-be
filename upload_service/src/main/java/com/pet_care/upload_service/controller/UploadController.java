package com.pet_care.upload_service.controller;

// Import necessary classes for Spring Web and reactive programming
import com.pet_care.upload_service.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController // Marks this class as a Spring REST controller to handle HTTP requests
@RequestMapping("api/v1/image") // Sets the base URL for the controller
@RequiredArgsConstructor // Generates a constructor with the required (final) fields
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Sets fields to private and final by default
public class UploadController {

    CloudinaryService cloudinaryService; // Service to handle image uploads to Cloudinary

    /**
     * Handles image uploads to Cloudinary.
     *
     * @param files the image files to be uploaded
     * @return a ResponseEntity containing a Mono of a list of image URLs returned by Cloudinary
     */
    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // Configures the endpoint for handling multipart form-data requests (file uploads)
    public ResponseEntity<Mono<List<String>>> uploadImages(@RequestPart("files") Flux<FilePart> files) {
        // Uploads the files and returns a Mono containing a list of URLs of the uploaded images
        return ResponseEntity.ok(cloudinaryService.uploadImages(files));
    }
}
