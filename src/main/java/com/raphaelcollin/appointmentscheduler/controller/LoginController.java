package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.raphaelcollin.appointmentscheduler.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class LoginController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label loginLabel;
    @FXML
    private GridPane inputGridPane;
    @FXML
    private Label userLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private JFXTextField userField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private Label recoverCredentialsLabel;
    @FXML
    private HBox buttonHBox;
    @FXML
    private JFXButton enterButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        double width = 600;
        double height = 350;

        root.setPrefSize(width,height);
        root.setMinSize(width,height);
        root.setMaxSize(width,height);

        loginLabel.setFont(Font.font(32));
        loginLabel.getStyleClass().add(Main.STYLE_CLASS_CONFIGURATION_SUBTITLE);
        AnchorPane.setTopAnchor(loginLabel, 15.0);

        inputGridPane.setHgap(20);
        inputGridPane.setVgap(30);
        AnchorPane.setTopAnchor(inputGridPane, 91.0);

        userLabel.setFont(Font.font(24));
        passwordLabel.setFont(Font.font(24));

        userField.setFont(Font.font(18));
        passwordField.setFont(Font.font(18));

        AnchorPane.setTopAnchor(recoverCredentialsLabel, 229.0);
        AnchorPane.setLeftAnchor(recoverCredentialsLabel, 138.0);

        AnchorPane.setBottomAnchor(buttonHBox, 30.0);

        enterButton.setFont(Font.font(22));
        enterButton.getStyleClass().add(Main.STYLE_CLASS_GREEN_BUTTON);

        recoverCredentialsLabel.setId(ID_RECOVER_CREDENTIALS_LABEL);
    }

    @FXML
    void handleEnter() {

        String enterUser = userField.getText().trim();
        String enterPassword = passwordField.getText().trim();

        if (enterUser.isEmpty() || enterPassword.isEmpty()) {

            showAlert(Alert.AlertType.ERROR, root,
                    getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                    getResources().getString(BUNDLE_KEY_ERROR_HEADER_TEXT),
                    getResources().getString(BUNDLE_KEY_ERROR_EMPTY_MESSAGE));

        } else {

            String storedUser = getPreferences().get(PREFERENCES_KEY_ACCESS_CONTROL_USER, null);
            String storedPassword = getPreferences().get(PREFERENCES_KEY_ACCESS_CONTROL_PASSWORD, null);

            if (!storedUser.equals(enterUser) || !storedPassword.equals(enterPassword)) {

                showAlert(Alert.AlertType.ERROR, root,
                        getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                        getResources().getString(BUNDLE_KEY_ERROR_HEADER_TEXT),
                        getResources().getString(BUNDLE_KEY_INVALID_CREDENTIALS_MESSAGE));

            } else {

                Stage currentStage = (Stage) root.getScene().getWindow();
                currentStage.close();

                loadDashboardStage();

            }
        }
    }

    @FXML
    void handleRecoverCredentials() {
        Parent recoverCredentialsRoot = loadView(RECOVER_CREDENTIALS_LOCATION, getResources());

        assert recoverCredentialsRoot != null;

        AnchorPane containerRoot = (AnchorPane) root.getScene().getRoot();

        switchScenes(containerRoot, root, recoverCredentialsRoot, TRANSITION_FROM_RIGHT);
    }
}
