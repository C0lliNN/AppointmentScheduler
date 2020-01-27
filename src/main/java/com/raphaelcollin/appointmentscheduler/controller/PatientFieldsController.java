package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.raphaelcollin.appointmentscheduler.db.model.ComboBoxItemHelper;
import com.raphaelcollin.appointmentscheduler.db.model.Patient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class PatientFieldsController implements Initializable {
    @FXML
    private GridPane root;
    @FXML
    private JFXTextField firstNameField;
    @FXML
    private JFXTextField lastNameField;
    @FXML
    private JFXComboBox<ComboBoxItemHelper> genderField;
    @FXML
    private JFXDatePicker dateField;
    @FXML
    private JFXTextField phoneField;
    @FXML
    private JFXTextField emailField;
    @FXML
    private JFXTextField cityField;
    @FXML
    private JFXTextField streetField;
    @FXML
    private JFXTextField zipCodeField;
    @FXML
    private JFXTextField houseField;

    private Patient patient;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setVgap(25);
        root.setHgap(20);
        root.setPadding(new Insets(10,10,0,10));

        genderField.setPrefSize(134, 25);
        genderField.setItems(genderList);
        genderField.setCellFactory(new Callback<ListView<ComboBoxItemHelper>, ListCell<ComboBoxItemHelper>>() {
            @Override
            public ListCell<ComboBoxItemHelper> call(ListView<ComboBoxItemHelper> param) {
                return new ListCell<ComboBoxItemHelper>() {
                    @Override
                    protected void updateItem(ComboBoxItemHelper item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item != null && !empty) {
                            setText(getResources().getString(item.getBundleKey()));
                        } else {
                            setGraphic(null);
                        }

                    }
                };
            }
        });

        genderField.setButtonCell(genderField.getCellFactory().call(null));

        genderField.getSelectionModel().selectFirst();

        dateField.setDefaultColor(Color.valueOf("#085394"));

        for (Node node : root.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setFont(Font.font(18));
            }
        }
    }

    public Patient getPatient() {

        Patient newPatient = null;

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String gender = genderField.getSelectionModel().getSelectedItem().getDbName();
        LocalDate birthDate = dateField.getValue();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String city = cityField.getText().trim();
        String street = streetField.getText().trim();
        String zipCode = zipCodeField.getText().trim();
        String house = houseField.getText().trim();

        boolean errorFounded = false;
        String errorMessage = "";

        if (firstName.isEmpty() || lastName.isEmpty() || birthDate == null || phone.isEmpty()) {
            errorMessage = getResources().getString(BUNDLE_KEY_ERROR_EMPTY_MESSAGE);
            errorFounded = true;
        }

        if (!errorFounded && !email.isEmpty() && !email.matches(EMAIL_REGEX)) {
            errorMessage = getResources().getString(BUNDLE_KEY_INVALID_EMAIL);
            errorFounded = true;
        }

        if (errorFounded) {
            showAlert(Alert.AlertType.ERROR, root,
                    getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                    getResources().getString(BUNDLE_KEY_ERROR_INVALID_INPUT),
                    errorMessage);
        } else {

            newPatient = new Patient.Builder()
                    .setId(patient == null ? -1 : patient.getId())
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setGender(gender)
                    .setBirthDate(birthDate)
                    .setPhoneNumber(phone)
                    .setEmail(email)
                    .setCity(city)
                    .setStreetName(street)
                    .setZipCode(zipCode)
                    .setHouseNumber(house)
                    .build();

        }

        return newPatient;

    }

    public void setPatient(Patient patient) {
        firstNameField.setText(patient.getFirstName());
        lastNameField.setText(patient.getLastName());

        int index = -1;

        for (ComboBoxItemHelper gender : genderList) {
            if (gender.getDbName().equals(patient.getGender())) {
                index = gender.getComboBoxIndex();
                break;
            }
        }

        if (index > 0) {
            genderField.getSelectionModel().select(index);
        }

        dateField.setValue(patient.getBirthDate());
        phoneField.setText(patient.getPhoneNumber() == null ? "" : patient.getPhoneNumber());
        emailField.setText(patient.getEmail() == null ? "" : patient.getEmail());
        zipCodeField.setText(patient.getZipCode() == null ? "" : patient.getZipCode());
        cityField.setText(patient.getCity() == null ? "" : patient.getCity());
        streetField.setText(patient.getStreetName() == null ? "" : patient.getStreetName());
        houseField.setText(patient.getHouseNumber() == null ? "" : patient.getHouseNumber());

        this.patient = patient;

    }

    public void clear() {
        firstNameField.clear();
        lastNameField.clear();
        genderField.getSelectionModel().selectFirst();
        dateField.setValue(null);
        phoneField.clear();
        emailField.clear();
        emailField.clear();
        cityField.clear();
        streetField.clear();
        zipCodeField.clear();
        houseField.clear();
    }
}
