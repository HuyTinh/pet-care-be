package com.pet_care.upload_service.service;

// Import necessary classes for Cloudinary interaction, reactive programming, and file handling
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;

@Service // Marks this class as a Spring service component
@RequiredArgsConstructor // Generates a constructor for the final fields
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Sets fields to private and final by default
public class CloudinaryService {

    Cloudinary cloudinary; // Cloudinary instance for uploading files to Cloudinary

    /**
     * Uploads a single image to Cloudinary.
     *
     * @param file the image file to upload
     * @return a Mono containing the URL of the uploaded image
     */
    Mono<String> uploadImage(MultipartFile file) {
        return Mono.fromCallable(() -> {
            // Uploads the file to Cloudinary via the API
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            return (String) uploadResult.get("url"); // Returns the URL of the uploaded image
        }).subscribeOn(Schedulers.boundedElastic()); // Executes the upload on a bounded elastic thread pool
    }

    /**
     * Uploads multiple images to Cloudinary.
     *
     * @param fileParts a Flux stream of FilePart objects representing the files to be uploaded
     * @return a Mono containing a List of URLs of the uploaded images
     */
    public Mono<List<String>> uploadImages(Flux<FilePart> fileParts) {
        return fileParts
                .flatMap(filePart -> filePart.content()
                        .map(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()]; // Converts the data buffer into a byte array
                            dataBuffer.read(bytes); // Reads the data buffer content
                            DataBufferUtils.release(dataBuffer); // Releases the resources of the data buffer
                            return bytes; // Returns the byte array representing the file content
                        })
                        .next() // Takes the first content buffer from the FilePart
                        .flatMap(bytes -> Mono.fromCallable(() -> {
                            // Uploads the byte data to Cloudinary
                            Map<?, ?> uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap());
                            return (String) uploadResult.get("url"); // Returns the URL of the uploaded image
                        }))
                )
                .collectList(); // Collects all the uploaded image URLs into a List
    }
}
