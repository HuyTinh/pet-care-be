package com.pet_care.medical_prescription_service.utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class fileUtil {

    public static String UPLOAD_DIRECTORY = fileUtil.class.getProtectionDomain()
            .getCodeSource().getLocation().getPath() + "uploads";

    public String storeImageLocal(byte[] data) {
        try {
            File dir = new File(UPLOAD_DIRECTORY);
            if(!dir.exists()) {
                dir.mkdir();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            String fileDir = UPLOAD_DIRECTORY+"/" + fileName + ".webp";

           try (FileOutputStream fileOutputStream = new FileOutputStream(fileDir)) {
               fileOutputStream.write(data);
           }

            return fileName;
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}
