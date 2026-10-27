package com.healthcare.hms.repository;

import com.healthcare.hms.model.Appointment;
import com.healthcare.hms.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
        SELECT COUNT(a) > 0
        FROM Appointment a
        WHERE a.doctor.id = :doctorId
          AND a.status <> :cancelled
          AND a.startTime < :requestedEnd
          AND a.endTime > :requestedStart
        """)
    boolean existsDoctorConflict(
            @Param("doctorId") Long doctorId,
            @Param("requestedStart") LocalDateTime requestedStart,
            @Param("requestedEnd") LocalDateTime requestedEnd,
            @Param("cancelled") AppointmentStatus cancelled
    );

    List<Appointment> findByDoctor_IdOrderByStartTimeAsc(Long doctorId);

    List<Appointment> findByPatient_IdOrderByStartTimeDesc(Long patientId);
}