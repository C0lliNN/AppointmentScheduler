package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.*;
import com.raphaelcollin.appointmentscheduler.db.DataSource;
import com.raphaelcollin.appointmentscheduler.db.model.Appointment;
import com.raphaelcollin.appointmentscheduler.db.model.ComboBoxItemHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class AppointmentDetailsController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXTabPane tabPane;

    @FXML
    private Tab appointmentTab;

    @FXML
    private JFXDatePicker appointmentDateField;

    @FXML
    private JFXTimePicker appointmentTimeField;

    @FXML
    private JFXTextField appointmentStatusField;

    @FXML
    private JFXTextField appointmentPriceField;

    @FXML
    private JFXTextArea appointmentDescriptionField;

    @FXML
    private Tab patientTab;

    @FXML
    private JFXTextField patientNameField;

    @FXML
    private JFXTextField patientGenderField;

    @FXML
    private JFXDatePicker patientBirthDateField;

    @FXML
    private JFXTextField patientPhoneField;

    @FXML
    private JFXTextField patientEmailField;

    @FXML
    private JFXTextField patientAddressField;

    @FXML
    private JFXTextField patientAppointmentsField;

    @FXML
    private JFXTextField patientAmountField;

    @FXML
    private Tab doctorTab;

    @FXML
    private JFXTextField doctorNameField;

    @FXML
    private JFXTextField doctorGenderField;

    @FXML
    private JFXDatePicker doctorBirthDateField;

    @FXML
    private JFXTextField doctorPhoneField;

    @FXML
    private JFXTextField doctorLicenseNumberField;

    @FXML
    private JFXTextField doctorAppointmentsField;

    @FXML
    private JFXTextField doctorAmountField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        double width = 600;
        double height = 600;

        root.setMinSize(width, height);
        root.setPrefSize(width, height);
        root.setMaxSize(width, height);

        tabPane.setTabMaxWidth(196);
        tabPane.setTabMinWidth(196);

        for (Tab tab : tabPane.getTabs()) {

            AnchorPane content = ((AnchorPane) tab.getContent());

            Label titleLabel = ((Label) content.getChildren().get(0));
            AnchorPane.setTopAnchor(titleLabel, 20.0);
            titleLabel.setFont(Font.font(32));

            GridPane gridPane = ((GridPane) content.getChildren().get(1));
            gridPane.setHgap(20);
            gridPane.setVgap(25);

            AnchorPane.setTopAnchor(gridPane, 80.0);

            for (int i = 0; i < gridPane.getChildren().size(); i++) {

                Node node = gridPane.getChildren().get(i);

                if (node instanceof Label) {
                    ((Label) node).setFont(Font.font(18));
                }

                if (node instanceof JFXTextField) {
                    ((JFXTextField) node).setEditable(false);
                }

                if (node instanceof JFXTextArea) {
                    ((JFXTextArea) node).setEditable(false);
                }

                if (node instanceof JFXDatePicker) {
                    ((JFXDatePicker) node).setEditable(false);
                    ((JFXDatePicker) node).setDefaultColor(Color.valueOf("#085394"));
                }

                if (node instanceof JFXTimePicker) {
                    ((JFXTimePicker) node).setEditable(false);
                    ((JFXTimePicker) node).setDefaultColor(Color.valueOf("#085394"));
                }

            }
        }


        appointmentStatusField.setPrefSize(230, 25);
        patientGenderField.setPrefSize(230, 25);
        doctorGenderField.setPrefSize(230, 25);

    }

    public void setAppointment(Appointment appointment) {
        appointmentDateField.setValue(appointment.getDate().toLocalDate());

        String status = "";

        for (ComboBoxItemHelper statusItem : statusList) {
            if (statusItem.getDbName().equals(appointment.getStatus())) {
                status = getResources().getString(statusItem.getBundleKey());
                break;
            }
        }

        appointmentStatusField.setText(status);
        appointmentPriceField.setText(appointment.getPrice() + "");
        appointmentDescriptionField.setText(appointment.getDescription());
        patientNameField.setText(appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName());

        String patientGender = "";

        for (ComboBoxItemHelper gender : genderList) {
            if (gender.getDbName().equals(appointment.getPatient().getGender())) {
                patientGender = getResources().getString(gender.getBundleKey());
                break;
            }
        }

        patientGenderField.setText(patientGender);
        patientBirthDateField.setValue(appointment.getPatient().getBirthDate());
        patientPhoneField.setText(appointment.getPatient().getPhoneNumber());
        patientEmailField.setText(appointment.getPatient().getEmail());

        if (appointment.getPatient().getCity() != null && appointment.getPatient().getZipCode() != null &&
        appointment.getPatient().getHouseNumber() != null && appointment.getPatient().getStreetName() != null) {
            patientAddressField.setText(appointment.getPatient().getCity() + ", " + appointment.getPatient().getStreetName()
            + ", " + appointment.getPatient().getHouseNumber() + " - " + appointment.getPatient().getZipCode());
        }

        int totalPatientAppointments = 0;
        double totalPatientSpent = 0;
        int totalDoctorAppointments = 0;
        double totalDoctorEarned = 0;

        for (Appointment app : DataSource.getInstance().getAppointments()) {
            if (app.getPatient().equals(appointment.getPatient())) {
                totalPatientAppointments++;
                totalPatientSpent += app.getPrice();
            }
            if (app.getDoctor().equals(appointment.getDoctor())) {
                totalDoctorAppointments++;
                totalDoctorEarned += app.getPrice();
            }
        }

        patientAppointmentsField.setText(totalPatientAppointments + "");
        patientAmountField.setText(totalPatientSpent + "");

        doctorNameField.setText(appointment.getDoctor().getName());

        String doctorGender = "";

        for (ComboBoxItemHelper gender : genderList) {
            if (gender.getDbName().equals(appointment.getDoctor().getGender())) {
                doctorGender = getResources().getString(gender.getBundleKey());
                break;
            }
        }

        doctorGenderField.setText(doctorGender);
        doctorBirthDateField.setValue(appointment.getDoctor().getBirthDate());
        doctorPhoneField.setText(appointment.getDoctor().getPhoneNumber());
        doctorLicenseNumberField.setText(appointment.getDoctor().getLicenseNumber());

        doctorAppointmentsField.setText(totalDoctorAppointments + "");
        doctorAmountField.setText(totalDoctorEarned + "");
    }
}
