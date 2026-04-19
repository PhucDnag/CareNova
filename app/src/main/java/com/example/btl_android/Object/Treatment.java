package com.example.btl_android.Object;

public class Treatment {

    private int id;
    private int recordId;
    private String medicineName;
    private String method;
    private int timesPerDay;
    private String purpose;
    private String guide;

    public Treatment() {
    }

    public Treatment(int id, int recordId, String medicineName,
                     String method, int timesPerDay,
                     String purpose, String guide) {
        this.id = id;
        this.recordId = recordId;
        this.medicineName = medicineName;
        this.method = method;
        this.timesPerDay = timesPerDay;
        this.purpose = purpose;
        this.guide = guide;
    }

    // ===== GETTER & SETTER =====

    public int getId() {
        return id;
    }

    public int getRecordId() {
        return recordId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getMethod() {
        return method;
    }

    public int getTimesPerDay() {
        return timesPerDay;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getGuide() {
        return guide;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setTimesPerDay(int timesPerDay) {
        this.timesPerDay = timesPerDay;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }
}
