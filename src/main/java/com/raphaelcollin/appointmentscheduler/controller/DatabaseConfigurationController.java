package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.raphaelcollin.appointmentscheduler.ApplicationPreferences;
import com.raphaelcollin.appointmentscheduler.DatabaseCredentials;
import com.raphaelcollin.appointmentscheduler.Main;
import com.raphaelcollin.appointmentscheduler.db.ConnectionFactory;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.ApplicationPreferences.PREFERENCES_KEY_ACCESS_CONTROL;
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

    private static final String REGEX_PORT = "\\d+$";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        double width = 600;
        double height = 510;

        root.setPrefSize(width, height);
        root.setMinSize(width, height);
        root.setMaxSize(width, height);

        welcomeLabel.setFont(Font.font(32));
        AnchorPane.setTopAnchor(welcomeLabel, 15.0);

        dbConfigLabel.setFont(Font.font(24));
        dbConfigLabel.getStyleClass().add(STYLE_CLASS_CONFIGURATION_SUBTITLE);
        AnchorPane.setLeftAnchor(dbConfigLabel, 50.0);
        AnchorPane.setTopAnchor(dbConfigLabel, 90.0);

        AnchorPane.setTopAnchor(inputGridPane, 165.0);
        AnchorPane.setBottomAnchor(inputGridPane, 130.0);

        inputGridPane.setHgap(20);
        inputGridPane.setVgap(30);

        for (int i = 0; i < inputGridPane.getChildren().size(); i = i + 2) {
           ((Label) inputGridPane.getChildren().get(i)).setFont(Font.font(20));
        }

        AnchorPane.setBottomAnchor(testConnectionHBox, 40.0);

        testConnectionButton.setFont(Font.font(20));

        testConnectionButton.getStyleClass().add(STYLE_CLASS_CONFIGURATION_GREEN_BUTTON);
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
            errorMessage = getResources().getString(BUNDLE_KEY_ERROR_EMPTY_MESSAGE);
            errorFounded = true;
        }

        if (!errorFounded && !port.matches(REGEX_PORT)) {
            errorMessage = getResources().getString(BUNDLE_KEY_ERROR_PORT_INVALID_MESSAGE);
            errorFounded = true;
        }

        if (errorFounded) {
            Main.showAlert(Alert.AlertType.ERROR, root,
                    getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                    getResources().getString(BUNDLE_KEY_ERROR_HEADER_TEXT),
                    errorMessage);

        } else {

            Task<Connection> testConnectionTask = new Task<Connection>() {
                @Override
                protected Connection call() {
                    return testConnection(ipAddress, port, user, password);
                }
            };

            testConnectionTask.setOnSucceeded(event -> {
                root.setCursor(Cursor.DEFAULT);
                setConnection(testConnectionTask.getValue());
                if (getConnection() == null) {
                    Main.showAlert(Alert.AlertType.ERROR, root,
                            getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                            getResources().getString(BUNDLE_KEY_CONNECTION_ERROR_HEADER_TEXT),
                            getResources().getString(BUNDLE_KEY_CONNECTION_ERROR_CONTENT_TEXT));
                } else {

                    DatabaseCredentials.saveCredentials(ipAddress, port, user, password);

                    boolean accessControlDefined = ApplicationPreferences.getInstance().getPreferences().getBoolean(PREFERENCES_KEY_ACCESS_CONTROL, false);

                    if (accessControlDefined) {
                        ((Stage) root.getScene().getWindow()).close();
                        Main.createMainViewStage();
                    }

                    AnchorPane containerRoot = (AnchorPane) root.getScene().getRoot();
                    AnchorPane inRoot = null;

                    try {
                        inRoot = FXMLLoader.load(getClass().getResource(ACCESS_CONTROL_CONFIGURATION_LOCATION), getResources());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (containerRoot != null && inRoot != null) {
                        switchScenes(containerRoot, root, inRoot, TRANSITION_FROM_RIGHT);
                    }
                }
            });

            root.setCursor(Cursor.WAIT);
            new Thread(testConnectionTask).start();

        }
    }

    public Connection testConnection(String ipAddress, String port, String user, String password) {
        DatabaseCredentials credentials = new DatabaseCredentials(ipAddress, port, user, password);
        return ConnectionFactory.getConnection(credentials);
    }

    public void setupFields(String ipAddress, String port, String user, String password) {

        welcomeLabel.setText(getResources().getString(BUNDLE_KEY_CONNECTION_ERROR_LABEL));

        ((JFXTextField) inputGridPane.getChildren().get(1)).setText(ipAddress);
        ((JFXTextField) inputGridPane.getChildren().get(3)).setText(port);
        ((JFXTextField) inputGridPane.getChildren().get(5)).setText(user);
        ((JFXPasswordField) inputGridPane.getChildren().get(7)).setText(password);

        showAlert(Alert.AlertType.ERROR, root,
                getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                getResources().getString(BUNDLE_KEY_CONNECTION_ERROR_HEADER_TEXT),
                getResources().getString(BUNDLE_KEY_CONNECTION_ERROR_CONTENT_TEXT2));
    }
}
