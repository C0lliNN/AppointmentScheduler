package com.raphaelcollin.appointmentscheduler.db.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.time.LocalDateTime;
import java.util.Locale;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class Appointment extends RecursiveTreeObject<Appointment> {
    private int idAppointment;
    private LocalDateTime date;
    private double price;
    private String description;
    private String status;
    private Doctor doctor;
    private Patient patient;

    private SimpleStringProperty scheduleProperty;
    private SimpleStringProperty patientProperty;
    private SimpleStringProperty descriptionProperty;
    private SimpleStringProperty doctorProperty;
    private SimpleDoubleProperty priceProperty;
    private SimpleStringProperty statusProperty;

    private Appointment(Builder builder) {
        this.idAppointment = builder.idAppointment;
        this.date = builder.date;
        this.price = builder.price;
        this.description = builder.description;
        this.status = builder.status;
        this.doctor = builder.doctor;
        this.patient = builder.patient;

        String currentLanguage = getPreferences().get(PREFERENCES_KEY_LANGUAGE, DEFAULT_LANGUAGE);
        String schedule = "Error";

        if (currentLanguage.equals(Locale.ENGLISH.getLanguage())) {

            if (this.date.getHour() > 12) {
                schedule = adjustDate(this.date.getHour() - 12) + ":" + adjustDate(this.date.getMinute()) + " PM";
            } else {
                schedule = adjustDate(this.date.getHour()) + ":" + adjustDate(this.date.getMinute()) + " AM";
            }
        } else if (currentLanguage.equals("pt")) {
            schedule = adjustDate(this.date.getHour()) + ":" + adjustDate(this.date.getMinute());
        }

        this.scheduleProperty = new SimpleStringProperty(schedule);
        this.patientProperty = new SimpleStringProperty(patient.getName());
        this.descriptionProperty = new SimpleStringProperty(this.description);
        this.doctorProperty = new SimpleStringProperty(this.doctor.getName());
        this.priceProperty = new SimpleDoubleProperty(this.price);
        this.statusProperty = new SimpleStringProperty(this.status);
    }


    private String adjustDate(int number) {
        if (number < 10) {
            return "0" + number;
        }

        return String.format("%d", number);
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

    public static class Builder {
        private int idAppointment;
        private LocalDateTime date;
        private double price;
        private String description;
        private String status;
        private Doctor doctor;
        private Patient patient;

        public Appointment build() {
            return new Appointment(this);
        }

        public Builder setIdAppointment(int idAppointment) {
            this.idAppointment = idAppointment;
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
