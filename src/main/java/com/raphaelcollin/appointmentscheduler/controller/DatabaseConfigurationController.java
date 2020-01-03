package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.raphaelcollin.appointmentscheduler.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class DatabaseConfigurationController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label dbConfigLabel;
    @FXML
    private GridPane inputGridPane;
    @FXML
    private HBox testConnectionHBox;
    @FXML
    private JFXTextField ipAddressField;
    @FXML
    private JFXTextField portField;
    @FXML
    private JFXTextField userField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXButton testConnectionButton;

    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.resources = resources;

        double width = Main.getScreenWidth() * 0.3125;
        double height = Main.getScreenWidth() * 0.3125 * 0.85; // Maintain the aspect radio

        root.setPrefSize(width, height);
        root.setMinSize(width, height);
        root.setMaxSize(width, height);

        welcomeLabel.setFont(Font.font(32));
        AnchorPane.setTopAnchor(welcomeLabel, 25.0);

        dbConfigLabel.setFont(Font.font(24));
        AnchorPane.setLeftAnchor(dbConfigLabel, 50.0);
        AnchorPane.setTopAnchor(dbConfigLabel, 100.0);

        AnchorPane.setTopAnchor(inputGridPane, 175.0);
        AnchorPane.setBottomAnchor(inputGridPane, 120.0);

        inputGridPane.setHgap(20);
        inputGridPane.setVgap(30);

        for (int i = 0; i < inputGridPane.getChildren().size(); i = i + 2) {

           ((Label) inputGridPane.getChildren().get(i)).setFont(Font.font(20));

        }

        AnchorPane.setBottomAnchor(testConnectionHBox, 20.0);

        testConnectionButton.setFont(Font.font(20));
    }

    @FXML
    private void handleTestConnection() {

        String ipAddress = ipAddressField.getText().trim();
        String port = portField.getText().trim();
        String user = userField.getText().trim();
        String password = passwordField.getText().trim();

        String errorMessage = "";
        boolean errorFounded = false;

        if (ipAddress.isEmpty() || port.isEmpty() || user.isEmpty()) {
            errorMessage = resources.getString("database_configuration_errorMessage_empty");
            errorFounded = true;
        }

        if (!errorFounded && !ipAddress.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")) {
            errorMessage = resources.getString("database_configuration_errorMessage_invalidIP");
        }

        if (!errorFounded && !port.matches("\\d$")) {
            errorMessage = resources.getString("database_configuration_errorMessage_invalidPort");
        }

        if (errorFounded) {
            Main.showAlert(Alert.AlertType.ERROR, root, "Invalid Input", errorMessage);

        } else {



        }


    }
}
