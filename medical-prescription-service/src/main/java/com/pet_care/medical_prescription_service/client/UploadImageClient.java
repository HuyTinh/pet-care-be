package com.pet_care.medical_prescription_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@FeignClient(name = "uploadClient", url = "http://localhost:8089/api/v1/image/upload")
public interface UploadImageClient {
    /**
     * @param files
     * @return
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    List<String> uploadImage(@RequestPart("files") List<MultipartFile> files);

    @PostMapping(value = "/base64")
    String uploadImageFromBase64(@RequestBody String base64);
}
