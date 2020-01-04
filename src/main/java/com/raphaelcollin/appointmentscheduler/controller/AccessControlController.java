package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.raphaelcollin.appointmentscheduler.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    private ResourceBundle resources;

    private static final String STYLE_CLASS_ORANGE_BUTTON = "orange-button";
    private static final String BUNDLE_KEY_ERROR_PASSWORD_MATCH = "access_control_error_password_match";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        double width = Main.getScreenWidth() * 0.3125;
        double height = Main.getScreenWidth() * 0.3125 * 0.85; // Maintain the aspect radio

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

        boolean errorFounded = false;
        String errorMessage = "";

        if (user.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || securityQuestion.isEmpty() ||
                answer.isEmpty()) {
            errorFounded = true;
            errorMessage = resources.getString(BUNDLE_KEY_ERROR_EMPTY_MESSAGE);
        }

        if (!errorFounded && !password.equals(confirmPassword)) {
            errorFounded = true;
            errorMessage = resources.getString(BUNDLE_KEY_ERROR_PASSWORD_MATCH);
        }

        if (errorFounded) {
            showAlert(Alert.AlertType.ERROR,
                    root,
                    resources.getString(Main.BUNDLE_KEY_ERROR_ALERT_TITLE),
                    resources.getString(Main.BUNDLE_KEY_ERROR_HEADER_TEXT),
                    errorMessage);
        } else {

            getPreferences().putBoolean(PREFERENCES_KEY_ACCESS_CONTROL, true);
            getPreferences().put(PREFERENCES_KEY_ACCESS_CONTROL_USER, user);
            getPreferences().put(PREFERENCES_KEY_ACCESS_CONTROL_PASSWORD, password);
            getPreferences().put(PREFERENCES_KEY_ACCESS_CONTROL_SECURITY_QUESTION, securityQuestion);
            getPreferences().put(PREFERENCES_KEY_ACCESS_CONTROL_ANSWER, answer);

            loadDashboard();
        }
    }

    @FXML
    void handleSkip() {
        loadDashboard();
    }

    private void loadDashboard() {

        Stage currentStage = (Stage) root.getScene().getWindow();
        currentStage.close();

        Stage newStage = new Stage();
        newStage.setTitle(resources.getString(BUNDLE_KEY_APPLICATION_TITLE));
        Parent dashboardRoot = loadView(LOCATION_DASHBOARD_VIEW, resources);
        if (dashboardRoot != null) {
            newStage.setScene(new Scene(dashboardRoot));
            newStage.show();
        }
    }
}