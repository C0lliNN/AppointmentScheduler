package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.raphaelcollin.appointmentscheduler.db.DataSource;
import com.raphaelcollin.appointmentscheduler.db.model.ComboBoxItemHelper;
import com.raphaelcollin.appointmentscheduler.db.model.Doctor;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class DoctorDialogController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label titleLabel;
    @FXML
    private GridPane fieldsGridPane;
    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField phoneField;
    @FXML
    private JFXTextField licenseNumberField;
    @FXML
    private JFXComboBox<ComboBoxItemHelper> genderField;
    @FXML
    private JFXDatePicker dateField;
    @FXML
    private HBox buttonsHBox;
    @FXML
    private JFXButton leftButton;
    @FXML
    private JFXButton rightButton;

    private Doctor doctor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double width = 700;
        double height = 520;

        root.setMinSize(width, height);
        root.setPrefSize(width, height);
        root.setMaxSize(width, height);

        titleLabel.setFont(Font.font(32));
        AnchorPane.setTopAnchor(titleLabel, 25.0);

        fieldsGridPane.setPadding(new Insets(10, 10, 0, 10));
        fieldsGridPane.setHgap(20);
        fieldsGridPane.setVgap(25);

        fieldsGridPane.getChildren().forEach(node -> {
            if (node instanceof Label) {
                ((Label) node).setFont(Font.font(18));
            }
        });

        AnchorPane.setTopAnchor(fieldsGridPane, 100.0);

        genderField.setPrefSize(134, 25);
        genderField.setItems(genderList);
        genderField.setCellFactory(new Callback<ListView<ComboBoxItemHelper>, ListCell<ComboBoxItemHelper>>() {
            @Override
            public ListCell<ComboBoxItemHelper> call(ListView<ComboBoxItemHelper> param) {
                return new ListCell<ComboBoxItemHelper>() {
                    @Override
                    protected void updateItem(ComboBoxItemHelper item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item != null && !empty) {
                            setText(getResources().getString(item.getBundleKey()));
                        } else {
                            setGraphic(null);
                        }

                    }
                };
            }
        });

        genderField.setButtonCell(genderField.getCellFactory().call(null));
        genderField.getSelectionModel().selectFirst();

        dateField.setDefaultColor(Color.valueOf("#085394"));

        buttonsHBox.setSpacing(25);
        buttonsHBox.getChildren().forEach(node -> ((JFXButton) node).setFont(Font.font(22)));

        AnchorPane.setBottomAnchor(buttonsHBox, 50.0);

        leftButton.getStyleClass().add(STYLE_CLASS_ORANGE_BUTTON);
        rightButton.getStyleClass().add(STYLE_CLASS_CONFIGURATION_GREEN_BUTTON);

        leftButton.setOnAction(this::handleClear);
    }

    public void setupAddDialog() {
        titleLabel.setText(getResources().getString(BUNDLE_KEY_ADD_DOCTOR));
        leftButton.setText(getResources().getString(BUNDLE_KEY_CLEAR));
        rightButton.setText(getResources().getString(BUNDLE_KEY_ADD));
        rightButton.setOnAction(this::handleAdd);
    }

    public void setupEditDialog(Doctor doctor) {
        titleLabel.setText(getResources().getString(BUNDLE_KEY_EDIT_DOCTOR));
        leftButton.setText(getResources().getString(BUNDLE_KEY_CLEAR));
        rightButton.setText(getResources().getString(BUNDLE_KEY_SAVE));
        rightButton.setOnAction(this::handleSave);

        nameField.setText(doctor.getName());

        int index = -1;

        for (ComboBoxItemHelper gender : genderList) {
            if (gender.getDbName().equals(doctor.getGender())) {
                index = gender.getComboBoxIndex();
            }
        }

        genderField.getSelectionModel().select(index);
        dateField.setValue(doctor.getBirthDate());
        phoneField.setText(doctor.getPhoneNumber());
        licenseNumberField.setText(doctor.getLicenseNumber());

        this.doctor = doctor;
    }

    @FXML
    private void handleAdd(ActionEvent event) {

        String name = nameField.getText().trim();
        String gender = genderField.getSelectionModel().getSelectedItem().getDbName();
        LocalDate birthDate = dateField.getValue();
        String phone = phoneField.getText().trim();
        String license = licenseNumberField.getText().trim();

        if (name.isEmpty() || gender.isEmpty() || birthDate == null || phone.isEmpty() || license.isEmpty()) {
            showRequiredFieldsErrorAlert(root);
        } else {

            Doctor doctor = new Doctor.Builder()
                    .setName(name)
                    .setGender(gender)
                    .setBirthDate(birthDate)
                    .setPhoneNumber(phone)
                    .setLicenseNumber(license)
                    .build();

            Task<Integer> task = new Task<Integer>() {
                @Override
                protected Integer call() {
                    return DataSource.getInstance().addDoctor(doctor);
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
    private void handleSave(ActionEvent event) {
        String name = nameField.getText().trim();
        String gender = genderField.getSelectionModel().getSelectedItem().getDbName();
        LocalDate birthDate = dateField.getValue();
        String phone = phoneField.getText().trim();
        String license = licenseNumberField.getText().trim();

        if (name.isEmpty() || gender.isEmpty() || birthDate == null || phone.isEmpty() || license.isEmpty()) {
            showRequiredFieldsErrorAlert(root);
        } else {

            Doctor doctor = new Doctor.Builder()
                    .setId(this.doctor.getId())
                    .setName(name)
                    .setGender(gender)
                    .setBirthDate(birthDate)
                    .setPhoneNumber(phone)
                    .setLicenseNumber(license)
                    .build();

            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call(){
                    return DataSource.getInstance().updateDoctor(doctor);
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
    private void handleClear(ActionEvent event) {
        nameField.clear();
        genderField.getSelectionModel().selectFirst();
        dateField.setValue(null);
        phoneField.clear();
        licenseNumberField.clear();
    }
}
