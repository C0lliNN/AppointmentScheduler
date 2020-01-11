package com.raphaelcollin.appointmentscheduler.controller;


import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.raphaelcollin.appointmentscheduler.db.model.Appointment;
import com.raphaelcollin.appointmentscheduler.db.model.Doctor;
import com.raphaelcollin.appointmentscheduler.db.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class AppointmentController implements Initializable {

    @FXML
    private TreeTableColumn<Appointment, String> scheduleColumn;
    @FXML
    private TreeTableColumn<Appointment, String> patientColumn;
    @FXML
    private TreeTableColumn<Appointment, String> descriptionColumn;
    @FXML
    private TreeTableColumn<Appointment, String> doctorColumn;
    @FXML
    private TreeTableColumn<Appointment, Double> priceColumn;
    @FXML
    private TreeTableColumn<Appointment, String> statusColumn;
    @FXML
    private AnchorPane root;
    @FXML
    private HBox filtersHBox;

    @FXML
    private JFXDatePicker dateField;

    @FXML
    private JFXTextField doctorField;

    @FXML
    private JFXTextField patientField;

    @FXML
    private JFXComboBox<String> statusField;

    @FXML
    private JFXTreeTableView<Appointment> appointmentsTableView;

    @FXML
    private JFXCheckBox allDatesCheckBox;

    @FXML
    private HBox buttonsHBox;

    @FXML
    private JFXButton showDetailsButton;

    @FXML
    private JFXButton newAppointmentButton;

    @FXML
    private JFXButton updateAppointmentButton;

    @FXML
    private JFXButton deleteAppointmentButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double width = 1000;
        double height = 800;

        root.setMinSize(width, height);
        root.setPrefSize(width, height);
        root.setMaxSize(width, height);

        root.getStyleClass().add(STYLE_CLASS_APPOINTMENT_TAB);

        filtersHBox.setSpacing(10);
        filtersHBox.getChildren().forEach(node -> {
            HBox box = (HBox) node;

            box.setSpacing(15);

            ((Label) box.getChildren().get(0)).setFont(Font.font(18));
        });
        AnchorPane.setTopAnchor(filtersHBox, 50.0);

        dateField.setStyle("-fx-font-size: 15px");
        patientField.setFont(Font.font(15));
        doctorField.setFont(Font.font(15));
        statusField.setValue("All");
        statusField.setItems(FXCollections.observableList(Arrays.asList("All", "Scheduled", "Confirmed", "Completed")));

        dateField.disableProperty().bind(allDatesCheckBox.selectedProperty());

        allDatesCheckBox.setFont(Font.font(14));
        allDatesCheckBox.getStyleClass().add(STYLE_CLASS_BLUE_CHECK_BOX);
        AnchorPane.setTopAnchor(allDatesCheckBox, 87.0);
        AnchorPane.setLeftAnchor(allDatesCheckBox,20.0);


        showDetailsButton.getStyleClass().add(STYLE_CLASS_BLUE_BUTTON);
        newAppointmentButton.getStyleClass().add(STYLE_CLASS_MAIN_VIEW_GREEN_BUTTON);
        updateAppointmentButton.getStyleClass().add(STYLE_CLASS_MAIN_VIEW_ORANGE_BUTTON);
        deleteAppointmentButton.getStyleClass().add(STYLE_CLASS_RED_BUTTON);

        buttonsHBox.setSpacing(30);
        AnchorPane.setTopAnchor(buttonsHBox, 130.0);
        buttonsHBox.getChildren().forEach(node -> ((Button) node).setFont(Font.font(19)));

        AnchorPane.setTopAnchor(appointmentsTableView, 200.0);

        scheduleColumn.setCellValueFactory(param -> param.getValue().getValue().getScheduleProperty());
        patientColumn.setCellValueFactory(param -> param.getValue().getValue().getPatientProperty());
        descriptionColumn.setCellValueFactory(param -> param.getValue().getValue().getDescriptionProperty());
        doctorColumn.setCellValueFactory(param -> param.getValue().getValue().getDoctorProperty());
        priceColumn.setCellValueFactory(param -> param.getValue().getValue().getPriceProperty());
        statusColumn.setCellValueFactory(param -> param.getValue().getValue().getStatusProperty());

        scheduleColumn.setComparator((date1, date2) -> {
            if (date1.contains("AM") && date2.contains("PM")) {
                return -1;
            }
            if (date1.contains("PM") && date2.contains("AM")) {
                return 1;
            }

            String [] dateParts1 = date1.trim().split(":");
            String [] dateParts2 = date2.trim().split(":");

            if (!dateParts1[0].equals(dateParts2[0])) {
                return Integer.compare(Integer.parseInt(dateParts1[0]), Integer.parseInt(dateParts2[0]));
            }

            return Integer.compare(Integer.parseInt(dateParts1[1]), Integer.parseInt(dateParts2[1]));
        });


        ObservableList<Appointment> appointments = FXCollections.observableArrayList(

                new Appointment.Builder().
                        setIdAppointment(1).
                        setDate(LocalDateTime.of(LocalDate.of(2020, 1, 11), LocalTime.of(12,0))).
                        setPrice(100.0).
                        setDescription("Test").
                        setStatus("Completed").
                        setDoctor(new Doctor("Raphael")).
                        setPatient(new Patient.Builder().
                                setFirstName("Phillipe").
                                setLastName("Olivier").
                                build()).
                        build(),
                new Appointment.Builder().
                        setIdAppointment(2).
                        setDate(LocalDateTime.of(LocalDate.of(2020, 1, 11), LocalTime.of(13,12))).
                        setPrice(120.0).
                        setDescription("Test2").
                        setStatus("Unconfirmed").
                        setDoctor(new Doctor("Raphael")).
                        setPatient(new Patient.Builder().
                                setFirstName("Christiane").
                                setLastName("Maria").
                                build()).
                        build(),
                new Appointment.Builder().
                        setIdAppointment(3).
                        setDate(LocalDateTime.of(LocalDate.of(2020, 1, 11), LocalTime.of(14,0))).
                        setPrice(90.0).
                        setDescription("Test3").
                        setStatus("Canceled").
                        setDoctor(new Doctor("Julio")).
                        setPatient(new Patient.Builder().
                                setFirstName("John").
                                setLastName("Len").
                                build()).
                        build(),
                new Appointment.Builder().
                        setIdAppointment(4).
                        setDate(LocalDateTime.of(LocalDate.of(2020, 1, 11), LocalTime.of(16,45))).
                        setPrice(200.0).
                        setDescription("Test4").
                        setStatus("Completed").
                        setDoctor(new Doctor("Pedro")).
                        setPatient(new Patient.Builder().
                                setFirstName("Phillipe").
                                setLastName("Olivier").
                                build()).
                        build()
        );

        final TreeItem<Appointment> item = new RecursiveTreeItem<>(appointments, RecursiveTreeObject::getChildren);

        appointmentsTableView.setRoot(item);
        appointmentsTableView.setShowRoot(false);

        // Style TreeView, Create Wireframe for Appointments, Create Datamodel and DAOs


    }

    @FXML
    void handleDeleteAppointment(ActionEvent event) {

    }

    @FXML
    void handleNewAppointment(ActionEvent event) {

    }

    @FXML
    void handleShowDetails(ActionEvent event) {

    }

    @FXML
    void handleUpdateAppointment(ActionEvent event) {

    }
}

