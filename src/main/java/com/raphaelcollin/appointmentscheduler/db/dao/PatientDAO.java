package com.raphaelcollin.appointmentscheduler.db.dao;

import com.raphaelcollin.appointmentscheduler.db.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class PatientDAO extends DAO<Patient> {

    private static final String TABLE_NAME = "Patient";
    private static final String COLUMN_ID = "idPatient";
    private static final String COLUMN_FIRST_NAME = "firstName";
    private static final String COLUMN_LAST_NAME = "lastName";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_BIRTH_DATE = "birthDate";
    private static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_ZIP_CODE = "zipCode";
    private static final String COLUMN_STREET_NAME = "streetName";
    private static final String COLUMN_HOUSE_NUMBER = "houseNumber";

    public PatientDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int add(Patient item) {

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(TABLE_NAME).append(" ");
        queryBuilder.append("(");
        queryBuilder.append(COLUMN_FIRST_NAME).append(",");
        queryBuilder.append(COLUMN_LAST_NAME).append(",");
        queryBuilder.append(COLUMN_GENDER).append(",");
        queryBuilder.append(COLUMN_BIRTH_DATE).append(",");
        queryBuilder.append(COLUMN_PHONE_NUMBER).append(",");
        queryBuilder.append(COLUMN_EMAIL).append(",");
        queryBuilder.append(COLUMN_CITY).append(",");
        queryBuilder.append(COLUMN_ZIP_CODE).append(",");
        queryBuilder.append(COLUMN_STREET_NAME).append(",");
        queryBuilder.append(COLUMN_HOUSE_NUMBER);
        queryBuilder.append(")");
        queryBuilder.append(" VALUES ");
        queryBuilder.append("(?, ?, ?, ?, ?, ?, ?, ?,?,?)");

        int generatedKey;

        try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString(), PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, item.getFirstName());
            statement.setString(2, item.getLastName());
            statement.setString(3, item.getGender());
            statement.setDate(4, Date.valueOf(item.getBirthDate()));
            statement.setString(5, item.getPhoneNumber());
            statement.setString(6, item.getEmail());
            statement.setString(7, item.getCity());
            statement.setString(8, item.getZipCode());
            statement.setString(9, item.getStreetNumber());
            statement.setString(10, item.getHouseNumber());

            if (statement.executeUpdate() > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.next();
                generatedKey = generatedKeys.getInt(1);
                generatedKeys.close();
            } else {
                generatedKey = -1;
            }


        } catch (SQLException e) {
            System.err.println("Exception in PatientDAO - Add" + e.getMessage());
            generatedKey = -1;
        }

        return generatedKey;
    }

    @Override
    public ObservableList<Patient> getAll() {

        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        queryBuilder.append(COLUMN_ID).append(", ");
        queryBuilder.append(COLUMN_FIRST_NAME).append(", ");
        queryBuilder.append(COLUMN_LAST_NAME).append(", ");
        queryBuilder.append(COLUMN_GENDER).append(", ");
        queryBuilder.append(COLUMN_BIRTH_DATE).append(", ");
        queryBuilder.append(COLUMN_PHONE_NUMBER).append(", ");
        queryBuilder.append(COLUMN_EMAIL).append(", ");
        queryBuilder.append(COLUMN_CITY).append(", ");
        queryBuilder.append(COLUMN_ZIP_CODE).append(", ");
        queryBuilder.append(COLUMN_STREET_NAME).append(", ");
        queryBuilder.append(COLUMN_HOUSE_NUMBER);
        queryBuilder.append(" FROM ");
        queryBuilder.append(TABLE_NAME);

        try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {

            ResultSet resultSet = statement.executeQuery();

            ObservableList<Patient> patients = FXCollections.observableArrayList();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                String gender = resultSet.getString(4);
                LocalDate birthDate = resultSet.getDate(5).toLocalDate();
                String phoneNumber = resultSet.getString(6);
                String email = resultSet.getString(7);
                String city = resultSet.getString(8);
                String zipCode = resultSet.getString(9);
                String streetNumber = resultSet.getString(10);
                String houseNumber = resultSet.getString(11);

                patients.add(new Patient.Builder().
                        setId(id).
                        setFirstName(firstName).
                        setLastName(lastName).
                        setGender(gender).
                        setBirthDate(birthDate).
                        setPhoneNumber(phoneNumber).
                        setEmail(email).
                        setCity(city).
                        setZipCode(zipCode).
                        setStreetNumber(streetNumber).
                        setHouseNumber(houseNumber).
                        build()
                );

            }

            return patients;

        } catch (SQLException e) {
            System.err.println("Exception in PatientDAO - getAll" + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean update(Patient item) {

        StringBuilder queryBuilder = new StringBuilder("UPDATE ");
        queryBuilder.append(TABLE_NAME).append(" SET ");
        queryBuilder.append(COLUMN_FIRST_NAME).append(" = ").append("?").append(", ");
        queryBuilder.append(COLUMN_LAST_NAME).append(" = ").append("?").append(", ");
        queryBuilder.append(COLUMN_GENDER).append(" = ").append("?").append(", ");
        queryBuilder.append(COLUMN_BIRTH_DATE).append(" = ").append("?").append(", ");
        queryBuilder.append(COLUMN_PHONE_NUMBER).append(" = ").append("?").append(", ");
        queryBuilder.append(COLUMN_EMAIL).append(" = ").append("?").append(", ");
        queryBuilder.append(COLUMN_CITY).append(" = ").append("?").append(", ");
        queryBuilder.append(COLUMN_ZIP_CODE).append(" = ").append("?").append(", ");
        queryBuilder.append(COLUMN_STREET_NAME).append(" = ").append("?").append(", ");
        queryBuilder.append(COLUMN_HOUSE_NUMBER).append(" = ").append("?");
        queryBuilder.append(" WHERE ");
        queryBuilder.append(COLUMN_ID).append(" = ").append("?");

        try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {

            statement.setString(1, item.getFirstName());
            statement.setString(2, item.getLastName());
            statement.setString(3, item.getGender());
            statement.setDate(4, Date.valueOf(item.getBirthDate()));
            statement.setString(5, item.getPhoneNumber());
            statement.setString(6, item.getEmail());
            statement.setString(7, item.getCity());
            statement.setString(8, item.getZipCode());
            statement.setString(9, item.getStreetNumber());
            statement.setString(10, item.getHouseNumber());
            statement.setInt(11, item.getId());

            return statement.executeUpdate() > 0;


        } catch (SQLException e) {
            System.err.println("Exception in PatientDAO - Update" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {

        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ? ";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Exception in PatientDAO - Delete" + e.getMessage());
            return false;
        }

    }
}
