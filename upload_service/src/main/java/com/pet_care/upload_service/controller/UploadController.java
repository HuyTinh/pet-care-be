package com.pet_care.upload_service.controller;

import com.pet_care.upload_service.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1/upload")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadController {
    CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<Flux<Object>> uploadImage(@RequestPart("file") Flux<FilePart> fileParts) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cloudinaryService.uploadImage(fileParts));
    }
}