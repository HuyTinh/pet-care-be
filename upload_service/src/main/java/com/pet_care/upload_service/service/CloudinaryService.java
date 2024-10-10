package com.pet_care.upload_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService {

    Cloudinary cloudinary;

    Mono<String> uploadImage(MultipartFile file) {
        return Mono.fromCallable(() -> {
            // Tải file lên Cloudinary thông qua API upload
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("url");
        }).subscribeOn(Schedulers.boundedElastic());
    }
//    public Mono<Object> uploadFile(FilePart file) {
//        return file.content()  // Get the file content as a Flux of DataBuffer
//                .flatMap(dataBuffer -> {
//                    // Transfer DataBuffer into byte array
//                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                    try {
//                        outputStream.write(dataBuffer.asByteBuffer().array());
//                    } catch (IOException e) {
//                        return Mono.error(new RuntimeException("Failed to read file", e));
//                    }
//                    return Mono.just(outputStream.toByteArray());
//                })
//                .reduce((bytes1, bytes2) -> {  // Combine all DataBuffer chunks into one byte array
//                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                    try {
//                        outputStream.write(bytes1);
//                        outputStream.write(bytes2);
//                    } catch (IOException e) {
//                        throw new RuntimeException("Failed to combine file chunks", e);
//                    }
//                    return outputStream.toByteArray();
//                })
//                .flatMap(bytes -> {
//                    // Upload to Cloudinary
//                    try {
//                        return Mono.just(cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap()).get("url"));
//                    } catch (IOException e) {
//                        return Mono.error(new RuntimeException("Failed to upload to Cloudinary", e));
//                    }
//                });
//    }

//    public Flux<String> uploadFiles(Flux<FilePart> files) {
//        return files.flatMap(this::uploadFiles);
//    }

    public Mono<List<String>> uploadImages(Flux<FilePart> fileParts) {
        return fileParts
                .flatMap(filePart -> filePart.content()
                        .map(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            DataBufferUtils.release(dataBuffer);  // Giải phóng tài nguyên
                            return bytes;
                        })
                        .next()  // Lấy buffer đầu tiên của FilePart
                        .flatMap(bytes -> Mono.fromCallable(() -> {
                            // Upload ảnh lên Cloudinary
                            Map uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap());
                            return (String) uploadResult.get("url");
                        }))
                )
                .collectList();  // Thu thập các URL thành List
    }
}
