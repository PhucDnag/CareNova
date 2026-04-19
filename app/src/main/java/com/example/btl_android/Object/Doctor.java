package com.example.btl_android.Object;

public class Doctor {
    private int id;
    private String name;
    private String specialty;
    private String phone;
    private String education;

    public Doctor() {
    }

    public Doctor(int id, String name, String specialty, String phone, String education) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.phone = phone;
        this.education = education;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getPhone() {
        return phone;
    }

    public String getEducation() {
        return education;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEducation(String education) {
        this.education = education;
    }
}
