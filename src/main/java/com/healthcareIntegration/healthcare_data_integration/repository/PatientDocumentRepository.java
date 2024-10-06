package com.healthcareIntegration.healthcare_data_integration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.healthcareIntegration.healthcare_data_integration.model.PatientDocument;

@Repository
public interface PatientDocumentRepository extends MongoRepository<PatientDocument,Long>{

}
