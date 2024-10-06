package com.healthcareIntegration.healthcare_data_integration.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.springframework.stereotype.Service;

import com.healthcareIntegration.healthcare_data_integration.model.Patient;
import com.healthcareIntegration.healthcare_data_integration.repository.PatientDocumentRepository;
import com.healthcareIntegration.healthcare_data_integration.repository.PatientRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@Service
public class DataAnalysisService {

    private final PatientRepository sqlRepository;
    private final PatientDocumentRepository mongoRepository;
    private final MongoDatabase mongoDatabase;

    //@Autowired
    public DataAnalysisService(PatientRepository sqlRepository, PatientDocumentRepository mongoRepository,
    MongoDatabase mongoDatabase){
        this.sqlRepository=sqlRepository;
        this.mongoRepository=mongoRepository;
        this.mongoDatabase=mongoDatabase;
    }


    //1. Calculate average age of Patients
    public double CalculateAverageAge(){
        List<Patient> patients=sqlRepository.findAll();
        OptionalDouble averageAge=patients.stream().mapToInt(patient-> calculateAge(patient.getDob())).average();

        return averageAge.orElse(0);
    }

    public int calculateAge(Date dob){

        LocalDate CurrentDate= LocalDate.now();
        LocalDate Bday=dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return Period.between(CurrentDate, Bday).getYears();
    }

    //2. Gender Distribution
    public Map<String, Long> calculateGenderDistribution(){
        List<Patient> patients= sqlRepository.findAll();

        return patients.stream().collect(
        Collectors.groupingBy(Patient::getGender,Collectors.counting())
        );
    }

    //3. common Medical Conditions from MongoDB
    public Map<String, Long> findMostCommonMediaclConditions(){

        MongoCollection<Document> collection=mongoDatabase.getCollection("patients");

        Map<String,Long> conditionCounts = new HashMap<>();

        //Fetch all patients and count conditions
        StreamSupport.stream(collection.find().spliterator(),false).
        forEach(document->{
            List<String> medicalHistory= (List<String>) document.get("medicalHistory");

            if(medicalHistory!=null){
                for(String condition:medicalHistory){
                    conditionCounts.put(condition, conditionCounts.getOrDefault(condition,0L)+1);

                }
            }
        });
        return conditionCounts;
    }

    //4. find patients with specific medical condition from MongoDB
    public List<Document> findPatientWithCondition(String condition){
        MongoCollection<Document> collection = mongoDatabase.getCollection("patients");

        return collection.find(Filters.elemMatch("medicalHistory", Filters.eq(condition))).into (new ArrayList<>());
    }

    //5. Total number of patients per age range
    public Map<String, Long> calculateAgeDistribution(){

        List<Patient> patients = sqlRepository.findAll();
        Map<String, Long> ageDistribution = new HashMap<>();

        for(Patient patient: patients){
            int age=calculateAge(patient.getDob());
            String range=getAgeRange(age);
            ageDistribution.put(range,ageDistribution.getOrDefault(range,0L)+1);
        }
        return ageDistribution;
    }

    public String getAgeRange(int age){
        if (age < 18) {
            return "0-17";
        } else if (age < 40) {
            return "18-39";
        } else if (age < 60) {
            return "40-59";
        } else {
            return "60+";
        }
    }

    //6. Detect rare medical conditions (appearing than a threshold)
    public Set<String> detectRareMedicalConditions(long threshold){
        Map<String, Long> commonConditions= findMostCommonMediaclConditions();
        return commonConditions.entrySet().stream()
        .filter(entry-> entry.getValue() < threshold)
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());
    }

}