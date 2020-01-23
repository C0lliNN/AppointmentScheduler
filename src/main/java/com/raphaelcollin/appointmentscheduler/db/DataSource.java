package com.raphaelcollin.appointmentscheduler.db;

import com.raphaelcollin.appointmentscheduler.DatabaseCredentials;
import com.raphaelcollin.appointmentscheduler.db.dao.DAO;
import com.raphaelcollin.appointmentscheduler.db.dao.DAOFactory;
import com.raphaelcollin.appointmentscheduler.db.model.Appointment;
import com.raphaelcollin.appointmentscheduler.db.model.Doctor;
import com.raphaelcollin.appointmentscheduler.db.model.Patient;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Connection;

public class DataSource {

    private ObservableList<Appointment> appointments;
    private ObservableList<Patient> patients;
    private ObservableList<Doctor> doctors;

    private PropertyChangeSupport observers;

    public static final String APPOINTMENTS_CHANGE = "Appointment_change";
    public static final String DOCTORS_CHANGE = "Doctor_change";
    public static final String PATIENTS_CHANGE = "Patient_change";
    public static final String INITIAL_DATA_LOADED = "Initial_Data_Loaded";


    private Connection connection;

    private DataSource() {
        connection = ConnectionFactory.getConnection(DatabaseCredentials.getSavedCredentials());
        observers = new PropertyChangeSupport(this);
    }

    public void addObserver(PropertyChangeListener listener) {
        observers.addPropertyChangeListener(listener);
    }

    public int addAppointment(Appointment appointment) {

        DAO<Appointment> dao = DAOFactory.getAppointmentDAO(connection);
        int generatedId = dao.add(appointment);

        if (generatedId > 0) {
            appointment.setId(generatedId);
            appointments.add(appointment);
            observers.firePropertyChange(APPOINTMENTS_CHANGE, null, appointment);
        }

        return generatedId;
    }

    public boolean deleteAppointment(int id) {

        DAO<Appointment> dao = DAOFactory.getAppointmentDAO(connection);

        Appointment removedAppointment = null;

        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getId() == id) {
                removedAppointment = appointments.remove(i);
                break;
            }
        }
        boolean operationResult = dao.delete(id);

        if (operationResult) {
            observers.firePropertyChange(APPOINTMENTS_CHANGE, null, appointments);
        } else {
            appointments.add(removedAppointment);
        }

        return operationResult;
    }

    public boolean updateAppointment(Appointment appointment) {
        DAO<Appointment> dao = DAOFactory.getAppointmentDAO(connection);

        boolean result = dao.update(appointment);

        if (result) {
            int index = appointments.indexOf(appointment);
            appointments.set(index, appointment);
            observers.firePropertyChange(APPOINTMENTS_CHANGE, null, appointments);
        }

        return result;
    }


    public int addDoctor(Doctor doctor) {
        DAO<Doctor> dao = DAOFactory.getDoctorDAO(connection);
        int generatedId = dao.add(doctor);

        if (generatedId > 0) {
            doctor.setId(generatedId);
            doctors.add(doctor);
            observers.firePropertyChange(DOCTORS_CHANGE, null, doctors);
        }

        return generatedId;
    }

    public boolean deleteDoctor(int id) {

        DAO<Doctor> dao = DAOFactory.getDoctorDAO(connection);

        Doctor removedDoctor = null;

        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getId() == id) {
                removedDoctor = doctors.remove(i);
                break;
            }
        }

        boolean operationResult = dao.delete(id);

        if (operationResult) {
            observers.firePropertyChange(DOCTORS_CHANGE, null, doctors);

        } else {
            doctors.add(removedDoctor);
        }

        return operationResult;
    }

    public boolean updateDoctor(Doctor doctor) {
        DAO<Doctor> dao = DAOFactory.getDoctorDAO(connection);

        boolean result = dao.update(doctor);

        if (result) {
            int index = doctors.indexOf(doctor);
            doctors.set(index, doctor);
            observers.firePropertyChange(DOCTORS_CHANGE, null, doctors);
        }

        return result;
    }

    public int addPatient(Patient patient) {
        DAO<Patient> dao = DAOFactory.getPatientDAO(connection);
        int generatedId = dao.add(patient);

        if (generatedId > 0) {
            patient.setId(generatedId);
            patients.add(patient);
            observers.firePropertyChange(PATIENTS_CHANGE, null, doctors);
        }

        return generatedId;
    }

    public boolean deletePatient(int id) {

        DAO<Patient> dao = DAOFactory.getPatientDAO(connection);

        Patient removedPatient = null;

        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getId() == id) {
                removedPatient = patients.remove(i);
                break;
            }
        }

        boolean operationResult = dao.delete(id);

        if (operationResult) {
            observers.firePropertyChange(PATIENTS_CHANGE, null, patients);
            appointments.removeIf(appointment -> appointment.getPatient().getId() == id);
            observers.firePropertyChange(APPOINTMENTS_CHANGE, null, appointments);
        } else {
            patients.add(removedPatient);
        }

        return operationResult;
    }

    public boolean updatePatient(Patient patient) {
        DAO<Patient> dao = DAOFactory.getPatientDAO(connection);

        boolean result = dao.update(patient);

        if (result) {
            int index = patients.indexOf(patient);
            patients.set(index, patient);
            observers.firePropertyChange(PATIENTS_CHANGE, null, patients);

            for (Appointment appointment : appointments) {
                if (appointment.getPatient().equals(patient)) {
                    appointment.setPatient(patient);
                }
            }

            observers.firePropertyChange(APPOINTMENTS_CHANGE, null, appointments);

        }

        return result;
    }

    public void loadInitialData() {

        DAO<Patient> patientDAO = DAOFactory.getPatientDAO(connection);
        DAO<Doctor> doctorDAO = DAOFactory.getDoctorDAO(connection);
        DAO<Appointment> appointmentDAO = DAOFactory.getAppointmentDAO(connection);


        patients = patientDAO.getAll();
        doctors = doctorDAO.getAll();
        appointments = appointmentDAO.getAll();

        observers.firePropertyChange(INITIAL_DATA_LOADED, null, this);


    }

    // Returns the Year of the oldest appointment
    public int getFirstYear() {
        int oldestYear = Integer.MIN_VALUE;
        for (Appointment appointment : appointments) {
            if (appointment.getDate().getYear() > oldestYear) {
                oldestYear = appointment.getDate().getYear();
            }
        }
        return oldestYear;
    }

    // Returns the Year of the most recent appointment
    public int getLastYear() {
        int mostRecentYear = Integer.MAX_VALUE;
        for (Appointment appointment : appointments) {
            if (appointment.getDate().getYear() < mostRecentYear) {
                mostRecentYear = appointment.getDate().getYear();
            }
        }
        return mostRecentYear;
    }

    private static class SingletonHelper {
        private static DataSource instance = new DataSource();
    }

    public static DataSource getInstance() {
        return SingletonHelper.instance;
    }

    public ObservableList<Appointment> getAppointments() {
        return appointments;
    }

    public ObservableList<Patient> getPatients() {
        return patients;
    }

    public ObservableList<Doctor> getDoctors() {
        return doctors;
    }
}
