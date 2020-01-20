package com.raphaelcollin.appointmentscheduler.db.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class Appointment extends RecursiveTreeObject<Appointment>{

    private int id;
    private LocalDateTime date;
    private double price;
    private String description;
    private String status;
    private Doctor doctor;
    private Patient patient;

    private SimpleStringProperty dateProperty;
    private SimpleStringProperty scheduleProperty;
    private SimpleStringProperty patientProperty;
    private SimpleStringProperty descriptionProperty;
    private SimpleStringProperty doctorProperty;
    private SimpleDoubleProperty priceProperty;
    private SimpleStringProperty statusProperty;

    private Appointment(Builder builder) {
        this.id = builder.id;
        this.date = builder.date;
        this.price = builder.price;
        this.description = builder.description;
        this.status = builder.status;
        this.doctor = builder.doctor;
        this.patient = builder.patient;


        String datePattern = getResources().getString(BUNDLE_KEY_DATE_FORMAT).trim();
        String stringDate = this.date.toLocalDate().format(DateTimeFormatter.ofPattern(datePattern));

        String timePattern = getResources().getString(BUNDLE_KEY_TIME_FORMAT).trim();
        String schedule = this.date.toLocalTime().format(DateTimeFormatter.ofPattern(timePattern));

        this.dateProperty = new SimpleStringProperty(stringDate);
        this.scheduleProperty = new SimpleStringProperty(schedule);
        this.patientProperty = new SimpleStringProperty(patient.getName());
        this.descriptionProperty = new SimpleStringProperty(this.description);
        this.doctorProperty = new SimpleStringProperty(this.doctor.getName());
        this.priceProperty = new SimpleDoubleProperty(this.price);
        this.statusProperty = new SimpleStringProperty(this.status);
    }

    public ObservableValue<String> getDateProperty() {
        return dateProperty;
    }

    public ObservableValue<String> getScheduleProperty() {
        return scheduleProperty;
    }

    public ObservableValue<String> getPatientProperty() {
        return patientProperty;
    }

    public ObservableValue<String> getDescriptionProperty() {
        return descriptionProperty;
    }

    public ObservableValue<String> getDoctorProperty() {
        return doctorProperty;
    }

    public ObservableValue<Double> getPriceProperty() {
        return priceProperty.asObject();
    }

    public ObservableValue<String> getStatusProperty() {
        return statusProperty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }



    public static class Builder {
        private int id;
        private LocalDateTime date;
        private double price;
        private String description;
        private String status;
        private Doctor doctor;
        private Patient patient;

        public Appointment build() {
            return new Appointment(this);
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Builder setPrice(double price) {
            this.price = price;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setDoctor(Doctor doctor) {
            this.doctor = doctor;
            return this;
        }

        public Builder setPatient(Patient patient) {
            this.patient = patient;
            return this;
        }
    }
}
