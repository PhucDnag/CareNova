package com.example.btl_android.Object;

public class TreatmentItem {
    private int treatmentId;
    private int patientId;
    private String patientName;
    private String medicineName;

    public TreatmentItem(int treatmentId, int patientId,
                         String patientName, String medicineName) {
        this.treatmentId = treatmentId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.medicineName = medicineName;
    }

    public int getTreatmentId() {
        return treatmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getMedicineName() {
        return medicineName;
    }
}
