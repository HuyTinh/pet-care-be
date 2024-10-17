package com.pet_care.warehouse_manager_service.controllers;

import com.pet_care.warehouse_manager_service.entity.Caculation_Unit;
import com.pet_care.warehouse_manager_service.service.serviceImpl.CaculationUnitServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Caculation Unit - Service")
@RequestMapping("/warehouse-manager/caculation-unit")
public class CaculationUnitController {

    @Autowired
    CaculationUnitServiceImpl caculationUnitService;

    @GetMapping
    public ResponseEntity<List<Caculation_Unit>> getCaculationUnitTrue(){
        return ResponseEntity.ok(caculationUnitService.getCaculationUnitTrue());
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Caculation_Unit>> getAllCaculationUnit(){
        return ResponseEntity.ok(caculationUnitService.findAll());
    }

    @PostMapping
    public ResponseEntity<Caculation_Unit> create(@RequestBody Caculation_Unit caculationUnit){
        return ResponseEntity.ok(caculationUnitService.create(caculationUnit));
    }

    @PutMapping("{id}")
    public ResponseEntity<Caculation_Unit> update(@PathVariable int id,@RequestBody Caculation_Unit caculationUnit){
        return ResponseEntity.ok(caculationUnitService.update(caculationUnit));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@PathVariable List<Long> ids){
        caculationUnitService.deleteCaculationunits(ids);
        return ResponseEntity.ok("Deleted Successfully" );
    }
}
