package com.healthcare.hms.service;

import com.healthcare.hms.model.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PatientService {
    // Create
    Patient createPatient(Patient patient);

    // Read All
    List<Patient> getAllPatients();


    // Read by ID
    Patient getPatientById(Long id);

    // Update Patient
    Patient updatePatient(Long id, Patient patient);

    // Delete Patient
    void deletePatient(Long id);

    //Search Patient by Keyword
    List<Patient> searchPatients(String keyword);

}
