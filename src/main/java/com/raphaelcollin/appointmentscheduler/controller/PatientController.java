package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.raphaelcollin.appointmentscheduler.db.DataSource;
import com.raphaelcollin.appointmentscheduler.db.model.Patient;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;
import static com.raphaelcollin.appointmentscheduler.db.DataSource.INITIAL_DATA_LOADED;
import static com.raphaelcollin.appointmentscheduler.db.DataSource.PATIENTS_CHANGE;

public class PatientController implements Initializable, PropertyChangeListener {

    @FXML
    private AnchorPane root;
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXButton editButton;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private JFXTreeTableView<Patient> patientsTableView;
    @FXML
    private TreeTableColumn<Patient, String> nameColumn;
    @FXML
    private TreeTableColumn<Patient, String> genderColumn;
    @FXML
    private TreeTableColumn<Patient, String> birthDateColumn;
    @FXML
    private TreeTableColumn<Patient, String> phoneColumn;
    @FXML
    private TreeTableColumn<Patient, String> emailColumn;
    @FXML
    private TreeTableColumn<Patient, String> addressColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double width = 1000;
        double height = 800;

        root.setMinSize(width, height);
        root.setPrefSize(width, height);
        root.setMaxSize(width, height);


        searchField.setPrefSize(495, 45);
        searchField.setFont(Font.font(20));
        AnchorPane.setTopAnchor(searchField, 30.0);
        AnchorPane.setLeftAnchor(searchField, 50.0);

        searchField.textProperty().addListener(((observable, oldValue, newValue) -> {
            patientsTableView.setPredicate(patient ->
                    newValue.trim().isEmpty() ||
                    patient.getValue().getFirstName().toLowerCase().contains(newValue.toLowerCase()) ||
                    patient.getValue().getLastName().toLowerCase().contains(newValue.toLowerCase()) ||
                    patient.getValue().getGender().toLowerCase().contains(newValue.toLowerCase()) ||
                    patient.getValue().getPhoneNumber().contains(newValue) ||
                    ((patient.getValue().getEmail() != null) && patient.getValue().getEmail().contains(newValue)) ||
                    ((patient.getValue().getZipCode() != null) && patient.getValue().getZipCode().contains(newValue)) ||
                    ((patient.getValue().getCity() != null) && patient.getValue().getCity().toLowerCase().contains(newValue.toLowerCase())) ||
                    ((patient.getValue().getHouseNumber() != null) && patient.getValue().getHouseNumber().contains(newValue.toLowerCase())));
        }));

        for (int i = 1; i < 4; i++) {
            JFXButton button = (JFXButton) root.getChildren().get(i);
            button.setFont(Font.font(18));
            button.setPrefSize(120, 40);
            AnchorPane.setTopAnchor(button, 30.0);
            button.setTextFill(Color.WHITE);
        }

        addButton.getStyleClass().add(STYLE_CLASS_MAIN_VIEW_GREEN_BUTTON);
        FontAwesomeIconView addIcon = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
        addIcon.getStyleClass().add(STYLE_CLASS_BUTTON_ICON);
        addButton.setGraphic(addIcon);
        AnchorPane.setLeftAnchor(addButton, 576.0);

        editButton.getStyleClass().add(STYLE_CLASS_BLUE_BUTTON);
        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL);
        editIcon.getStyleClass().add(STYLE_CLASS_BUTTON_ICON);
        editButton.setGraphic(editIcon);
        AnchorPane.setLeftAnchor(editButton, 701.0);

        deleteButton.getStyleClass().add(STYLE_CLASS_RED_BUTTON);
        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        deleteIcon.getStyleClass().add(STYLE_CLASS_BUTTON_ICON);
        deleteButton.setGraphic(deleteIcon);
        AnchorPane.setLeftAnchor(deleteButton, 826.0);


        AnchorPane.setTopAnchor(patientsTableView, 130.0);
        nameColumn.setCellValueFactory(param -> param.getValue().getValue().getNameProperty());
        genderColumn.setCellValueFactory(param -> param.getValue().getValue().getGenderProperty());
        birthDateColumn.setCellValueFactory(param -> param.getValue().getValue().getBirthDateProperty());
        phoneColumn.setCellValueFactory(param -> param.getValue().getValue().getPhoneProperty());
        emailColumn.setCellValueFactory(param -> param.getValue().getValue().getEmailProperty());
        addressColumn.setCellValueFactory(param -> param.getValue().getValue().getAddressProperty());

        DataSource.getInstance().addObserver(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(INITIAL_DATA_LOADED)) {
            ObservableList<Patient> patients = DataSource.getInstance().getPatients();

            final RecursiveTreeItem<Patient> root = new RecursiveTreeItem<>(patients, RecursiveTreeObject::getChildren);
            Platform.runLater(() -> {
                patientsTableView.setRoot(root);
                patientsTableView.getSortOrder().add(nameColumn);
            });
        }

        if (evt.getPropertyName().equals(PATIENTS_CHANGE)) {
            Platform.runLater(() -> patientsTableView.getSortOrder().add(nameColumn));
        }
    }

    @FXML
    void handleAdd() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(PATIENT_DIALOG_LOCATION), getResources());
            Parent patientDialog = loader.load();
            PatientDialogController controller = loader.getController();
            controller.setupAddDialog();

            AnchorPane containerRoot = createNewView(900, 750, patientDialog);
            createNewStage(containerRoot, root);

        } catch (IOException e){
            System.err.println("PatientController - handleAdd(): " + e.getMessage());
        }

    }

    @FXML
    void handleDelete() {
        TreeItem<Patient> selectedPatient = patientsTableView.getSelectionModel().getSelectedItem();

        if (selectedPatient == null) {
            showSelectionErrorAlert(root);
        } else {

            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return DataSource.getInstance().deletePatient(selectedPatient.getValue().getId());
                }
            };

            task.setOnSucceeded(event -> {
                if (!task.getValue()) {

                    showDatabaseErrorAlert(root);

                }
            });


            new Thread(task).start();
        }
    }

    @FXML
    void handleEdit() {

        TreeItem<Patient> selectedPatient = patientsTableView.getSelectionModel().getSelectedItem();

        if (selectedPatient == null) {
            showSelectionErrorAlert(root);
        } else {

            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource(PATIENT_DIALOG_LOCATION), getResources());
                Parent patientDialog = loader.load();
                PatientDialogController controller = loader.getController();
                controller.setupEditDialog(selectedPatient.getValue());

                AnchorPane containerRoot = createNewView(900, 750, patientDialog);
                createNewStage(containerRoot, root);

            } catch (IOException e){
                System.err.println("PatientController - handleAdd(): " + e.getMessage());
            }
        }


    }
}
