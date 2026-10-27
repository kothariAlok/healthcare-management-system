package com.healthcare.hms.service;

import com.healthcare.hms.model.Patient;
import com.healthcare.hms.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {


    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Patient createPatient(Patient patient) {
        if (patientRepository.existsByPhoneNumber(patient.getPhoneNumber())) {
            throw new RuntimeException("Phone number already registered");
        }
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found with id :" + id));
    }

    @Override
    public Patient updatePatient(Long id, Patient patient) {

        Patient existingPatient = patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found with id :" + id));

        existingPatient.setFirstName(patient.getFirstName());
        existingPatient.setLastName(patient.getLastName());
        existingPatient.setDateOfBirth(patient.getDateOfBirth());
        existingPatient.setGender(patient.getGender());
        existingPatient.setPhoneNumber(patient.getPhoneNumber());
        existingPatient.setEmail(patient.getEmail());
        existingPatient.setAddress(patient.getAddress());
        existingPatient.setBloodGroup(patient.getBloodGroup());
        existingPatient.setEmergencyContact(patient.getEmergencyContact());
        existingPatient.setMedicalHistory(patient.getMedicalHistory());
        existingPatient.setAllergies(patient.getAllergies());

        return patientRepository.save(existingPatient);

    }

    @Override
    public void deletePatient(Long id) {

        Patient patient = patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));

        patientRepository.delete(patient);
    }


    @Override
    public List<Patient> searchPatients(String keyword) {
        return patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword);
    }
}


