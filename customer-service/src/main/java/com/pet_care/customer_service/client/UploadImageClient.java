package com.pet_care.customer_service.client;

// Import necessary Spring and Feign libraries
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// Mark this interface as a Spring service
@Service
@FeignClient(name = "upload-service")
public interface UploadImageClient {

    /**
     * Uploads multiple image files to the upload service.
     *
     * @param files List of image files to be uploaded
     * @return List of URLs for the uploaded images
     */
    @PostMapping(value = "${service.upload-client.path}/image/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // Define POST request with multipart/form-data content type
    List<String> uploadImage(@RequestPart("files") List<MultipartFile> files); // Map the "files" parameter to a list of files in the request
}
