package com.pet_care.employee_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public Mono<Map> uploadImage(Flux<FilePart> filePart) {
        return filePart.publishOn(Schedulers.boundedElastic()).map((file) -> {
            try {
                // Chuyển FilePart thành File
                File tempFile = File.createTempFile(filePart.file(), "");
                FileOutputStream fos = new FileOutputStream(tempFile);
                file.transferTo(tempFile).subscribe();

                return Mono.fromCallable(() -> {
                    // Upload file lên Cloudinary
                    Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());

                    // Xóa file tạm sau khi upload
                    tempFile.delete();

                    return uploadResult;
                });
            } catch (IOException e) {
                return Mono.error(new RuntimeException("Error uploading image", e));
            }
        });
    }
}
