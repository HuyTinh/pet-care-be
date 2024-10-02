package com.pet_care.upload_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public Flux<Object> uploadImage(Flux<FilePart> fileParts) {
        return fileParts
                .publishOn(Schedulers.boundedElastic())
                .flatMap(filePart -> {
                    // Lưu file tạm thời
                    try {
                        File tempFile = File.createTempFile(filePart.filename(), ".tmp");
                        return filePart.transferTo(tempFile)
                                .then(Mono.fromCallable(() -> {
                                    // Tải lên Cloudinary
                                    Object uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap()).get("url");
                                    // Xóa file tạm sau khi tải lên
                                    tempFile.delete();

                                    return uploadResult;
                                }));
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException("Failed to create temp file", e));
                    }
                });
    }
}
