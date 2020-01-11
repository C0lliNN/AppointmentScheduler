package com.raphaelcollin.appointmentscheduler.db.model;

import java.time.LocalDate;

public class Patient {
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private String zipCode;
    private String streetNumber;
    private String houseNumber;

    private Patient (Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.gender = builder.gender;
        this.birthDate = builder.birthDate;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.zipCode = builder.zipCode;
        this.streetNumber = builder.streetNumber;
        this.houseNumber = builder.houseNumber;
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

    public String getZipCode() {
        return zipCode;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public static class Builder {

        private String firstName;
        private String lastName;
        private String gender;
        private LocalDate birthDate;
        private String phoneNumber;
        private String email;
        private String zipCode;
        private String streetNumber;
        private String houseNumber;

        public Patient build() {
            return new Patient(this);
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

        public Builder setZipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Builder setStreetNumber(String streetNumber) {
            this.streetNumber = streetNumber;
            return this;
        }

        public Builder setHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }
    }
}
