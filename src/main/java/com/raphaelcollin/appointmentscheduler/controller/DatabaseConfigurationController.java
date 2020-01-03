package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.raphaelcollin.appointmentscheduler.Main;
import com.raphaelcollin.appointmentscheduler.db.ConnectionFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

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

    private static final String BUNDLE_KEY_ERROR_EMPTY_MESSAGE = "database_configuration_errorMessage_empty";
    private static final String REGEX_IP = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$";
    private static final String BUNDLE_KEY_ERROR_IP_INVALID_MESSAGE = "database_configuration_errorMessage_invalidIP";
    private static final String REGEX_PORT = "\\d+$";
    private static final String BUNDLE_KEY_ERROR_PORT_INVALID_MESSAGE = "database_configuration_errorMessage_invalidPort";
    private static final String BUNDLE_KEY_ERROR_HEADER_TEXT = "database_configuration_errorMessage_headerText";
    private static final String BUNDLE_KEY_CONNECTION_ERROR_HEADER_TEXT = "database_configuration_errorMessage2_headerText";
    private static final String BUNDLE_KEY_CONNECTION_ERROR_CONTENT_TEXT = "database_configuration_errorMessage2_contentText";


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
            errorMessage = resources.getString(BUNDLE_KEY_ERROR_EMPTY_MESSAGE);
            errorFounded = true;
        }

        if (!errorFounded && !ipAddress.matches(REGEX_IP)) {
            errorMessage = resources.getString(BUNDLE_KEY_ERROR_IP_INVALID_MESSAGE);
            errorFounded = true;
        }

        if (!errorFounded && !port.matches(REGEX_PORT)) {
            errorMessage = resources.getString(BUNDLE_KEY_ERROR_PORT_INVALID_MESSAGE);
            errorFounded = true;
        }

        if (errorFounded) {
            Main.showAlert(Alert.AlertType.ERROR, root, resources.getString(BUNDLE_KEY_ERROR_HEADER_TEXT), errorMessage);

        } else {

            Connection connection = testConnection(ipAddress, port, user, password);

            if (connection == null) {
                Main.showAlert(Alert.AlertType.ERROR, root,
                        resources.getString(BUNDLE_KEY_CONNECTION_ERROR_HEADER_TEXT),
                        resources.getString(BUNDLE_KEY_CONNECTION_ERROR_CONTENT_TEXT));
            } else {

                preferences.put(PREFERENCES_KEY_IP, ipAddress);
                preferences.put(PREFERENCES_KEY_PORT, port);
                preferences.put(PREFERENCES_KEY_USER, user);
                preferences.put(PREFERENCES_KEY_PASSWORD, password);

                // Switch Scenes, constants

            }
        }
    }

    // This method improves software testability
    public Connection testConnection(String ipAddress, String port, String user, String password) {
        return ConnectionFactory.getConnection(ipAddress, port, user, password);
    }
}
