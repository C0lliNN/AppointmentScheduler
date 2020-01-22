package com.raphaelcollin.appointmentscheduler.db.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class Patient extends RecursiveTreeObject<Patient> {
    private int id;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private String city;
    private String zipCode;
    private String streetName;
    private String houseNumber;

    private SimpleStringProperty nameProperty;
    private SimpleStringProperty genderProperty;
    private SimpleStringProperty birthDateProperty;
    private SimpleStringProperty phoneNumberProperty;
    private SimpleStringProperty emailProperty;
    private SimpleStringProperty addressProperty;

    private Patient (Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.gender = builder.gender;
        this.birthDate = builder.birthDate;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.zipCode = builder.zipCode;
        this.streetName = builder.streetName;
        this.houseNumber = builder.houseNumber;
        this.city = builder.city;

        nameProperty = new SimpleStringProperty(this.firstName + " " + this.lastName);
        if (this.gender.equals(MALE)) {
            genderProperty = new SimpleStringProperty(getResources().getString(BUNDLE_KEY_GENDER_MALE));
        } else {
            genderProperty = new SimpleStringProperty(getResources().getString(BUNDLE_KEY_GENDER_FEMALE));
        }
        birthDateProperty = new SimpleStringProperty(this.birthDate.format(DateTimeFormatter.ofPattern(getResources().
                getString(BUNDLE_KEY_DATE_FORMAT))));
        phoneNumberProperty = new SimpleStringProperty(this.phoneNumber);
        emailProperty = new SimpleStringProperty(this.email == null ? "" : email);

        if (zipCode == null || streetName == null || houseNumber == null || city == null) {
            addressProperty = new SimpleStringProperty("");
        } else {
            addressProperty = new SimpleStringProperty(this.city + ", " + this.streetName + " " + this.houseNumber + " - " + this.zipCode);
        }

    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
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
    public ObservableValue<String> getEmailProperty() {
        return emailProperty;
    }
    public ObservableValue<String> getAddressProperty() {
        return addressProperty;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Patient patient = (Patient) object;
        return id == patient.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class Builder {
        private int id;
        private String firstName;
        private String lastName;
        private String gender;
        private LocalDate birthDate;
        private String phoneNumber;
        private String email;
        private String city;
        private String zipCode;
        private String streetName;
        private String houseNumber;

        public Patient build() {
            return new Patient(this);
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setGender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setZipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Builder setStreetName(String streetName) {
            this.streetName = streetName;
            return this;
        }

        public Builder setHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }
    }
}
