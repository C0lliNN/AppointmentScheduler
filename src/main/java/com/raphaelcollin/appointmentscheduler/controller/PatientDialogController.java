package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.raphaelcollin.appointmentscheduler.db.DataSource;
import com.raphaelcollin.appointmentscheduler.db.model.Patient;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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

public class PatientDialogController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label titleLabel;
    @FXML
    private HBox buttonsHBox;
    @FXML
    private JFXButton rightButton;
    @FXML
    private JFXButton leftButton;

    private PatientFieldsController fieldsController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            titleLabel.setFont(Font.font(32));
            AnchorPane.setTopAnchor(titleLabel, 25.0);

            FXMLLoader loader = new FXMLLoader(getClass().getResource(PATIENT_FIELDS_LOCATION), resources);
            Parent fields = loader.load();
            fieldsController = loader.getController();
            root.getChildren().add(fields);

            AnchorPane.setLeftAnchor(fields, 50.0);
            AnchorPane.setRightAnchor(fields, 50.0);

            AnchorPane.setTopAnchor(fields, 100.0);

            buttonsHBox.setSpacing(25);
            buttonsHBox.getChildren().forEach(node -> ((JFXButton) node).setFont(Font.font(22)));
            AnchorPane.setBottomAnchor(buttonsHBox, 50.0);

            rightButton.getStyleClass().add(STYLE_CLASS_CONFIGURATION_GREEN_BUTTON);
            leftButton.getStyleClass().add(STYLE_CLASS_ORANGE_BUTTON);

        } catch (IOException e) {
            System.err.println("Error in PatientDialog - initialize(): " + e.getMessage());
        }
    }

    public void setupAddDialog() {
        titleLabel.setText(getResources().getString(BUNDLE_KEY_PATIENT_DIALOG_ADD_TITLE));
        leftButton.setText(getResources().getString(BUNDLE_KEY_DIALOG_CLEAR));
        leftButton.setOnAction(this::clear);
        rightButton.setText(getResources().getString(BUNDLE_KEY_DIALOG_ADD));
        rightButton.setOnAction(this::addPatient);
    }

    public void setupEditDialog(Patient patient) {
        titleLabel.setText(getResources().getString(BUNDLE_KEY_PATIENT_DIALOG_EDIT_TITLE));
        leftButton.setText(getResources().getString(BUNDLE_KEY_DIALOG_CLEAR));
        leftButton.setOnAction(this::clear);
        rightButton.setText(getResources().getString(BUNDLE_KEY_DIALOG_SAVE));
        rightButton.setOnAction(this::savePatient);
        fieldsController.setPatient(patient);
    }

    private void savePatient(ActionEvent event) {
        Patient patient = fieldsController.getPatient();

        if (patient != null) {
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return DataSource.getInstance().updatePatient(patient);
                }
            };

            task.setOnSucceeded(event1 -> {

                root.setCursor(Cursor.DEFAULT);

                if (!task.getValue()) {
                    showDatabaseErrorAlert(root);
                }
            });

            root.setCursor(Cursor.WAIT);
            new Thread(task).start();
        }
    }

    @FXML
    private void addPatient(ActionEvent event) {
        Patient patient = fieldsController.getPatient();

        if (patient != null) {

            Task<Integer> task = new Task<Integer>() {
                @Override
                protected Integer call() {
                    return DataSource.getInstance().addPatient(patient);
                }
            };

            task.setOnSucceeded(event1 -> {

                root.setCursor(Cursor.DEFAULT);

                if (task.getValue() < 0) {

                    showDatabaseErrorAlert(root);
                }
            });

            root.setCursor(Cursor.WAIT);
            new Thread(task).start();

        }
    }

    @FXML
    private void clear(ActionEvent event) {
        fieldsController.clear();
    }
}
