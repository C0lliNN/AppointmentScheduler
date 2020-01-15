package com.raphaelcollin.appointmentscheduler.db.dao;

import com.raphaelcollin.appointmentscheduler.DatabaseCredentials;
import com.raphaelcollin.appointmentscheduler.db.ConnectionFactory;
import com.raphaelcollin.appointmentscheduler.db.model.Doctor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Connection;
import java.time.LocalDate;

class DoctorDAOTest {

    @ParameterizedTest
    @CsvSource({"John, Male, 1990, 2,5, 642234135, 32424DS",
            "Mathew, Male, 1988, 10,20, 89752352, null",
            "Mary, Female, 1995, 1,13, 21567595, 235336DS",
            "Bruce, Male, 1979, 8,1, 62782423, 6432359AB",})
    void testDoctorDAO(ArgumentsAccessor accessor) {

        DatabaseCredentials credentials = new DatabaseCredentials("127.0.0.1", "3307", "root", "");

        Connection connection = ConnectionFactory.getConnection(credentials);

        DAO<Doctor> dao = DAOFactory.getDoctorDAO(connection);

        Doctor doctor = new Doctor.Builder().
                setName(accessor.getString(0)).
                setGender(accessor.getString(1)).
                setBirthDate(LocalDate.of(accessor.getInteger(2), accessor.getInteger(3), accessor.getInteger(4))).
                setPhoneNumber(accessor.getString(5)).
                setLicenseNumber(accessor.getString(6)).
                build();

        int generatedKey  = dao.add(doctor);

        Assertions.assertTrue(generatedKey >= 0);
        Assertions.assertNotNull(dao.getAll());

        doctor.setName(doctor.getName() + " Updated");
        doctor.setId(generatedKey);

        Assertions.assertTrue(dao.update(doctor));

        Assertions.assertTrue(dao.delete(generatedKey));


    }

}