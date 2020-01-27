package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.raphaelcollin.appointmentscheduler.db.DataSource;
import com.raphaelcollin.appointmentscheduler.db.model.Doctor;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
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
import static com.raphaelcollin.appointmentscheduler.db.DataSource.DOCTORS_CHANGE;
import static com.raphaelcollin.appointmentscheduler.db.DataSource.INITIAL_DATA_LOADED;

public class DoctorController implements Initializable, PropertyChangeListener {

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
    private JFXTreeTableView<Doctor> doctorsTableView;
    @FXML
    private TreeTableColumn<Doctor, String> nameColumn;
    @FXML
    private TreeTableColumn<Doctor, String> genderColumn;
    @FXML
    private TreeTableColumn<Doctor, String> birthDateColumn;
    @FXML
    private TreeTableColumn<Doctor, String> phoneColumn;
    @FXML
    private TreeTableColumn<Doctor, String> licenseNumberColumn;

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

        searchField.textProperty().addListener(((observable, oldValue, newValue) -> doctorsTableView.setPredicate(doctor -> newValue.trim().isEmpty() ||
                doctor.getValue().getName().toLowerCase().contains(newValue.toLowerCase()) ||
                doctor.getValue().getGender().toLowerCase().contains(newValue.toLowerCase()) ||
                doctor.getValue().getPhoneNumber().contains(newValue) ||
                doctor.getValue().getLicenseNumber().contains(newValue))));

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


        AnchorPane.setTopAnchor(doctorsTableView, 130.0);
        nameColumn.setCellValueFactory(param -> param.getValue().getValue().getNameProperty());
        genderColumn.setCellValueFactory(param -> param.getValue().getValue().getGenderProperty());
        birthDateColumn.setCellValueFactory(param -> param.getValue().getValue().getBirthDateProperty());
        phoneColumn.setCellValueFactory(param -> param.getValue().getValue().getPhoneProperty());
        licenseNumberColumn.setCellValueFactory(param -> param.getValue().getValue().getLicenseNumberProperty());

        DataSource.getInstance().addObserver(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(INITIAL_DATA_LOADED)) {
            ObservableList<Doctor> doctors = DataSource.getInstance().getDoctors();

            final RecursiveTreeItem<Doctor> root = new RecursiveTreeItem<>(doctors, RecursiveTreeObject::getChildren);
            Platform.runLater(() -> {
                doctorsTableView.setRoot(root);
                doctorsTableView.getSortOrder().add(nameColumn);
            });
        }

        if (evt.getPropertyName().equals(DOCTORS_CHANGE)) {
            Platform.runLater(() -> doctorsTableView.getSortOrder().add(nameColumn));

        }
    }

    @FXML
    void handleAdd() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(DOCTOR_DIALOG_LOCATION), getResources());
            Parent patientDialog = loader.load();
            DoctorDialogController controller = loader.getController();
            controller.setupAddDialog();

            AnchorPane containerRoot = createNewView(710, 560, patientDialog);
            createNewStage(containerRoot, root);

        } catch (IOException e){
            System.err.println("PatientController - handleAdd(): " + e.getMessage());
        }
    }

    @FXML
    void handleEdit() {

        TreeItem<Doctor> selectedDoctor = doctorsTableView.getSelectionModel().getSelectedItem();

        if (selectedDoctor == null) {
            showSelectionErrorAlert(root);
        } else {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource(DOCTOR_DIALOG_LOCATION), getResources());
                Parent patientDialog = loader.load();
                DoctorDialogController controller = loader.getController();
                controller.setupEditDialog(selectedDoctor.getValue());

                AnchorPane containerRoot = createNewView(710, 560, patientDialog);
                createNewStage(containerRoot, root);

            } catch (IOException e){
                System.err.println("PatientController - handleAdd(): " + e.getMessage());
            }
        }

    }

    @FXML
    void handleDelete() {

        TreeItem<Doctor> selectedDoctor = doctorsTableView.getSelectionModel().getSelectedItem();

        if (selectedDoctor == null) {
            showSelectionErrorAlert(root);
        } else {

            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call()  {
                    return DataSource.getInstance().deleteDoctor(selectedDoctor.getValue().getId());
                }
            };

            task.setOnSucceeded(event -> {
                root.setCursor(Cursor.DEFAULT);
                if (!task.getValue()) {
                    showDatabaseErrorAlert(root);
                }
            });

            root.setCursor(Cursor.WAIT);
            new Thread(task).start();
        }

    }
}
