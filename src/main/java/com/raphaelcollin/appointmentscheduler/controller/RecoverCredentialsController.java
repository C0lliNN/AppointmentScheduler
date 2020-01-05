package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
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

    private String correctAnswer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        double width = 600;
        double height = 350;

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

        recoverButton.getStyleClass().add(STYLE_CLASS_GREEN_BUTTON);
        recoverButton.setFont(Font.font(20));

        skipButton.getStyleClass().add(STYLE_CLASS_ORANGE_BUTTON);
        skipButton.setFont(Font.font(20));

        String securityQuestion = Main.getPreferences().get(PREFERENCES_KEY_ACCESS_CONTROL_SECURITY_QUESTION, null);
        securityQuestionField.setText(securityQuestion);

        correctAnswer = Main.getPreferences().get(PREFERENCES_KEY_ACCESS_CONTROL_ANSWER, null);
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

            if (!enteredAnswer.equalsIgnoreCase(correctAnswer)) {

                showAlert(Alert.AlertType.ERROR, root,
                        getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                        getResources().getString(BUNDLE_KEY_ERROR_HEADER_TEXT),
                        getResources().getString(BUNDLE_KEY_ERROR_INCORRECT_ANSWER));

            } else {

                String user = getPreferences().get(PREFERENCES_KEY_ACCESS_CONTROL_USER, null);
                String password = getPreferences().get(PREFERENCES_KEY_ACCESS_CONTROL_PASSWORD, null);

                assert user != null && password != null;

                userField.setText(user);
                passwordField.setText(password);

                recoverButton.setDisable(true);
            }
        }
    }

    @FXML
    void handleBack() {

        Parent loginRoot = loadView(LOGIN_LOCATION, getResources());

        assert loginRoot != null;

        AnchorPane containerRoot = (AnchorPane) root.getScene().getRoot();
        switchScenes(containerRoot, root, loginRoot, TRANSITION_FROM_LEFT);

    }
}