package com.raphaelcollin.appointmentscheduler.db.dao;

import java.sql.Connection;

public class DAOFactory {

    public static PatientDAO getPatientDAO(Connection connection) {
        return new PatientDAO(connection);
    }
    public static DoctorDAO getDoctorDAO(Connection connection) {
        return new DoctorDAO(connection);
    }
    public static AppointmentDAO getAppointmentDAO(Connection connection) {
        return new AppointmentDAO(connection);
    }

}
