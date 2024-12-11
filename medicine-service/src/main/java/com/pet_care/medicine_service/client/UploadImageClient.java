package com.pet_care.medicine_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@FeignClient(name = "upload-service")
@RequestMapping("/api/v1/upload-service")
public interface UploadImageClient {

    /**
     * Uploads a list of image files to the image upload service.
     *
     * @param files The list of image files to upload.
     * @return A list of URLs of the uploaded images.
     */
    @PostMapping(value = "/image/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    List<String> uploadImage(@RequestPart("files") List<MultipartFile> files);
}
