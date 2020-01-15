package com.raphaelcollin.appointmentscheduler.db.dao;

import com.raphaelcollin.appointmentscheduler.DatabaseCredentials;
import com.raphaelcollin.appointmentscheduler.db.ConnectionFactory;
import com.raphaelcollin.appointmentscheduler.db.model.Patient;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Connection;
import java.time.LocalDate;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PatientDAOTest {

    private DatabaseCredentials credentials = new DatabaseCredentials("127.0.0.1", "3307", "root", "");

    @ParameterizedTest
    @CsvSource({"Mathew, Jefferson, Male, 1999, 1, 14, 532-341293, null, null, null, null, null",
                "Glen, Peterson, Male, 1982, 11, 26, 193-532424, test@gmail.com, New York, null, null, null",
                "Peter, Park, Male, 2000, 3, 15, 934-3123, test@gmail.com, Paris, 235142, Street John Jefferson, 43",
                "Donald, Smith, Male, 1993, 2, 13, 431-532425, null, null, null, null, 53",
                "Mary, Williams, Female, 1984, 5, 4, 412-53134, null, null, 53242342, null, null"})
    void testPatientDAO(ArgumentsAccessor accessor) {

        Connection connection = ConnectionFactory.getConnection(credentials);
        DAO<Patient> dao = DAOFactory.getPatientDAO(connection);

        ObservableList<Patient> patients = dao.getAll();

        Assertions.assertNotNull(patients);

        Patient patient = new Patient.Builder()
                .setFirstName(accessor.getString(0))
                .setLastName(accessor.getString(1))
                .setGender(accessor.getString(2))
                .setBirthDate(LocalDate.of(accessor.getInteger(3),accessor.getInteger(4),accessor.getInteger(5)))
                .setPhoneNumber(accessor.getString(6))
                .setEmail(accessor.getString(7))
                .setCity(accessor.getString(8))
                .setZipCode(accessor.getString(9))
                .setStreetName(accessor.getString(10))
                .setHouseNumber(accessor.getString(11))
                .build();


        int generatedKey = dao.add(patient);
        Assertions.assertTrue(generatedKey >= 0);

        patient.setId(generatedKey);
        patient.setFirstName(patient.getFirstName() + " Updated");

        Assertions.assertTrue(dao.update(patient));

        Assertions.assertTrue(dao.delete(patient.getId()));

        ConnectionFactory.closeConnection(connection);

    }

}