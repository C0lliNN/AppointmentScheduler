package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.raphaelcollin.appointmentscheduler.db.DataSource;
import com.raphaelcollin.appointmentscheduler.db.model.Appointment;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class AppointmentAddController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label titleLabel;
    private AppointmentFieldsController fieldsController;
    @FXML
    private HBox hBoxButtons;
    @FXML
    private JFXButton clearButton;
    @FXML
    private JFXButton addButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {

            titleLabel.setFont(Font.font(32));
            AnchorPane.setTopAnchor(titleLabel, 25.0);

            FXMLLoader loader = new FXMLLoader(getClass().getResource(APPOINTMENT_FIELDS_LOCATION), resources);
            Parent appointmentsField = loader.load();

            fieldsController = loader.getController();

            root.getChildren().add(appointmentsField);

            AnchorPane.setTopAnchor(appointmentsField, 60.0);
            AnchorPane.setLeftAnchor(appointmentsField, 0.0);
            AnchorPane.setRightAnchor(appointmentsField, 0.0);

            hBoxButtons.setSpacing(50);
            hBoxButtons.getChildren().forEach(node -> {
                JFXButton button = (JFXButton) node;
                button.setButtonType(JFXButton.ButtonType.RAISED);
                button.setFont(Font.font(24));
            });

            AnchorPane.setBottomAnchor(hBoxButtons, 40.0);

            clearButton.getStyleClass().add(STYLE_CLASS_ORANGE_BUTTON);
            addButton.getStyleClass().add(STYLE_CLASS_CONFIGURATION_GREEN_BUTTON);

        } catch (IOException e) {
            System.err.println("Error in AppointmentAdd - initialize(): " + e.getMessage());
        }
    }

    @FXML
    void handleClear() {
        fieldsController.clear();
    }

    @FXML
    void handleAdd() {
        Appointment newAppointment = fieldsController.getAppointment();

        if (newAppointment != null) {

            Task<Integer> task = new Task<Integer>() {
                @Override
                protected Integer call() {
                    return DataSource.getInstance().addAppointment(newAppointment);
                }
            };

            task.setOnSucceeded(event -> {
                root.setCursor(Cursor.DEFAULT);
                if (task.getValue() < 0) {
                    showAlert(Alert.AlertType.ERROR, root,
                            getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                            getResources().getString(BUNDLE_KEY_ERROR_CONNECTION_LABEL),
                            getResources().getString(BUNDLE_KEY_ERROR_DATABASE_MESSAGE));
                }
            });

            root.setCursor(Cursor.WAIT);
            new Thread(task).start();

        }
    }
}
