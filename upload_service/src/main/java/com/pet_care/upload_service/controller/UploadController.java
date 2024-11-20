package com.pet_care.upload_service.controller;

// Import necessary classes for Spring Web and reactive programming
import com.pet_care.upload_service.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

@RestController // Marks this class as a Spring REST controller to handle HTTP requests
@RequestMapping("api/v1/image") // Sets the base URL for the controller
@RequiredArgsConstructor // Generates a constructor with the required (final) fields
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Sets fields to private and final by default
public class UploadController {

    CloudinaryService cloudinaryService; // Service to handle image uploads to Cloudinary

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> uploadImage(@RequestPart("files") List<MultipartFile> files) {
        return ResponseEntity.ok(cloudinaryService.uploadImages(files));
    }

    @PostMapping(value = "upload/base64")
    public ResponseEntity<String> uploadImageFromBase64(@RequestBody String base64) {
        return ResponseEntity.ok(cloudinaryService.uploadImageFromBase64(base64));
    }
    private String UPLOAD_DIRECTORY = this.getClass().getProtectionDomain()
            .getCodeSource().getLocation().getPath() + "uploads";

    @GetMapping("/{filename}")
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
