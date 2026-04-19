package com.example.btl_android.Object;

import java.io.Serializable;

public class MedicalRecord implements Serializable {
    private int id;
    private int patientId;
    private int doctorId;
    private String visitDate;
    private String diagnosis;
    private String prescription;
    private String doctorNotes;

    public MedicalRecord() {
    }

    public MedicalRecord(int id, int patientId, int doctorId, String visitDate, String diagnosis, String prescription, String doctorNotes) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.visitDate = visitDate;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.doctorNotes = doctorNotes;
    }

    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public String getPrescription() {
        return prescription;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }
}