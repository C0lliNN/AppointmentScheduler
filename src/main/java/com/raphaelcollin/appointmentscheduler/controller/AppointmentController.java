package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.raphaelcollin.appointmentscheduler.Main;
import com.raphaelcollin.appointmentscheduler.db.DataSource;
import com.raphaelcollin.appointmentscheduler.db.model.Appointment;
import com.raphaelcollin.appointmentscheduler.db.model.ComboBoxItemHelper;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;
import static com.raphaelcollin.appointmentscheduler.db.DataSource.APPOINTMENTS_CHANGE;
import static com.raphaelcollin.appointmentscheduler.db.DataSource.INITIAL_DATA_LOADED;

public class AppointmentController implements Initializable, PropertyChangeListener {

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
    private JFXComboBox<ComboBoxItemHelper> statusField;
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
    @FXML
    private JFXTreeTableView<Appointment> appointmentsTableView;
    @FXML
    private TreeTableColumn<Appointment, String> dateColumn;
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

            ((Control) box.getChildren().get(1)).setPrefWidth(150);
        });
        AnchorPane.setTopAnchor(filtersHBox, 50.0);

        dateField.setStyle("-fx-font-size: 15px");
        dateField.setDefaultColor(Color.valueOf("#085394"));
        patientField.setFont(Font.font(15));
        doctorField.setFont(Font.font(15));

        statusField.setItems(statusList);

        statusField.setCellFactory(new Callback<ListView<ComboBoxItemHelper>, ListCell<ComboBoxItemHelper>>() {
            @Override
            public ListCell<ComboBoxItemHelper> call(ListView<ComboBoxItemHelper> param) {
                return new ListCell<ComboBoxItemHelper>() {
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

        statusField.getSelectionModel().selectFirst();

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

        appointmentsTableView.getColumns().remove(dateColumn);

        dateColumn.setCellValueFactory(param -> param.getValue().getValue().getDateProperty());
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

            String [] dateParts1 = date1.replaceAll("[^\\d:]+", "").split(":");
            String [] dateParts2 = date2.replaceAll("[^\\d:]+", "").split(":");

            dateParts1[1] = dateParts1[1].replaceAll(REMOVE_NON_DIGITS_REGEX, "");
            dateParts2[1] = dateParts2[1].replaceAll(REMOVE_NON_DIGITS_REGEX, "");

            if (!dateParts1[0].equals(dateParts2[0])) {
                return Integer.compare(Integer.parseInt(dateParts1[0]), Integer.parseInt(dateParts2[0]));
            }

            return Integer.compare(Integer.parseInt(dateParts1[1]), Integer.parseInt(dateParts2[1]));
        });

        appointmentsTableView.setShowRoot(false);

        DataSource.getInstance().addObserver(this);

        allDatesCheckBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                appointmentsTableView.getColumns().add(0, dateColumn);
            } else {
                appointmentsTableView.getColumns().remove(dateColumn);
            }
        }));

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
                    !statusField.getSelectionModel().getSelectedItem().getDbName().equals(treeItem.getValue().getStatus())) {
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
                appointmentsTableView.getSortOrder().add(scheduleColumn);
            });
        }

        if (evt.getPropertyName().equals(APPOINTMENTS_CHANGE)) {
            Platform.runLater(() -> {
                if (appointmentsTableView.getColumns().contains(dateColumn)) {
                    appointmentsTableView.getSortOrder().add(dateColumn);
                } else {
                    appointmentsTableView.getSortOrder().add(scheduleColumn);
                }
            });
        }

    }

    @FXML
    void handleShowDetails() {
        TreeItem<Appointment> selectedItem = appointmentsTableView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showSelectionErrorAlert(root);
        } else {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(APPOINTMENT_DETAILS_LOCATION), getResources());
                Parent detailsViewRoot = loader.load();

                AppointmentDetailsController controller = loader.getController();
                controller.setAppointment(selectedItem.getValue());

                Parent containerRoot = Main.createNewView(608, 650, detailsViewRoot);
                createNewStage(containerRoot, root);

            } catch (IOException e) {
                System.err.println("Error in AppointmentController - handleShowDetails(): + " + e.getMessage());
            }
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

                    return Main.createNewView(600, 750, addAppointmentParent);


                } catch (IOException e) {
                    System.err.println("Error in AppointmentController - handleNewAppointment() " + e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }
        };

        task.setOnSucceeded(event -> createNewStage(task.getValue(), root));

        new Thread(task).start();

    }

    @FXML
    void handleUpdateAppointment() {
        TreeItem<Appointment> selectedItem = appointmentsTableView.getSelectionModel().getSelectedItem();


        if (selectedItem == null) {
            showSelectionErrorAlert(root);
        } else {


            Appointment appointment = selectedItem.getValue();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(APPOINTMENT_UPDATE_LOCATION), getResources());
                Parent updateRoot =  loader.load();

                AppointmentUpdateController controller = loader.getController();
                controller.setAppointment(appointment);

                Parent container = Main.createNewView(600, 800, updateRoot);

                createNewStage(container, root);

            } catch (IOException e) {
                System.err.println("Error in AppointmentController - handleUpdateAppointment(): + " + e.getMessage());
            }

        }

    }

    @FXML
    void handleDeleteAppointment() {

        TreeItem<Appointment> appointment = appointmentsTableView.getSelectionModel().getSelectedItem();

        if (appointment == null) {

            showSelectionErrorAlert(root);

        } else {

            Task<Boolean> deleteAppointmentTask = new Task<Boolean>() {
                @Override
                protected Boolean call(){
                    return DataSource.getInstance().deleteAppointment(appointment.getValue().getId());
                }
            };

            deleteAppointmentTask.setOnSucceeded(event -> {
                if (!deleteAppointmentTask.getValue()) {

                    showDatabaseErrorAlert(root);

                }
            });
            new Thread(deleteAppointmentTask).start();
        }
    }
}