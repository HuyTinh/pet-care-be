package com.pet_care.upload_service.config;

// Import necessary classes for Cloudinary configuration and Spring annotations
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Marks this class as a configuration for Spring beans
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name}") // Injects the Cloudinary cloud name from application properties
    private String cloudName;

    @Value("${cloudinary.api-key}") // Injects the Cloudinary API key from application properties
    private String apiKey;

    @Value("${cloudinary.api-secret}") // Injects the Cloudinary API secret from application properties
    private String apiSecret;

    /**
     * Provides a Cloudinary bean to interact with the Cloudinary service.
     *
     * @return a configured Cloudinary instance
     */
    @Bean // Defines this method as a bean provider for dependency injection
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap( // Creates and returns a Cloudinary instance with the configured properties
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }
}
