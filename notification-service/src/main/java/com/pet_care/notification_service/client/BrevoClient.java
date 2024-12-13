package com.pet_care.notification_service.client;

// Import necessary annotations and classes for configuring the client and handling dependencies
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration // Indicates that this class contains configuration for Spring beans
public class BrevoClient {

    @Value("${brevo.url}") // Injects the value of brevo.url from application properties
    private String brevoUrl;

    @Value("${brevo.key}") // Injects the value of brevo.key from application properties
    private String brevoKey;

    /**
     * Configures and creates a RestClient bean to connect to the Brevo service.
     *
     * @return a configured RestClient instance
     */
    @Bean // Defines this method as a bean provider for dependency injection
    public RestClient client() {
        System.out.println("Brevo URL: " + brevoUrl);
        System.out.println("Brevo Key: " + brevoKey);
        return RestClient.create() // Creates a base RestClient instance
                .mutate() // Enables customization of the RestClient instance
                .baseUrl(brevoUrl) // Sets the base URL for Brevo API requests
                .defaultHeader("api-key", brevoKey) // Adds the API key to the default headers for authentication
                .defaultHeader("Content-Type", "application/json; charset=UTF-8") // Sets the content type for JSON requests
                .build(); // Builds and returns the configured RestClient instance
    }

}
