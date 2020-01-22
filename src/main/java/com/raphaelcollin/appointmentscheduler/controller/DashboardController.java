package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.*;
import com.raphaelcollin.appointmentscheduler.db.DataSource;
import com.raphaelcollin.appointmentscheduler.db.model.Appointment;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;
import static com.raphaelcollin.appointmentscheduler.db.DataSource.APPOINTMENTS_CHANGE;
import static com.raphaelcollin.appointmentscheduler.db.DataSource.INITIAL_DATA_LOADED;

public class DashboardController implements Initializable, PropertyChangeListener {

    @FXML
    private AnchorPane root;
    @FXML
    private VBox vBoxUpcomingAppointments;
    @FXML
    private Label upcomingAppointmentsLabel;
    @FXML
    private VBox vBoxUnconfirmedAppointments;
    @FXML
    private Label unconfirmedAppointmentsLabel;
    @FXML
    private VBox vBoxCompletedAppointments;
    @FXML
    private Label completedAppointmentsLabel;
    @FXML
    private VBox vBoxEarnings;
    @FXML
    private Label earningsLabel;
    @FXML
    private JFXProgressBar completedAppointmentsProgressBar;
    @FXML
    private JFXProgressBar unconfirmedAppointmentsProgressBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        double width = 1000;
        double height = 800;

        root.setMinSize(width,height);
        root.setPrefSize(width, height);
        root.setMaxSize(width, height);

        for (Node node : root.getChildren()) {

            double boxWidth = 501;
            double boxHeight = 401;

            VBox box = (VBox) node;

            box.setSpacing(20);
            box.setPadding(new Insets(75,0,0,0));
            box.setMinSize(boxWidth, boxHeight);
            box.setPrefSize(boxWidth, boxHeight);
            box.setMaxSize(boxWidth, boxHeight);

            ((Label) box.getChildren().get(0)).setFont(Font.font(32));

            Node nodeValue = (box.getChildren().get(1));

            ObservableList<Label> valueLabels = FXCollections.observableArrayList();

            if (nodeValue instanceof Label) {
                valueLabels.add((Label) nodeValue);
            } else if (nodeValue instanceof HBox) {
                ((HBox) nodeValue).getChildren().forEach(label -> valueLabels.add((Label) label));
            }

            valueLabels.forEach(label -> {
                label.setFont(Font.font(40));
                label.getStyleClass().add(STYLE_CLASS_DASHBOARD_VALUE_LABEL);
            });

            int buttonIndex = box.getChildren().size() - 1;
            ((JFXButton) box.getChildren().get(buttonIndex)).setFont(Font.font(22));

        }

        vBoxUpcomingAppointments.getStyleClass().add(STYLE_CLASS_DASHBOARD_UPCOMING_BOX);
        vBoxUnconfirmedAppointments.getStyleClass().add(STYLE_CLASS_DASHBOARD_UNCONFIRMED_BOX);
        vBoxUnconfirmedAppointments.setLayoutX(500);
        vBoxCompletedAppointments.getStyleClass().add(STYLE_CLASS_DASHBOARD_COMPLETED_BOX);
        vBoxCompletedAppointments.setLayoutY(400);
        vBoxEarnings.getStyleClass().add(STYLE_CLASS_DASHBOARD_EARNINGS_BOX);
        vBoxEarnings.setLayoutX(500);
        vBoxEarnings.setLayoutY(400);

        double progressBarWidth = 300;
        double progressBarHeight = 30;

        completedAppointmentsProgressBar.setMinSize(progressBarWidth, progressBarHeight);
        completedAppointmentsProgressBar.setPrefSize(progressBarWidth, progressBarHeight);
        completedAppointmentsProgressBar.setMaxSize(progressBarWidth, progressBarHeight);

        completedAppointmentsProgressBar.setProgress(0.55);

        unconfirmedAppointmentsProgressBar.setMinSize(progressBarWidth, progressBarHeight);
        unconfirmedAppointmentsProgressBar.setPrefSize(progressBarWidth, progressBarHeight);
        unconfirmedAppointmentsProgressBar.setMaxSize(progressBarWidth, progressBarHeight);

        unconfirmedAppointmentsProgressBar.setProgress(0.4);

        DataSource.getInstance().addObserver(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(APPOINTMENTS_CHANGE) ||  evt.getPropertyName().equals(INITIAL_DATA_LOADED)) {

            int numberOfAppointments = 0;
            int numberOfUnconfirmedAppointments = 0;
            int numberOfCompletedAppointments = 0;
            double earnings = 0;

            for (Appointment appointment : DataSource.getInstance().getAppointments()) {

                if (appointment.getDate().toLocalDate().equals(LocalDate.now())) {
                    if (appointment.getStatus().equals(UNCONFIRMED)) {
                        numberOfUnconfirmedAppointments++;
                    }
                    if (appointment.getStatus().equals(COMPLETED)) {
                        numberOfCompletedAppointments++;
                        earnings += appointment.getPrice();
                    }

                    numberOfAppointments++;
                }

            }

            int tempTotalAppointments = numberOfAppointments;
            int tempTotalUnconfirmedAppointments = numberOfUnconfirmedAppointments;
            int tempTotalCompletedAppointments = numberOfCompletedAppointments;
            double tempEarnings = earnings;

            Platform.runLater(() -> {

                upcomingAppointmentsLabel.setText(tempTotalAppointments + "");
                unconfirmedAppointmentsLabel.setText(tempTotalUnconfirmedAppointments + "/" + tempTotalAppointments);

                double unconfirmedAppointmentsProgress = tempTotalAppointments > 0 ? (double) tempTotalUnconfirmedAppointments / (double) tempTotalAppointments : 1;
                unconfirmedAppointmentsProgressBar.setProgress(unconfirmedAppointmentsProgress);

                completedAppointmentsLabel.setText(tempTotalCompletedAppointments + "/" + tempTotalAppointments);

                double completedAppointmentsProgress = tempTotalAppointments > 0 ? (double) tempTotalCompletedAppointments / (double) tempTotalAppointments : 1;
                completedAppointmentsProgressBar.setProgress(completedAppointmentsProgress);

                earningsLabel.setText(tempEarnings + "");
            });

        }
    }

    @FXML
    void handleShowUpcomingAppointments() {

        showAppointmentTab(0);

    }

    @FXML
    void handleShowUnconfirmedAppointments() {

        showAppointmentTab(UNCONFIRMED_INDEX);
    }

    @FXML
    void handleShowCompletedAppointments() {
        showAppointmentTab(COMPLETED_INDEX);
    }

    @FXML
    void handleShowEarnings() {

        TabPane tabpane = (TabPane) root.getParent().getParent();
        AnchorPane financialContentRoot = (AnchorPane) tabpane.getTabs().get(2).getContent();

        GridPane fields = (GridPane) financialContentRoot.getChildren().get(3);

        LocalDate today = LocalDate.now();

        ((JFXComboBox<Integer>) fields.getChildren().get(4)).getSelectionModel().select(new Integer(today.getYear()));
        ((JFXComboBox) fields.getChildren().get(5)).getSelectionModel().select(today.getMonthValue() - 1);
        ((JFXTextField) fields.getChildren().get(6)).setText(today.getDayOfMonth() + "");
        ((JFXTextField) fields.getChildren().get(7)).setText(today.getDayOfMonth() + "");

        tabpane.getSelectionModel().select(2);

    }

    private JFXDatePicker getDateField(AnchorPane contentRoot) {
        HBox hBoxFilters = (HBox) contentRoot.getChildren().get(0);
        return ((JFXDatePicker)((HBox) hBoxFilters.getChildren().get(0)).getChildren().get(1));
    }

    private JFXTextField getPatientField(AnchorPane contentRoot) {
        HBox hBoxFilters = (HBox) contentRoot.getChildren().get(0);
        return ((JFXTextField)((HBox) hBoxFilters.getChildren().get(1)).getChildren().get(1));
    }

    private JFXTextField getDoctorField(AnchorPane contentRoot) {
        HBox hBoxFilters = (HBox) contentRoot.getChildren().get(0);
        return ((JFXTextField)((HBox) hBoxFilters.getChildren().get(2)).getChildren().get(1));
    }

    private JFXComboBox getStatusField(AnchorPane contentRoot) {
        HBox hBoxFilters = (HBox) contentRoot.getChildren().get(0);
        return ((JFXComboBox)((HBox) hBoxFilters.getChildren().get(3)).getChildren().get(1));
    }

    private JFXCheckBox getAllDatesCheckBox(AnchorPane contentRoot) {
        return (JFXCheckBox) contentRoot.getChildren().get(1);
    }

    private void showAppointmentTab(int statusIndex) {
        TabPane tabPane = ((TabPane) root.getParent().getParent());

        AnchorPane appointmentContentRoot = (AnchorPane) tabPane.getTabs().get(1).getContent();

        JFXDatePicker dateField = getDateField(appointmentContentRoot);
        dateField.setValue(LocalDate.now());

        JFXTextField patientField = getPatientField(appointmentContentRoot);
        patientField.clear();

        JFXTextField doctorField = getDoctorField(appointmentContentRoot);
        doctorField.clear();

        JFXComboBox<String> statusField = getStatusField(appointmentContentRoot);
        statusField.getSelectionModel().select(statusIndex);

        JFXCheckBox checkBox = getAllDatesCheckBox(appointmentContentRoot);
        checkBox.setSelected(false);

        tabPane.getSelectionModel().select(1);
    }
}
