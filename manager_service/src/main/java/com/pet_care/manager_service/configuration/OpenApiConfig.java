package com.pet_care.manager_service.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info( new Info()
                        .title("API") // đặt tên title API
//                        .description("Management API") // mô tả của API
                        .version("1.0") //cài đặt phiên bản cho API
        );
    }

    @Bean
    public GroupedOpenApi groupedOpenApiManager(){
        return GroupedOpenApi.builder()
                .group("api-service-manager")
                .packagesToScan("com.pet_care.manager_service.controllers")
                .build()
        ;
    }
//
    @Bean
    public GroupedOpenApi groupedOpenApiAccount(){
        return GroupedOpenApi.builder()
                .group("api-service-account")
                .pathsToMatch("/management/account/**") // dựa theo url của controller
                .build()
                ;
    }

    @Bean
    public GroupedOpenApi groupedOpenApiCustomer(){
        return GroupedOpenApi.builder()
                .group("api-service-customer")
                .pathsToMatch("/management/customer/**") // dựa theo url của controller
//                .packagesToScan("com.pet_care.manager_service.controllers.ownerController")
                .build()
                ;
    }

    @Bean
    public GroupedOpenApi groupedOpenApiAppointment(){
        return GroupedOpenApi.builder()
                .group("api-service-appointment")
                .pathsToMatch("/management/appointment/**") // dựa theo url của controller
//                .packagesToScan("com.pet_care.manager_service.controllers.appointmentController")
                .build()
                ;
    }

    @Bean
    public GroupedOpenApi groupedOpenApiService(){
        return GroupedOpenApi.builder()
                .group("api-service-service")
                .pathsToMatch("/management/service/**") // dựa theo url của controller
                .build()
                ;
    }

    @Bean
    public GroupedOpenApi groupedOpenApiRole(){
        return GroupedOpenApi.builder()
                .group("api-service-role")
                .pathsToMatch("/management/role/**") // dựa theo url của controller
                .build()
                ;
    }

    @Bean
    public GroupedOpenApi groupedOpenApiDashBoard(){
        return GroupedOpenApi.builder()
                .group("api-service-dashboard")
                .pathsToMatch("/management/dashboard/**") // dựa theo url của controller
                .build()
                ;
    }

    @Bean
    public GroupedOpenApi groupedOpenApiReport(){
        return GroupedOpenApi.builder()
                .group("api-service-report")
                .pathsToMatch("/management/report/**") // dựa theo url của controller
                .build()
                ;
    }
}
