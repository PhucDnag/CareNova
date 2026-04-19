package com.example.btl_android.Object;

import java.io.Serializable;

public class MedicalRecord1 implements Serializable {
    private int id;
    private int patientId;
    private int appointmentId;
    private int doctorId;
    private String doctorName;
    private String visitDate;
    private String diagnosis;
    private String prescription;
    private String doctorNotes;

    public MedicalRecord1(int id, int patientId, int doctorId, int appointmentId, String doctorName, String visitDate, String diagnosis, String prescription, String doctorNotes) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId; // <--- THÊM DÒNG NÀY
        this.appointmentId = appointmentId;
        this.doctorName = doctorName;
        this.visitDate = visitDate;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.doctorNotes = doctorNotes;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public int getAppointmentId() { return appointmentId; } 
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public String getVisitDate() { return visitDate; }
    public void setVisitDate(String visitDate) { this.visitDate = visitDate; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }
    public String getDoctorNotes() { return doctorNotes; }
    public void setDoctorNotes(String doctorNotes) { this.doctorNotes = doctorNotes; }
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
}
