package com.raphaelcollin.appointmentscheduler.db.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.raphaelcollin.appointmentscheduler.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class Patient extends RecursiveTreeObject<Patient> implements Exportable {

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
        this.city = builder.city;
        this.zipCode = builder.zipCode;
        this.streetName = builder.streetName;
        this.houseNumber = builder.houseNumber;


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

        if (zipCode == null || zipCode.isEmpty() || streetName == null || streetName.isEmpty() || houseNumber == null
                || houseNumber.isEmpty() || city == null || city.isEmpty()) {
            addressProperty = new SimpleStringProperty("");
        } else {
            addressProperty = new SimpleStringProperty(this.city + ", " + this.streetName + " " + this.houseNumber + " - " + this.zipCode);
        }
    }

    @Override
    public String getHeaderLineCSV() {
        return getResources().getString(BUNDLE_KEY_PATIENT_ID) + "," + getResources().getString(BUNDLE_KEY_FIRST_NAME)
                + "," + getResources().getString(BUNDLE_KEY_LAST_NAME) + "," + getResources().getString(BUNDLE_KEY_GENDER)
                + "," + getResources().getString(BUNDLE_KEY_BIRTH_DATE) + "," + getResources().getString(BUNDLE_KEY_PHONE_NUMBER)
                + "," + getResources().getString(BUNDLE_KEY_EMAIL) + "," + getResources().getString(BUNDLE_KEY_CITY)
                + "," + getResources().getString(BUNDLE_KEY_ZIP_CODE) + "," + getResources().getString(BUNDLE_KEY_STREET_NAME)
                + "," + getResources().getString(BUNDLE_KEY_HOUSE_NUMBER);
    }

    @Override
    public String convertToCSV() {

        return id + "," + firstName + "," + lastName + "," + Main.getTranslatedGender(this.gender) + "," +
                birthDate.format(DateTimeFormatter.ofPattern(getResources().getString(BUNDLE_KEY_DATE_FORMAT))) + "," +
                phoneNumber + "," +
                (email == null ? "" : email) + "," +
                (city == null ? "" : city) + "," +
                (zipCode == null ? "" : zipCode) + "," +
                (streetName == null ? "" : streetName) + "," +
                (houseNumber == null ? "" : houseNumber);
    }

    @Override
    public String convertToXML() {

        StringBuilder builder = new StringBuilder("\t<" + getResources().getString(BUNDLE_KEY_PATIENT) + ">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_PATIENT_ID)).append(">").append(this.id).append("</").
                append(getResources().getString(BUNDLE_KEY_PATIENT_ID)).append(">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_FIRST_NAME)).append(">").append(this.firstName).append("</").
                append(getResources().getString(BUNDLE_KEY_FIRST_NAME)).append(">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_LAST_NAME)).append(">").append(this.lastName).append("</").
                append(getResources().getString(BUNDLE_KEY_LAST_NAME)).append(">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_GENDER)).append(">").append(Main.getTranslatedGender(this.gender)).append("</").
                append(getResources().getString(BUNDLE_KEY_GENDER)).append(">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_BIRTH_DATE)).append(">").append(this.birthDate.format(DateTimeFormatter.
                ofPattern(getResources().getString(BUNDLE_KEY_DATE_FORMAT)))).append("</").
                append(getResources().getString(BUNDLE_KEY_BIRTH_DATE)).append(">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_PHONE_NUMBER)).append(">").append(this.phoneNumber).append("</").
                append(getResources().getString(BUNDLE_KEY_PHONE_NUMBER)).append(">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_EMAIL)).append(">").append(email == null ? "" : email).append("</").
                append(getResources().getString(BUNDLE_KEY_EMAIL)).append(">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_CITY)).append(">").append(city == null ? "" : city).append("</").
                append(getResources().getString(BUNDLE_KEY_CITY)).append(">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_ZIP_CODE)).append(">").append(zipCode == null ? "" : zipCode).append("</").
                append(getResources().getString(BUNDLE_KEY_ZIP_CODE)).append(">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_STREET_NAME)).append(">").append(streetName == null ? "" : streetName).append("</").
                append(getResources().getString(BUNDLE_KEY_STREET_NAME)).append(">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_HOUSE_NUMBER)).append(">").append(houseNumber == null ? "" : houseNumber).append("</").
                append(getResources().getString(BUNDLE_KEY_HOUSE_NUMBER)).append(">\n");


        builder.append("\t</").append(getResources().getString(BUNDLE_KEY_PATIENT)).append(">\n");

        return builder.toString();
    }

    @Override
    public String convertToJSON() {
        StringBuilder builder = new StringBuilder("{");

        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_PATIENT_ID)).append("\": ").append(this.id).append(",\n");
        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_FIRST_NAME)).append("\": ").append("\"").append(this.firstName).append("\"").append(",\n");
        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_LAST_NAME)).append("\": ").append("\"").append(this.lastName).append("\"").append(",\n");
        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_GENDER)).append("\": ").append("\"").append(Main.getTranslatedGender(this.gender)).
                append("\"").append(",\n");
        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_BIRTH_DATE)).append("\": ").append("\"").append(this.birthDate.
                format(DateTimeFormatter.ofPattern(getResources().getString(BUNDLE_KEY_DATE_FORMAT)))).append("\"").append(",\n");
        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_PHONE_NUMBER)).append("\": ").append("\"").append(this.phoneNumber).append("\"").append(",\n");
        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_EMAIL)).append("\": ").append("\"").append(email == null ? "" : email).append("\"").append(",\n");
        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_CITY)).append("\": ").append("\"").append(city == null ? "" : city).append("\"").append(",\n");
        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_ZIP_CODE)).append("\": ").append("\"").append(zipCode == null ? "" : zipCode).append("\"").append(",\n");
        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_STREET_NAME)).append("\": ").append("\"").append(streetName == null ? "" : streetName).append("\"").append(",\n");
        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_HOUSE_NUMBER)).append("\": ").append("\"").append(houseNumber == null ? "" : houseNumber).append("\"").append("\n");
        builder.append("}\n");

        return builder.toString();
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

    public void setGender(String gender) {
        this.gender = gender;
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
