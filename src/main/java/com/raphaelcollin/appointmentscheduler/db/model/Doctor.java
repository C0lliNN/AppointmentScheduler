package com.raphaelcollin.appointmentscheduler.db.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class Doctor extends RecursiveTreeObject<Doctor> {
    private int id;
    private String name;
    private String phoneNumber;
    private String gender;
    private LocalDate birthDate;
    private String licenseNumber;

    private SimpleStringProperty nameProperty;
    private SimpleStringProperty genderProperty;
    private SimpleStringProperty birthDateProperty;
    private SimpleStringProperty phoneNumberProperty;
    private SimpleStringProperty licenseNumberProperty;

    private Doctor(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.phoneNumber = builder.phoneNumber;
        this.gender = builder.gender;
        this.birthDate = builder.birthDate;
        this.licenseNumber = builder.licenseNumber;

        nameProperty = new SimpleStringProperty(this.name);
        if (this.gender.equals(MALE)) {
            genderProperty = new SimpleStringProperty(getResources().getString(BUNDLE_KEY_GENDER_MALE));
        } else {
            genderProperty = new SimpleStringProperty(getResources().getString(BUNDLE_KEY_GENDER_FEMALE));
        }
        birthDateProperty = new SimpleStringProperty(this.birthDate.format(DateTimeFormatter.ofPattern(getResources().
                getString(BUNDLE_KEY_DATE_FORMAT))));
        phoneNumberProperty = new SimpleStringProperty(this.phoneNumber);
        licenseNumberProperty = new SimpleStringProperty(licenseNumber);

    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public ObservableValue<String> getNameProperty() {
        return nameProperty;
    }
    public ObservableValue<String> getGenderProperty() {
        return genderProperty;
    }
    public ObservableValue<String> getBirthDateProperty() {
        return birthDateProperty;
    }
    public ObservableValue<String> getPhoneProperty() {
        return phoneNumberProperty;
    }
    public ObservableValue<String> getLicenseNumberProperty() {
        return licenseNumberProperty;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Doctor doctor = (Doctor) object;
        return id == doctor.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class Builder {
        private int id;
        private String name;
        private String phoneNumber;
        private String gender;
        private LocalDate birthDate;
        private String licenseNumber;

        public Doctor build() {
            return new Doctor(this);
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name= name;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber= phoneNumber;
            return this;
        }

        public Builder setGender(String gender) {
            this.gender= gender;
            return this;
        }

        public Builder setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder setLicenseNumber(String licenseNumber) {
            this.licenseNumber= licenseNumber;
            return this;
        }

    }
}
