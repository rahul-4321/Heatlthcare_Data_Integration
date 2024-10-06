package com.healthcareIntegration.healthcare_data_integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcareIntegration.healthcare_data_integration.repository.PatientDocumentRepository;
import com.healthcareIntegration.healthcare_data_integration.repository.PatientRepository;
import com.healthcareIntegration.healthcare_data_integration.model.Patient;
import com.healthcareIntegration.healthcare_data_integration.model.PatientDocument;
import java.util.List;


@Service
public class DataIngestionService {
    private final PatientRepository sqlRepository;
    private final PatientDocumentRepository mongoRepository;

    public DataIngestionService(PatientRepository sqlRepository, PatientDocumentRepository mongoRepository){
        this.sqlRepository=sqlRepository;
        this.mongoRepository=mongoRepository;
    }

    @Autowired
    public void ingestData(List<Patient> patients,List<org.bson.Document> medicalInfos){
        
        //save to SQL (postgresSQL)
        sqlRepository.saveAll(patients);

        //save to NoSQL(MongoDB)
        
        for(int i=0;i<patients.size();i++){

            Patient patient=patients.get(i);
            org.bson.Document medicalInfo= medicalInfos.get(i);

            PatientDocument document =new PatientDocument(
                patient.getId(),patient.getFirstName(),patient.getLastName(),patient.getDob(),patient.getGender(),patient.getAddress(),medicalInfo
            );

            mongoRepository.save(document);
        }
    }

}
