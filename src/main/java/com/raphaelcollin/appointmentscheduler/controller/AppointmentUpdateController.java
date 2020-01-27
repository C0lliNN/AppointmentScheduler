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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class AppointmentUpdateController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label titleLabel;
    @FXML
    private HBox hBoxButtons;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton clearButton;

    private AppointmentFieldsController fieldsController;

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
            saveButton.getStyleClass().add(STYLE_CLASS_CONFIGURATION_GREEN_BUTTON);

            saveButton.setDefaultButton(true);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setAppointment(Appointment appointment) {
        fieldsController.setAppointment(appointment);
    }

    @FXML
    public void handleClear() {
        fieldsController.clear();
    }

    @FXML
    public void handleSave() {
        Appointment appointment = fieldsController.getAppointment();

        if (appointment != null) {

            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return DataSource.getInstance().updateAppointment(appointment);
                }
            };

            task.setOnSucceeded(event -> root.setCursor(Cursor.DEFAULT));

            root.setCursor(Cursor.WAIT);
            new Thread(task).start();


        }

    }
}
