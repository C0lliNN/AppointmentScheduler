package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.raphaelcollin.appointmentscheduler.Main;
import com.raphaelcollin.appointmentscheduler.UserCredentials;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class AccessControlController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label accessControlLabel;
    @FXML
    private Label loginLabel;
    @FXML
    private GridPane loginGridPane;
    @FXML
    private JFXTextField userField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXPasswordField confirmPasswordField;
    @FXML
    private Label recoverLabel;
    @FXML
    private GridPane recoverGridPane;
    @FXML
    private JFXTextField securityQuestionField;
    @FXML
    private JFXTextField answerField;
    @FXML
    private HBox buttonsHBox;
    @FXML
    private JFXButton skipButton;
    @FXML
    private JFXButton saveButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        double width = 600;
        double height = 510;

        root.setPrefSize(width, height);
        root.setMinSize(width, height);
        root.setMaxSize(width, height);

        accessControlLabel.setFont(Font.font(32));
        AnchorPane.setTopAnchor(accessControlLabel, 15.0);

        loginLabel.setFont(Font.font(22));
        loginLabel.getStyleClass().add(STYLE_CLASS_CONFIGURATION_SUBTITLE);

        AnchorPane.setTopAnchor(loginLabel, 65.0);
        AnchorPane.setLeftAnchor(loginLabel, 50.0);

        loginGridPane.setHgap(20);
        loginGridPane.setVgap(15);
        loginGridPane.setPadding(new Insets(20,80,20,80));

        for (int i = 0; i < 3; i++) {
            ((Label) loginGridPane.getChildren().get(i)).setFont(Font.font(18));
        }

        AnchorPane.setTopAnchor(loginGridPane, 95.0);

        recoverLabel.setFont(Font.font(22));
        recoverLabel.getStyleClass().add(STYLE_CLASS_CONFIGURATION_SUBTITLE);

        AnchorPane.setTopAnchor(recoverLabel, 265.0);
        AnchorPane.setLeftAnchor(recoverLabel, 50.0);

        recoverGridPane.setHgap(20);
        recoverGridPane.setVgap(15);
        recoverGridPane.setPadding(new Insets(20,80,20,80));

        for (int i = 0; i < 2; i++) {
            ((Label) recoverGridPane.getChildren().get(i)).setFont(Font.font(18));
        }

        AnchorPane.setTopAnchor(recoverGridPane, 300.0);

        buttonsHBox.setSpacing(50);
        buttonsHBox.getChildren().forEach(node -> ((Button) node).setFont(Font.font(20)));

        AnchorPane.setBottomAnchor(buttonsHBox, 35.0);

        saveButton.getStyleClass().add(Main.STYLE_CLASS_CONFIGURATION_GREEN_BUTTON);
        skipButton.getStyleClass().add(STYLE_CLASS_ORANGE_BUTTON);
    }

    @FXML
    void handleSave() {

        String user = userField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String securityQuestion = securityQuestionField.getText().trim();
        String answer = answerField.getText().trim();

        boolean emptyFieldsError = false;
        boolean passwordMatchError = false;
        String errorMessage = "";

        if (user.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || securityQuestion.isEmpty() ||
                answer.isEmpty()) {
            emptyFieldsError = true;
        }

        if (!emptyFieldsError && !password.equals(confirmPassword)) {
            passwordMatchError = true;
            errorMessage = getResources().getString(BUNDLE_KEY_ERROR_PASSWORD_MATCH);
        }

        if (emptyFieldsError) {

            showRequiredFieldsErrorAlert(root);

        } else if (passwordMatchError) {

            showAlert(Alert.AlertType.ERROR,
                    root,
                    getResources().getString(Main.BUNDLE_KEY_ERROR_ALERT_TITLE),
                    getResources().getString(Main.BUNDLE_KEY_ERROR_INVALID_INPUT),
                    errorMessage);

        } else {

            UserCredentials.saveCredentials(user, password, securityQuestion, answer);
            goToMainView();
        }
    }

    @FXML
    void handleSkip() {
        goToMainView();
    }

    private void goToMainView(){

        Stage currentStage = (Stage) root.getScene().getWindow();
        currentStage.close();

        createMainViewStage();
    }
}