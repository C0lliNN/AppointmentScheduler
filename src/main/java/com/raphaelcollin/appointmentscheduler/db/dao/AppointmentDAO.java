package com.raphaelcollin.appointmentscheduler.db.dao;

import com.raphaelcollin.appointmentscheduler.db.model.Appointment;
import com.raphaelcollin.appointmentscheduler.db.model.Doctor;
import com.raphaelcollin.appointmentscheduler.db.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AppointmentDAO extends DAO<Appointment> {

    private static final String TABLE_NAME = "Appointment";
    private static final String COLUMN_ID = "idAppointment";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_PRICE = "Price";
    private static final String COLUMN_PATIENT_ID = "id_patient";
    private static final String COLUMN_DOCTOR_ID = "id_doctor";
    private static final String VIEW_DATA = "appointment_data";

    public AppointmentDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int add(Appointment item) {

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(TABLE_NAME);
        queryBuilder.append("(");
        queryBuilder.append(COLUMN_DATE).append(", ");
        queryBuilder.append(COLUMN_DESCRIPTION).append(", ");
        queryBuilder.append(COLUMN_STATUS).append(", ");
        queryBuilder.append(COLUMN_PRICE).append(", ");
        queryBuilder.append(COLUMN_PATIENT_ID).append(", ");
        queryBuilder.append(COLUMN_DOCTOR_ID);
        queryBuilder.append(")");
        queryBuilder.append(" VALUES ");
        queryBuilder.append("(?, ?, ?, ?, ?, ?)");

        int generatedKey;

        try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString(), PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setTimestamp(1, Timestamp.valueOf(item.getDate()));
            statement.setString(2, item.getDescription());
            statement.setString(3, item.getStatus());
            statement.setDouble(4, item.getPrice());
            statement.setInt(5, item.getPatient().getId());
            statement.setInt(6, item.getDoctor().getId());

            if (statement.executeUpdate() > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.next();
                generatedKey = generatedKeys.getInt(1);
                generatedKeys.close();
            } else {
                generatedKey = -1;
            }

        } catch (SQLException e) {
            System.err.println("Exception in AppointmentDAO - add - " + e.getMessage());
            generatedKey = -1;
        }

        return generatedKey;
    }

    @Override
    public ObservableList<Appointment> getAll() {

        String query = "SELECT * FROM " + VIEW_DATA;

        ObservableList<Appointment> appointments = null;

        try(PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            appointments = FXCollections.observableArrayList();

            while (resultSet.next()) {

                appointments.add(new Appointment.Builder().
                        setId(resultSet.getInt(1)).
                        setDate(resultSet.getTimestamp(2).toLocalDateTime()).
                        setDescription(resultSet.getString(3)).
                        setStatus(resultSet.getString(4)).
                        setPrice(resultSet.getDouble(5)).
                        setPatient(new Patient.Builder().
                                setId(resultSet.getInt(6)).
                                setFirstName(resultSet.getString(7)).
                                setLastName(resultSet.getString(8)).
                                setGender(resultSet.getString(9)).
                                setBirthDate(resultSet.getDate(10).toLocalDate()).
                                setPhoneNumber(resultSet.getString(11)).
                                setEmail(resultSet.getString(12)).
                                setCity(resultSet.getString(13)).
                                setZipCode(resultSet.getString(14)).
                                setStreetName(resultSet.getString(15)).
                                setHouseNumber(resultSet.getString(16)).
                                build()).
                        setDoctor(new Doctor.Builder().
                                setId(resultSet.getInt(17)).
                                setName(resultSet.getString(18)).
                                setPhoneNumber(resultSet.getString(19)).
                                setGender(resultSet.getString(20)).
                                setBirthDate(resultSet.getDate(21).toLocalDate()).
                                setLicenseNumber(resultSet.getString(22)).
                                build()).
                        build());
            }

        } catch (SQLException e) {
            System.err.println("Exception in AppointmentDAO - getAll - " + e.getMessage());
        }

        return appointments;
    }

    @Override
    public boolean update(Appointment item) {

        StringBuilder queryBuilder = new StringBuilder("UPDATE ");
        queryBuilder.append(TABLE_NAME);
        queryBuilder.append(" SET ");
        queryBuilder.append(COLUMN_DATE).append(" = ?, ");
        queryBuilder.append(COLUMN_DESCRIPTION).append(" = ?, ");
        queryBuilder.append(COLUMN_STATUS).append(" = ?, ");
        queryBuilder.append(COLUMN_PRICE).append(" = ?");
        queryBuilder.append(" WHERE ");
        queryBuilder.append(COLUMN_ID).append(" = ? ");

        boolean updated;

        try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {

            statement.setTimestamp(1, Timestamp.valueOf(item.getDate()));
            statement.setString(2, item.getDescription());
            statement.setString(3, item.getStatus());
            statement.setDouble(4, item.getPrice());
            statement.setInt(5, item.getId());

            updated = statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Exception in AppointmentDAO - update - " + e.getMessage());
            updated = false;
        }

        return updated;
    }

    @Override
    public boolean delete(int id) {

        String query = "DELETE FROM " + TABLE_NAME  + " WHERE " + COLUMN_ID + " = ?";

        boolean deleted;

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            deleted = statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Exception in AppointmentDAO - delete - " + e.getMessage());
            deleted = false;
        }

        return deleted;
    }
}
