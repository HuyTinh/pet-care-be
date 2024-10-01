package com.pet_care.employee_service.controller;

import com.pet_care.employee_service.service.CloudinaryService;
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
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("api/v1/upload")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageUploadController {
    CloudinaryService cloudinaryService;

    @PostMapping
    public Mono<ResponseEntity<Map>> uploadImage(@RequestPart("file") Flux<FilePart> filePart) {
        System.out.println(12);
        return cloudinaryService.uploadImage(filePart)
                .map(uploadResult -> ResponseEntity.status(HttpStatus.CREATED).body(uploadResult))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", e.getMessage()))));
    }
}
