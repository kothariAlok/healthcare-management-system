package com.healthcare.hms.service;

import com.healthcare.hms.dto.AppointmentRequest;
import com.healthcare.hms.exception.AppointmentConflictException;
import com.healthcare.hms.exception.ResourceNotFoundException;
import com.healthcare.hms.model.Appointment;
import com.healthcare.hms.model.AppointmentStatus;
import com.healthcare.hms.model.Doctor;
import com.healthcare.hms.model.Patient;
import com.healthcare.hms.repository.AppointmentRepository;
import com.healthcare.hms.repository.DoctorRepository;
import com.healthcare.hms.repository.PatientRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private static final long APPOINTMENT_DURATION_MINUTES = 30;

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository) {

        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Transactional
    public Appointment bookAppointment(AppointmentRequest request) {

        // 1. Calculate the appointment end time.
        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime =
                startTime.plusMinutes(APPOINTMENT_DURATION_MINUTES);

        // 2. Lock the doctor row before checking availability.
        Doctor doctor = doctorRepository
                .findByIdForUpdate(request.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with ID: "
                                        + request.getDoctorId()));

        // 3. Verify that the patient exists.
        Patient patient = patientRepository
                .findById(request.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with ID: "
                                        + request.getPatientId()));

        // 4. Check for overlapping appointments.
        boolean conflict =
                appointmentRepository.existsDoctorConflict(
                        doctor.getId(),
                        startTime,
                        endTime,
                        AppointmentStatus.CANCELLED
                );

        if (conflict) {
            throw new AppointmentConflictException(
                    "Doctor already has an appointment during this time."
            );
        }

        // 5. Create and save the appointment.
        Appointment appointment = new Appointment();

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setReason(request.getReason());

        return appointmentRepository.save(appointment);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {

        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException(
                    "Doctor not found with ID: " + doctorId);
        }

        return appointmentRepository
                .findByDoctor_IdOrderByStartTimeAsc(doctorId);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByPatient(Long patientId) {

        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(
                    "Patient not found with ID: " + patientId);
        }

        return appointmentRepository
                .findByPatient_IdOrderByStartTimeDesc(patientId);
    }

    @Transactional
    public Appointment cancelAppointment(Long id) {

        Appointment appointment = getAppointmentById(id);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new AppointmentConflictException(
                    "Appointment is already cancelled.");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new AppointmentConflictException(
                    "A completed appointment cannot be cancelled.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        return appointmentRepository.save(appointment);
    }
}