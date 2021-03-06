package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.*;
import com.raphaelcollin.appointmentscheduler.db.DataSource;
import com.raphaelcollin.appointmentscheduler.db.model.Appointment;
import com.raphaelcollin.appointmentscheduler.db.model.ComboBoxItemHelper;
import com.raphaelcollin.appointmentscheduler.db.model.Doctor;
import com.raphaelcollin.appointmentscheduler.db.model.Patient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class AppointmentFieldsController implements Initializable {

    @FXML
    private GridPane root;
    @FXML
    private JFXDatePicker dateField;

    @FXML
    private JFXTimePicker timeField;

    @FXML
    private JFXComboBox<Patient> patientField;

    @FXML
    private JFXComboBox<Doctor> doctorField;

    @FXML
    private JFXTextField priceField;

    @FXML
    private JFXTextArea descriptionField;

    private static final String REGEX_PRICE = "^\\d+([.,]\\d+)?$";

    private Appointment appointment;
    private JFXComboBox<ComboBoxItemHelper> statusField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setVgap(25);
        root.setHgap(20);
        root.setPadding(new Insets(10,10,0,10));

        for (int i = 0; i < root.getChildren().size(); i = i + 2) {
            ((Label) root.getChildren().get(i)).setFont(Font.font(20));
        }

        dateField.setDefaultColor(Color.valueOf("#085394"));
        timeField.setDefaultColor(Color.valueOf("#085394"));

        if (getResources().getString(BUNDLE_KEY_TIME_FORMAT).contains("HH")) {
            timeField.set24HourView(true);
        }

        patientField.setPrefSize(230, 25);
        patientField.setItems(DataSource.getInstance().getPatients());
        patientField.setCellFactory(param -> new ListCell<Patient>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item.getFirstName() + " " + item.getLastName());
                }
            }
        });
        patientField.setButtonCell(patientField.getCellFactory().call(null));
        patientField.getSelectionModel().selectFirst();

        doctorField.setPrefSize(230, 25);
        doctorField.setItems(DataSource.getInstance().getDoctors());
        doctorField.setCellFactory(param -> new ListCell<Doctor>() {
            @Override
            protected void updateItem(Doctor item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        doctorField.setButtonCell(doctorField.getCellFactory().call(null));
        doctorField.getSelectionModel().selectFirst();

    }

    public void setAppointment(Appointment appointment) {

        dateField.setValue(appointment.getDate().toLocalDate());
        timeField.setValue(appointment.getDate().toLocalTime());
        patientField.getSelectionModel().select(appointment.getPatient());
        doctorField.getSelectionModel().select(appointment.getDoctor());
        priceField.setText(appointment.getPrice() + "");
        descriptionField.setText(appointment.getDescription());

        Label statusLabel = new Label(getResources().getString(BUNDLE_KEY_STATUS));
        statusLabel.setFont(Font.font(20));

        statusField = new JFXComboBox<>();
        statusField.setPrefSize(230, 25);
        statusField.setItems(statusList);

        statusField.setCellFactory(new Callback<ListView<ComboBoxItemHelper>, ListCell<ComboBoxItemHelper>>() {
            @Override
            public ListCell<ComboBoxItemHelper> call(ListView<ComboBoxItemHelper> param) {
                return new ListCell<ComboBoxItemHelper>(){
                    @Override
                    protected void updateItem(ComboBoxItemHelper item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item != null && !empty) {
                            setText(getResources().getString(item.getBundleKey()));
                        }
                    }
                };
            }
        });

        statusField.setButtonCell(statusField.getCellFactory().call(null));

        int index = -1;

        for (int i = 0; i < statusList.size(); i++) {
            if (statusList.get(i).getDbName().equals(appointment.getStatus())) {
                index = i;
                break;
            }
        }

        statusField.getSelectionModel().select(index);

        for (int i = 4; i < root.getChildren().size(); i++) {
            int rowIndex = GridPane.getRowIndex(root.getChildren().get(i));
            GridPane.setRowIndex(root.getChildren().get(i), rowIndex + 1);
        }

        root.addRow(3, statusLabel, statusField);

        this.appointment = appointment;
    }

    public void clear() {
        dateField.setValue(null);
        timeField.setValue(null);
        patientField.getSelectionModel().selectFirst();
        doctorField.getSelectionModel().selectFirst();
        priceField.clear();
        descriptionField.clear();
    }

    public Appointment getAppointment() {

        boolean errorFounded = false;
        String errorMessage = "";

        if (dateField.getValue() == null || timeField.getValue() == null || priceField.getText().trim().isEmpty()) {
            errorFounded = true;
            errorMessage = getResources().getString(BUNDLE_KEY_ERROR_EMPTY_MESSAGE);
        }

        if (!errorFounded && !priceField.getText().matches(REGEX_PRICE)){
            errorFounded = true;
            errorMessage = getResources().getString(BUNDLE_KEY_ERROR_INVALID_PRICE);
        }

        Appointment appointment = null;

        if (errorFounded) {
            showAlert(Alert.AlertType.ERROR, root,
                    getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                    getResources().getString(BUNDLE_KEY_ERROR_INVALID_INPUT),
                    errorMessage);
        } else {

            Appointment.Builder builder = new Appointment.Builder();

            if (this.appointment != null) {
                builder.setId(this.appointment.getId());
            }

            if (this.statusField != null) {

                builder.setStatus(statusField.getSelectionModel().getSelectedItem().getDbName());
            } else {

                builder.setStatus(UNCONFIRMED);
            }

            appointment = builder
                    .setDate(LocalDateTime.of(dateField.getValue(), timeField.getValue()))
                    .setPatient(patientField.getSelectionModel().getSelectedItem())
                    .setDoctor(doctorField.getSelectionModel().getSelectedItem())
                    .setPrice(Double.parseDouble(priceField.getText()))
                    .setDescription(descriptionField.getText())
                    .build();

        }

        return appointment;
    }
}
