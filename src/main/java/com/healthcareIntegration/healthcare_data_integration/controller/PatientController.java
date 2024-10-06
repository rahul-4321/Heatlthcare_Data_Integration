package com.healthcareIntegration.healthcare_data_integration.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcareIntegration.healthcare_data_integration.model.Patient;
import com.healthcareIntegration.healthcare_data_integration.service.DataAnalysisService;
import com.healthcareIntegration.healthcare_data_integration.service.DataIngestionService;


@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final DataIngestionService dataIngestionService;
    private final DataAnalysisService dataAnalysisService;

    //@Autowired // no need to use it when we have a single cunstructor as the 
    public PatientController(DataIngestionService dataIngestionService, DataAnalysisService dataAnalysisService){
        this.dataAnalysisService = dataAnalysisService;
        this.dataIngestionService= dataIngestionService;
    }

    //1. Endpoint to ingest patient data
    @PostMapping("/ingest")    
    public ResponseEntity<String> ingestPatients(@RequestBody List<Patient> patients, List<org.bson.Document> documents ){
        dataIngestionService.ingestData(patients,documents);
        return ResponseEntity.ok("Patient data ingested successfully");
    }

    //2. Endpoint to get average age of patients
    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageAge() {
        double averageAge= dataAnalysisService.CalculateAverageAge();
        return ResponseEntity.ok(averageAge);
    }

    //3. Endpoint to get gender Distribution
    @GetMapping("/gender-distribution")
    public ResponseEntity<Map<String,Long>> getGenderDistribution(){
        Map<String,Long> distribution = dataAnalysisService.calculateGenderDistribution();
        return ResponseEntity.ok(distribution);
    }

    //4. Endpoint to get most common medical condition
    @GetMapping("/common-condition")
    public ResponseEntity<Map<String,Long>> getCommonConditions(){
        Map<String, Long> commonConditions= dataAnalysisService.findMostCommonMediaclConditions();
        return ResponseEntity.ok(commonConditions);
    }

    //5. Endpoint to get patients with specific medical condition
    @GetMapping("condition/{condition}")
    public ResponseEntity<List<org.bson.Document>> getPatientsWithCondition(@PathVariable String condition){
        List<org.bson.Document> names= dataAnalysisService.findPatientWithCondition(condition);
        return ResponseEntity.ok(names);
    }


    //6. EndPoint to get age distribution
    @GetMapping("/age-distribution")
    public ResponseEntity<Map<String, Long>> getAgeDistribution(){
        Map<String, Long> distribution= dataAnalysisService.calculateAgeDistribution();
        return ResponseEntity.ok(distribution);
    }

    //7. Endpoint to detect rare medical conditions (threshold based)
    @GetMapping("/rare-conditions{threshold}")
    public ResponseEntity<Set<String>> getRareMedicalConditions(@PathVariable long threshold) {
        
        Set<String> rareConditions= dataAnalysisService.detectRareMedicalConditions(threshold);
        return ResponseEntity.ok(rareConditions);
    }

}
