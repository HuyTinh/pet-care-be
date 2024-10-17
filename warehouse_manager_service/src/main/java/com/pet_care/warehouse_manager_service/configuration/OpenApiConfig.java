package com.pet_care.warehouse_manager_service.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
    public GroupedOpenApi groupedOpenApiWarehouse(){
        return GroupedOpenApi.builder()
                .group("api-service-warehouse")
                .packagesToScan("com.pet_care.warehouse_manager_service.controllers")
                .build()
                ;
    }
    //
    @Bean
    public GroupedOpenApi groupedOpenApiLocation(){
        return GroupedOpenApi.builder()
                .group("api-service-location")
                .pathsToMatch("/warehouse-manager/location/**") // dựa theo url của controller
//                .packagesToScan("com.pet_care.manager_service.controllers.accountController")
                .build()
                ;
    }

    @Bean
    public GroupedOpenApi groupedOpenApiCaculationUnit(){
        return GroupedOpenApi.builder()
                .group("api-service-caculation-unit")
                .pathsToMatch("/warehouse-manager/caculation-unit/**") // dựa theo url của controller
//                .packagesToScan("com.pet_care.manager_service.controllers.ownerController")
                .build()
                ;
    }

    @Bean
    public GroupedOpenApi groupedOpenApiManufacturer(){
        return GroupedOpenApi.builder()
                .group("api-service-manufacturer")
                .pathsToMatch("/warehouse-manager/manufacturer/**") // dựa theo url của controller
//                .packagesToScan("com.pet_care.manager_service.controllers.appointmentController")
                .build()
                ;
    }

    @Bean
    public GroupedOpenApi groupedOpenApiMedicine(){
        return GroupedOpenApi.builder()
                .group("api-service-medicine")
                .pathsToMatch("/warehouse-manager/medicine/**") // dựa theo url của controller
//                .packagesToScan("com.pet_care.manager_service.controllers.serviceController")
                .build()
                ;
    }
}
