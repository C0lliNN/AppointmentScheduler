package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.raphaelcollin.appointmentscheduler.UserCredentials;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class RecoverCredentialsController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label recoverCredentialsLabel;
    @FXML
    private GridPane inputGridPane;
    @FXML
    private JFXTextField securityQuestionField;
    @FXML
    private JFXTextField answerField;
    @FXML
    private JFXTextField userField;
    @FXML
    private JFXTextField passwordField;
    @FXML
    private HBox buttonHBox;
    @FXML
    private JFXButton skipButton;
    @FXML
    private JFXButton recoverButton;

    private UserCredentials credentials;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        double width = 598;
        double height = 355;

        root.setPrefSize(width, height);
        root.setMinSize(width, height);
        root.setMaxSize(width, height);

        recoverCredentialsLabel.setFont(Font.font(32));
        recoverCredentialsLabel.getStyleClass().add(STYLE_CLASS_CONFIGURATION_SUBTITLE);
        AnchorPane.setTopAnchor(recoverCredentialsLabel, 15.0);

        inputGridPane.setHgap(20);
        inputGridPane.setVgap(10);

        int i = 0;

        for (;i < 4; i++) {
            ((Label) inputGridPane.getChildren().get(i)).setFont(Font.font(18));
        }

        for (; i < 8; i++) {
            ((JFXTextField) inputGridPane.getChildren().get(i)).setFont(Font.font(16));
        }

        AnchorPane.setTopAnchor(inputGridPane, 80.0);

        buttonHBox.setSpacing(30);
        AnchorPane.setBottomAnchor(buttonHBox, 25.0);

        recoverButton.getStyleClass().add(STYLE_CLASS_CONFIGURATION_GREEN_BUTTON);
        recoverButton.setFont(Font.font(20));

        skipButton.getStyleClass().add(STYLE_CLASS_ORANGE_BUTTON);
        skipButton.setFont(Font.font(20));

    }

    @FXML
    void handleRecover() {

        String enteredAnswer = answerField.getText().trim();

        if (enteredAnswer.isEmpty()) {

            showAlert(Alert.AlertType.ERROR, root,
                    getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                    getResources().getString(BUNDLE_KEY_ERROR_HEADER_TEXT),
                    getResources().getString(BUNDLE_KEY_ERROR_EMPTY_MESSAGE));

        } else {

            if (!enteredAnswer.equalsIgnoreCase(credentials.getAnswer())) {

                showAlert(Alert.AlertType.ERROR, root,
                        getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                        getResources().getString(BUNDLE_KEY_ERROR_HEADER_TEXT),
                        getResources().getString(BUNDLE_KEY_ERROR_INCORRECT_ANSWER));

            } else {

                userField.setText(credentials.getUser());
                passwordField.setText(credentials.getPassword());

                recoverButton.setDisable(true);
            }
        }
    }

    @FXML
    void handleBack() {

        Parent loginRoot = null;
        try {
            loginRoot = FXMLLoader.load(getClass().getResource(LOGIN_LOCATION), getResources());
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert loginRoot != null;

        AnchorPane containerRoot = (AnchorPane) root.getScene().getRoot();
        switchScenes(containerRoot, root, loginRoot, TRANSITION_FROM_LEFT);

    }

    void setUserCredentials(UserCredentials credentials) {
        this.credentials = credentials;
        securityQuestionField.setText(credentials.getSecurityQuestion());
    }
}