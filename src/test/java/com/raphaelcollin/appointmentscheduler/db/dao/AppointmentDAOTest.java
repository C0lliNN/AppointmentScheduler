package com.raphaelcollin.appointmentscheduler.db.dao;

import com.raphaelcollin.appointmentscheduler.DatabaseCredentials;
import com.raphaelcollin.appointmentscheduler.db.ConnectionFactory;
import com.raphaelcollin.appointmentscheduler.db.model.Appointment;
import com.raphaelcollin.appointmentscheduler.db.model.Doctor;
import com.raphaelcollin.appointmentscheduler.db.model.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class AppointmentDAOTest {

    @ParameterizedTest
    @CsvSource({"2020-01-15 13:40, Test, Unconfirmed, 100.0, 1, 1",
            "2020-02-08 16:00, Test2, Completed, 80.0, 1, 1",
            "2020-01-15 17:20, Test3, Confirmed, 120.0, 1, 1",
            "2020-02-01 08:30, Test4, Canceled, 200.0, 1, 1"})
    void testAppointDAO(ArgumentsAccessor accessor) {

        DatabaseCredentials credentials = new DatabaseCredentials("127.0.0.1", "3307", "root", "");
        Connection connection = ConnectionFactory.getConnection(credentials);

        DAO<Appointment> dao = DAOFactory.getAppointmentDAO(connection);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Appointment appointment = new Appointment.Builder().
                setDate(LocalDateTime.parse(accessor.getString(0), formatter)).
                setDescription(accessor.getString(1)).
                setStatus(accessor.getString(2)).
                setPrice(accessor.getDouble(3)).
                setDoctor(new Doctor.Builder().
                        setId(accessor.getInteger(4)).
                        build()).
                setPatient(new Patient.Builder().
                        setId(accessor.getInteger(5)).
                        build())
                .build();

        int generatedKey = dao.add(appointment);

        Assertions.assertTrue(generatedKey > 0);

        Assertions.assertNotNull(dao.getAll());

        appointment.setId(generatedKey);
        appointment.setPrice(appointment.getPrice() + 20.0);

        Assertions.assertTrue(dao.update(appointment));

        Assertions.assertTrue(dao.delete(appointment.getId()));
    }

}