package com.healthcare.hms.service;

import com.healthcare.hms.model.Doctor;
import com.healthcare.hms.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;


    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElseThrow(()->new RuntimeException("Doctor not found with id" + id));
    }

    @Override
    public Doctor updateDoctor(Long id, Doctor doctor) {
        Doctor doctorDetails = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found with id" + id));

        doctor.setName(doctorDetails.getName());
        doctor.setSpecialization(doctorDetails.getSpecialization());
        doctor.setEmail(doctorDetails.getEmail());
        doctor.setPhone(doctorDetails.getPhone());
        doctor.setGender(doctorDetails.getGender());
        doctor.setExperience(doctorDetails.getExperience());
        return doctorRepository.save(doctor);
    }

    @Override
    public void deleteDoctor(Long id) {

        Doctor doctorDetails = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found with id" + id));

        doctorRepository.deleteById(id);

    }
}
