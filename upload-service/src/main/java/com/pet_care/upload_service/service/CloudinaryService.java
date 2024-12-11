package com.pet_care.upload_service.service;

// Import necessary classes for Cloudinary interaction, reactive programming, and file handling
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service // Marks this class as a Spring service component
@RequiredArgsConstructor // Generates a constructor for the final fields
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Sets fields to private and final by default
public class CloudinaryService {

    Cloudinary cloudinary; // Cloudinary instance for uploading files to Cloudinary

    /**
     * Uploads multiple images to Cloudinary.
     *
     * @param file a Flux stream of FilePart objects representing the files to be uploaded
     * @return a Mono containing a List of URLs of the uploaded images
     */
    public List<String> uploadImages(List<MultipartFile> file) {
        try {
            // Get the byte array of the file
            byte[] bytes = file.get(0).getBytes();

            // Upload the file to Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap());

            // Return the URL of the uploaded image
            return List.of((String) uploadResult.get("url"));
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.singletonList("Error uploading the image: " + e.getMessage());
        }
    }


    public String uploadImageFromBase64(String base64) {
        try {
            // Get the byte array of the file
            byte[] bytes = Base64.getDecoder().decode(base64);

            return storeImageLocal(bytes);
//            // Upload the file to Cloudinary
//            Map<String, Object> uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap());
//
//            // Return the URL of the uploaded image
//            return (String) uploadResult.get("url");
        } catch (IOException e) {
            e.printStackTrace();
            return ("Error uploading the image: " + e.getMessage());
        }
    }


    private String UPLOAD_DIRECTORY = this.getClass().getProtectionDomain()
            .getCodeSource().getLocation().getPath() + "uploads";

    private String storeImageLocal(byte[] data) throws IOException {
        File dir = new File(UPLOAD_DIRECTORY);
        if(!dir.exists()) {
            dir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        String fileDir = UPLOAD_DIRECTORY+"/" + fileName + ".webp";

        try (FileOutputStream fileOutputStream = new FileOutputStream(fileDir)) {
            fileOutputStream.write(data);
        }

        return "http://localhost:8089/api/v1/image/" + fileName;
    }
}
