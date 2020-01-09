package com.raphaelcollin.appointmentscheduler.db;

import com.raphaelcollin.appointmentscheduler.db.model.Appointment;
import com.raphaelcollin.appointmentscheduler.db.model.Doctor;
import com.raphaelcollin.appointmentscheduler.db.model.Patient;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Datasource {

    private ObservableList<Appointment> appointments;
    private ObservableList<Patient> patients;
    private ObservableList<Doctor> doctors;

    private PropertyChangeSupport observers;

    public static final String APPOINTMENTS_CHANGE = "Appointment";
    public static final String DOCTORS_CHANGE = "Doctor";
    public static final String PATIENTS_CHANGE = "Patient";


    private Datasource() {}

    public void addObserver(PropertyChangeListener listener) {
        observers.addPropertyChangeListener(listener);
    }
    public int addAppointment(Appointment appointment) {
        observers.firePropertyChange(APPOINTMENTS_CHANGE, null, appointments);
        return 1;
    }

    public boolean deleteAppointment(int id) {
        observers.firePropertyChange(APPOINTMENTS_CHANGE, null, appointments);
        return true;
    }

    public boolean updateAppointment(int id, Appointment appointment) {
        observers.firePropertyChange(APPOINTMENTS_CHANGE, null, appointments);
        return true;
    }


    public int addDoctor(Doctor doctor) {
        observers.firePropertyChange(DOCTORS_CHANGE, null, doctors);
        return 1;
    }

    public boolean deleteDoctor(int id) {
        observers.firePropertyChange(DOCTORS_CHANGE, null, doctors);
        return true;
    }

    public boolean updateDoctor(int id, Doctor newDoctor) {
        observers.firePropertyChange(DOCTORS_CHANGE, null, doctors);
        return true;
    }

    public int addPatient(Patient patient) {
        observers.firePropertyChange(PATIENTS_CHANGE, null, patients);
        return 1;
    }

    public boolean deletePatient(int id) {
        observers.firePropertyChange(PATIENTS_CHANGE, null, patients);
        return true;
    }

    public boolean updatePatient(int id, Patient newPatient) {
        observers.firePropertyChange(PATIENTS_CHANGE, null, patients);
        return true;
    }

    public void loadInitialData() {
        // Get All the Patients, Doctors and Appointments
    }

    private static class SingletonHelper {
        private static Datasource instance = new Datasource();
    }

    public static Datasource getInstance() {
        return SingletonHelper.instance;
    }


}
