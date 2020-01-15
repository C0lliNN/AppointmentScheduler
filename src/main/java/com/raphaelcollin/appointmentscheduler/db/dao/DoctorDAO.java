package com.raphaelcollin.appointmentscheduler.db.dao;

import com.raphaelcollin.appointmentscheduler.db.model.Doctor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DoctorDAO extends DAO<Doctor>{

    private static final String TABLE_NAME = "Doctor";
    private static final String COLUMN_ID = "idDoctor";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_BIRTH_DATE = "birthDate";
    private static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    private static final String COLUMN_LICENSE_NUMBER = "licenseNumber";

    public DoctorDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int add(Doctor item) {

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(TABLE_NAME);
        queryBuilder.append("(");
        queryBuilder.append(COLUMN_NAME).append(", ");
        queryBuilder.append(COLUMN_GENDER).append(", ");
        queryBuilder.append(COLUMN_BIRTH_DATE).append(", ");
        queryBuilder.append(COLUMN_PHONE_NUMBER).append(", ");
        queryBuilder.append(COLUMN_LICENSE_NUMBER);
        queryBuilder.append(")");
        queryBuilder.append(" VALUES ");
        queryBuilder.append("(?, ?, ?, ?, ?)");

        int generatedKey;

        try (PreparedStatement statement  = connection.prepareStatement(queryBuilder.toString(), PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, item.getName());
            statement.setString(2, item.getGender());
            statement.setDate(3, Date.valueOf(item.getBirthDate()));
            statement.setString(4, item.getPhoneNumber());
            statement.setString(5, item.getLicenseNumber());

            if (statement.executeUpdate() > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.next();
                generatedKey = generatedKeys.getInt(1);
                generatedKeys.close();
            } else {
                generatedKey = -1;
            }

        } catch (SQLException e) {
            System.err.println("Exception in DoctorDAO - add - " + e.getMessage());
            generatedKey = -1;
        }

        return generatedKey;

    }

    @Override
    public ObservableList<Doctor> getAll() {

        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        queryBuilder.append(COLUMN_ID).append(", ");
        queryBuilder.append(COLUMN_NAME).append(", ");
        queryBuilder.append(COLUMN_GENDER).append(", ");
        queryBuilder.append(COLUMN_BIRTH_DATE).append(", ");
        queryBuilder.append(COLUMN_PHONE_NUMBER).append(", ");
        queryBuilder.append(COLUMN_LICENSE_NUMBER);
        queryBuilder.append(" FROM ");
        queryBuilder.append(TABLE_NAME);

        ObservableList<Doctor> doctors = null;

        try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {

            ResultSet resultSet = statement.executeQuery();

            doctors = FXCollections.observableArrayList();

            while (resultSet.next()) {
                doctors.add(new Doctor.Builder().
                        setId(resultSet.getInt(1)).
                        setName(resultSet.getString(2)).
                        setGender(resultSet.getString(3)).
                        setBirthDate(resultSet.getDate(4).toLocalDate()).
                        setPhoneNumber(resultSet.getString(5)).
                        setLicenseNumber(resultSet.getString(6)).
                        build());
            }

        } catch (SQLException e) {
            System.err.println("Exception in DoctorDAO - getAll - " + e.getMessage());
        }

        return doctors;
    }

    @Override
    public boolean update(Doctor item) {

        StringBuilder queryBuilder = new StringBuilder("UPDATE ");
        queryBuilder.append(TABLE_NAME);
        queryBuilder.append(" SET ");
        queryBuilder.append(COLUMN_NAME).append(" = ?, ");
        queryBuilder.append(COLUMN_GENDER).append(" = ?, ");
        queryBuilder.append(COLUMN_BIRTH_DATE).append(" = ?, ");
        queryBuilder.append(COLUMN_PHONE_NUMBER).append(" = ?, ");
        queryBuilder.append(COLUMN_LICENSE_NUMBER).append(" = ? ");
        queryBuilder.append(" WHERE ");
        queryBuilder.append(COLUMN_ID).append(" = ?");

        boolean updated;

        try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {

            statement.setString(1, item.getName());
            statement.setString(2, item.getGender());
            statement.setDate(3, Date.valueOf(item.getBirthDate()));
            statement.setString(4, item.getPhoneNumber());
            statement.setString(5, item.getLicenseNumber());
            statement.setInt(6, item.getId());

            updated = statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Exception in DoctorDAO - update - " + e.getMessage());
            updated = false;
        }

        return updated;
    }

    @Override
    public boolean delete(int id) {

        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";

        boolean deleted;

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            deleted = statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Exception in DoctorDAO - delete - " + e.getMessage());
            deleted = false;
        }

        return deleted;
    }
}
