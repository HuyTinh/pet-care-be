package com.pet_care.medicine_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;


@Service
public class ImageUploadService {
	@Autowired
	private Cloudinary cloudinary;
	
	  public String uploadImage(MultipartFile file) throws IOException {
	        if (file.isEmpty()) {
	            throw new IllegalArgumentException("File is empty");
	        }
	        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
	        		"public_id", Base64.getEncoder().encodeToString(file.getBytes()).replace("/", "[").substring(65, 75),
	        		"use_filename_as_display_name", true, 
		            "overwrite", true));
	        return uploadResult.get("secure_url").toString(); 
	    }
}
