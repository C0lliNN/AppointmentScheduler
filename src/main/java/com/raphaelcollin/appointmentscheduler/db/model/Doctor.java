package com.raphaelcollin.appointmentscheduler.db.model;

import java.time.LocalDate;

public class Doctor {
    private String name;
    private String phoneNumber;
    private String gender;
    private LocalDate birthDate;
    private String licenseNumber;

    public Doctor(String name) {
        this.name = name;
    }

    public Doctor(String name, String phoneNumber, String gender, LocalDate birthDate, String licenseNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
        this.licenseNumber = licenseNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }
}
