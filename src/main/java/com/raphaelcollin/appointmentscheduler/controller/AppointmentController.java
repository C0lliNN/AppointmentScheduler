package com.raphaelcollin.appointmentscheduler.controller;


import com.jfoenix.controls.*;
import com.raphaelcollin.appointmentscheduler.db.DataSource;
import com.raphaelcollin.appointmentscheduler.db.model.Appointment;
import com.raphaelcollin.appointmentscheduler.db.model.Doctor;
import com.raphaelcollin.appointmentscheduler.db.model.Patient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;
import static com.raphaelcollin.appointmentscheduler.db.DataSource.APPOINTMENTS_CHANGE;
import static com.raphaelcollin.appointmentscheduler.db.DataSource.INITIAL_DATA_LOADED;

public class AppointmentController implements Initializable, PropertyChangeListener {

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

        appointmentsTableView.setShowRoot(false);

        DataSource.getInstance().addObserver(this);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals(INITIAL_DATA_LOADED) || evt.getPropertyName().equals(APPOINTMENTS_CHANGE)) {

            ObservableList<Appointment> appointments = DataSource.getInstance().getAppointments();
            final TreeItem<Appointment> item = new TreeItem<>();

            for (Appointment appointment : appointments) {
                final TreeItem<Appointment> subItem = new TreeItem<>(appointment);
                item.getChildren().add(subItem);
            }

            Platform.runLater(() -> appointmentsTableView.setRoot(item));
        }


    }

    @FXML
    void handleDeleteAppointment() {

        TreeItem<Appointment> appointment = appointmentsTableView.getSelectionModel().getSelectedItem();

        if (appointment == null) {
            // Show an Alert
        } else {

            Task<Boolean> deleteAppointmentTask = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return DataSource.getInstance().deleteAppointment(appointment.getValue().getId());
                }
            };

            deleteAppointmentTask.setOnSucceeded(event -> {
                if (!deleteAppointmentTask.getValue()) {

                    // Show an Alert

                }
            });

            new Thread(deleteAppointmentTask).start();
        }
    }

    @FXML
    void handleNewAppointment() {

    }

    @FXML
    void handleShowDetails(ActionEvent event) {

    }

    @FXML
    void handleUpdateAppointment() {
        TreeItem<Appointment> selectedItem = appointmentsTableView.getSelectionModel().getSelectedItem();
        int selectedIndex = appointmentsTableView.getSelectionModel().getSelectedIndex();

        if (selectedItem == null) {
            // Show an Alert
        } else {

            // Open a Dialog Window passing the current Object

            Appointment appointment = selectedItem.getValue();
            appointment.setStatus("Completed");

            Task<Boolean> updateAppointmentTask = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return DataSource.getInstance().updateAppointment(appointment);
                }
            };

            updateAppointmentTask.setOnSucceeded(event -> {
                if (updateAppointmentTask.getValue()) {
                    Appointment updatedAppointment = new Appointment.Builder().
                            setStatus("Unconfirmed").
                            setDate(LocalDateTime.now()).
                            setDoctor(new Doctor.Builder().setName("Test").build()).
                            setPatient(new Patient.Builder().setFirstName("Test").setLastName("Test").build()).
                            build();
                    appointmentsTableView.getRoot().getChildren().add(new TreeItem<>(updatedAppointment));
                }
            });

            new Thread(updateAppointmentTask).start();
        }

    }
}