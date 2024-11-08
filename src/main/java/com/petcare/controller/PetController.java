package com.petcare.controller;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.petcare.dto.request.SearchRequest;
import com.petcare.dto.response.DataResponse;
import com.petcare.dto.response.PaginationResponse;
import com.petcare.dto.response.PetDetailResponse;
import com.petcare.dto.response.PetResponse;
import com.petcare.entity.Pet;
import com.petcare.mapper.PetDetailMapper;
import com.petcare.mapper.PetMapper;
import com.petcare.service.PetService;
import com.petcare.utils.ExcelExport;
import com.petcare.utils.PDFExport;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.petcare.utils.PDFExport.addBody;
import static com.petcare.utils.PDFExport.addHeader;

@RestController
@RequestMapping("api/v1/management")
@CrossOrigin("*")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping("/{page}")
        public DataResponse getPets(@PathVariable int page) {
    
            Page<Pet> pets = petService.getAllPets(page);
            List<PetResponse> petResponses = PetMapper.INSTANCE.mapperPetsToPetsResponse(pets.getContent());
    
            PaginationResponse paginationResponse = new PaginationResponse();
            paginationResponse.setPetResponses(petResponses);
            paginationResponse.setCurrentPage(page);
            paginationResponse.setTotalPages(pets.getTotalPages());
    
            DataResponse dataResponse = new DataResponse();
            dataResponse.setData(paginationResponse);
    
            return dataResponse;
    }

    @GetMapping()
    public DataResponse getPetsNoPage() {

        Page<Pet> pets = petService.getAllPets(0);
        List<PetResponse> petResponses = PetMapper.INSTANCE.mapperPetsToPetsResponse(pets.getContent());

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setPetResponses(petResponses);
        paginationResponse.setCurrentPage(1);
        paginationResponse.setTotalPages(pets.getTotalPages());

        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(paginationResponse);

        return dataResponse;
    }

    @GetMapping("/getById/{petId}")
    public DataResponse getPetById(@PathVariable("petId") long petId) {

        Pet pet = petService.getPetById(petId);
        PetDetailResponse petDetailResponse = PetDetailMapper.INSTANCE.mapperPetToPetDetailResponse(pet);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(petDetailResponse);

        return dataResponse;
    }

    @PostMapping("/getByAny")
    public DataResponse getByAny(@RequestBody SearchRequest searchRequest) {

        List<Pet> pets = petService.searchPets(searchRequest);
        List<PetResponse> petResponse = PetMapper.INSTANCE.mapperPetsToPetsResponse(pets);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(petResponse);

        return dataResponse;
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportToExcel() throws IOException {
        try {
            String fileName = "pet.xlsx";
            ByteArrayInputStream inputStream = petService.getActualData();
            InputStreamResource file = new InputStreamResource(inputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportToPDF() throws IOException {

        List<Pet> pets = petService.getAllPetsNoPage();
        List<PetResponse> petResponses = PetMapper.INSTANCE.mapperPetsToPetsResponse(pets);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

            addHeader(document);
            addBody(document, petResponses);

            document.close();

            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; charset=utf-8; filename=pets_report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

}
