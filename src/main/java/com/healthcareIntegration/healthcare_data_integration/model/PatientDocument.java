package com.healthcareIntegration.healthcare_data_integration.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import org.bson.Document;

@Document(collection="patients")
public class PatientDocument {
    @Id
    //private String id;
    private Long id;

    private String firstName;
    private String lastName;
    private Date dob;
    private String gender;
    private String address;

    private org.bson.Document medicalInfo;

    public PatientDocument(Long id, String firstName, String lastName, Date dob, String gender, String address, org.bson.Document medicalInfo){
        this.id=id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.dob=dob;
        this.gender=gender;
        this.address=address;
        this.medicalInfo=medicalInfo;
    }

    public void setId(Long id){
        this.id=id;
    }

    public Long getId(){
        return id;
    }

    public void setFirstName(String firstName){
        this.firstName=firstName;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setLastName(String lastName){
        this.lastName=lastName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setDob(Date dob){
        this.dob=dob;
    }

    public Date getDob(){
        return dob;
    }

    public void setGender(String gender){
        this.gender=gender;
    }

    public String getGender(){
        return gender;
    }

    public void setAddress(String address){
        this.address=address;
    }

    public String getAddress(){
        return address;
    }


    public org.bson.Document getMedicalInfo(){
        return medicalInfo;
    }

    public void setMedicalInfo(org.bson.Document medicalInfo){
        this.medicalInfo=medicalInfo;
    }
}