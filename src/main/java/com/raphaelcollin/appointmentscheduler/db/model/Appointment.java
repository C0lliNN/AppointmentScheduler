package com.raphaelcollin.appointmentscheduler.db.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.raphaelcollin.appointmentscheduler.Main;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class Appointment extends RecursiveTreeObject<Appointment> implements Exportable{

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
        this.statusProperty = new SimpleStringProperty(getTranslatedStatus(this.status));
    }

    @Override
    public String getHeaderLineCSV() {
        return getResources().getString(BUNDLE_KEY_APPOINTMENT_ID) + "," + getResources().getString(BUNDLE_KEY_DATE)
                + "," + getResources().getString(BUNDLE_KEY_PRICE) + "," + getResources().getString(BUNDLE_KEY_DESCRIPTION)
                + "," + getResources().getString(BUNDLE_KEY_STATUS) + "," + getResources().getString(BUNDLE_KEY_DOCTOR_ID)
                + "," + getResources().getString(BUNDLE_KEY_PATIENT_ID);
    }

    @Override
    public String convertToCSV() {

        return id + "," + date.format(DateTimeFormatter.ofPattern(getResources().getString(BUNDLE_KEY_DATE_FORMAT) + " " +
                        getResources().getString(BUNDLE_KEY_TIME_FORMAT))) + ","
                + price + "," + description + "," + Main.getTranslatedStatus(this.status) + "," + doctor.getId() + "," + patient.getId();
    }

    @Override
    public String convertToXML() {

        StringBuilder builder = new StringBuilder("\t<" + getResources().getString(BUNDLE_KEY_TAB_TITLE_APPOINTMENT) + ">\n");

        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_APPOINTMENT_ID)).append(">").append(this.id).append("</").
                append(getResources().getString(BUNDLE_KEY_APPOINTMENT_ID)).append(">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_DATE)).append(">").append(this.date.
                format(DateTimeFormatter.ofPattern(getResources().getString(BUNDLE_KEY_DATE_FORMAT) + " " +
                        getResources().getString(BUNDLE_KEY_TIME_FORMAT)))).append("</").append(getResources().getString(BUNDLE_KEY_DATE)).append(">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_PRICE)).append(">").append(this.price).append("</").
                append(getResources().getString(BUNDLE_KEY_PRICE)).append(">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_DESCRIPTION)).append(">").append(this.description).append("</").
                append(getResources().getString(BUNDLE_KEY_DESCRIPTION)).append(">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_STATUS)).append(">").append(Main.getTranslatedStatus(this.status)).append("</").
                append(getResources().getString(BUNDLE_KEY_STATUS)).append(">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_PATIENT_ID)).append(">").append(this.patient.getId()).append("</").
                append(getResources().getString(BUNDLE_KEY_PATIENT_ID)).append(">\n");
        builder.append("\t\t<").append(getResources().getString(BUNDLE_KEY_DOCTOR_ID)).append(">").append(this.doctor.getId()).append("</").
                append(getResources().getString(BUNDLE_KEY_DOCTOR_ID)).append(">\n");

        builder.append("\t</").append(getResources().getString(BUNDLE_KEY_TAB_TITLE_APPOINTMENT)).append(">\n");

        return builder.toString();
    }

    @Override
    public String convertToJSON() {

        StringBuilder builder = new StringBuilder("{");

        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_APPOINTMENT_ID)).append("\": ").append(this.id).append(",\n");
        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_DATE)).append("\": ").append("\"").append(this.date.
                format(DateTimeFormatter.ofPattern(getResources().getString(BUNDLE_KEY_DATE_FORMAT) + " " +
                        getResources().getString(BUNDLE_KEY_TIME_FORMAT)))).append("\"").append(",\n");
        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_PRICE)).append("\": ").append(this.price).append(",\n");
        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_DESCRIPTION)).append("\": ").append("\"").append(this.description).append("\"").append(",\n");
        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_STATUS)).append("\": ").append("\"").append(Main.getTranslatedStatus(this.status)).
                append("\"").append(",\n");
        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_PATIENT_ID)).append("\": ").append(this.patient.getId()).append(",\n");
        builder.append("\t\"").append(getResources().getString(BUNDLE_KEY_DOCTOR_ID)).append("\": ").append(this.doctor.getId()).append("\n");
        builder.append("}\n");

        return builder.toString();
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Appointment that = (Appointment) object;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
