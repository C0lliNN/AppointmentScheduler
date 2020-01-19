package com.raphaelcollin.appointmentscheduler.controller;


import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.raphaelcollin.appointmentscheduler.Main;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;
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

    private static final String REMOVE_NON_DIGITS_REGEX = "[^\\d]";


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
        statusField.setValue(resources.getString(BUNDLE_KEY_STATUS_ALL));
        statusField.setItems(FXCollections.observableList(Arrays.asList(
                resources.getString(BUNDLE_KEY_STATUS_ALL),
                resources.getString(BUNDLE_KEY_STATUS_UNCONFIRMED),
                resources.getString(BUNDLE_KEY_STATUS_CONFIRMED),
                resources.getString(BUNDLE_KEY_STATUS_CANCEL),
                resources.getString(BUNDLE_KEY_STATUS_COMPLETED))));

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

            String [] dateParts1 = date1.substring(0, date1.length() - 2).split(":");
            String [] dateParts2 = date2.substring(0, date2.length() - 2).split(":");

            dateParts1[1] = dateParts1[1].replaceAll(REMOVE_NON_DIGITS_REGEX, "");
            dateParts2[1] = dateParts2[1].replaceAll(REMOVE_NON_DIGITS_REGEX, "");

            if (!dateParts1[0].equals(dateParts2[0])) {
                return Integer.compare(Integer.parseInt(dateParts1[0]), Integer.parseInt(dateParts2[0]));
            }

            return Integer.compare(Integer.parseInt(dateParts1[1]), Integer.parseInt(dateParts2[1]));
        });

        appointmentsTableView.setShowRoot(false);

        DataSource.getInstance().addObserver(this);

        dateField.disableProperty().bind(allDatesCheckBox.selectedProperty());

        patientField.textProperty().addListener(((observable, oldValue, newValue) ->
                appointmentsTableView.setPredicate(this::filterItem)
        ));

        doctorField.textProperty().addListener(((observable, oldValue, newValue) ->
                appointmentsTableView.setPredicate(this::filterItem)));

        dateField.valueProperty().addListener(((observable, oldValue, newValue) ->
                appointmentsTableView.setPredicate(this::filterItem)));

        dateField.disableProperty().addListener(((observable, oldValue, newValue) ->
                appointmentsTableView.setPredicate(this::filterItem)));

        statusField.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->
                appointmentsTableView.setPredicate(this::filterItem));


    }

    private boolean filterItem(TreeItem<Appointment> treeItem) {
        boolean show = true;

        if (dateField != null && doctorField != null && patientField != null && statusField != null) {
            if (!dateField.isDisable() && dateField.getValue() != null && !dateField.getValue()
                    .equals(treeItem.getValue().getDate().toLocalDate())) {
                show = false;
            }

            if (show && !doctorField.getText().trim().isEmpty() &&
                    !treeItem.getValue().getDoctor().getName().contains(doctorField.getText())) {
                show = false;
            }

            if (show && !patientField.getText().trim().isEmpty() &&
                    !treeItem.getValue().getPatient().getName().contains(patientField.getText())) {
                show = false;
            }

            if (statusField.getSelectionModel().getSelectedIndex() > 0 &&
                    !statusMap.get(statusField.getSelectionModel().getSelectedIndex()).equals(treeItem.getValue().getStatus())) {
                show = false;
            }
        }

        return show;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals(INITIAL_DATA_LOADED)) {

            ObservableList<Appointment> appointments = DataSource.getInstance().getAppointments();

            final RecursiveTreeItem<Appointment> root = new RecursiveTreeItem<>(appointments, RecursiveTreeObject::getChildren);

            Platform.runLater(() -> {
                appointmentsTableView.setRoot(root);
                dateField.setValue(LocalDate.now());
                appointmentsTableView.setPredicate(this::filterItem);
            });
        }

    }

    @FXML
    void handleDeleteAppointment() {

        TreeItem<Appointment> appointment = appointmentsTableView.getSelectionModel().getSelectedItem();

        if (appointment == null) {

            showAlert(Alert.AlertType.ERROR, root,
                    getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                    getResources().getString(BUNDLE_KEY_INVALID_SELECTION_HEADER_TEXT),
                    getResources().getString(BUNDLE_KEY_INVALID_SELECTION_CONTENT_TEXT));

        } else {

            Task<Boolean> deleteAppointmentTask = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return DataSource.getInstance().deleteAppointment(appointment.getValue().getId());
                }
            };

            deleteAppointmentTask.setOnSucceeded(event -> {
                if (!deleteAppointmentTask.getValue()) {

                    showAlert(Alert.AlertType.ERROR, root,
                            getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                            getResources().getString(BUNDLE_KEY_DATABASE_ERROR_HEADER_TEXT),
                            getResources().getString(BUNDLE_KEY_DATABASE_ERROR_CONTENT_TEXT));

                }
            });


            new Thread(deleteAppointmentTask).start();
        }
    }

    @FXML
    void handleNewAppointment() {

        Task<Parent> task = new Task<Parent>() {
            @Override
            protected Parent call() {

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(APPOINTMENT_ADD_LOCATION), getResources());
                    Parent addAppointmentParent = loader.load();

                    return Main.createView(600, 750, addAppointmentParent);


                } catch (IOException e) {
                    System.err.println("Error in AppointmentController - handleNewAppointment() " + e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }
        };

        task.setOnSucceeded(event -> {
            Scene scene = new Scene(task.getValue());
            Stage newStage = new Stage();
            newStage.initOwner(root.getScene().getWindow());
            newStage.setTitle(getResources().getString(BUNDLE_KEY_APPLICATION_TITLE));
            newStage.setScene(scene);
            newStage.initStyle(StageStyle.TRANSPARENT);
            newStage.show();
        });

        new Thread(task).start();

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