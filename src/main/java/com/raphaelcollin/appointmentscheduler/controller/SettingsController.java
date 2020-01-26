package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.*;
import com.raphaelcollin.appointmentscheduler.ApplicationPreferences;
import com.raphaelcollin.appointmentscheduler.DatabaseCredentials;
import com.raphaelcollin.appointmentscheduler.UserCredentials;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.ApplicationPreferences.*;
import static com.raphaelcollin.appointmentscheduler.Main.*;

public class SettingsController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label titleLabel;
    @FXML
    private HBox containerHBox;
    @FXML
    private JFXComboBox<String> languageField;
    @FXML
    private JFXCheckBox accessControlCheckBox;
    @FXML
    private JFXTextField userField;
    @FXML
    private JFXPasswordField userPasswordField;
    @FXML
    private JFXPasswordField confirmPasswordField;
    @FXML
    private Label dbSectionTitleLabel;
    @FXML
    private JFXTextField ipField;
    @FXML
    private JFXTextField portField;
    @FXML
    private JFXTextField dbUserField;
    @FXML
    private JFXPasswordField dbPasswordField;
    @FXML
    private JFXButton applyButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double width = 1000;
        double height = 800;

        root.setMinSize(width, height);
        root.setPrefSize(width, height);
        root.setMinSize(width, height);

        titleLabel.setFont(Font.font(32));

        AnchorPane.setTopAnchor(titleLabel, 25.0);

        containerHBox.setSpacing(50);

        ObservableList<Node> children = containerHBox.getChildren();

        for (int i = 0; i < children.size(); i++) {
            GridPane gridPane = (GridPane) children.get(i);
            gridPane.setPrefWidth(500);
            gridPane.setHgap(20);
            gridPane.setVgap(30);

            if (i == 0) {
                gridPane.setPadding(new Insets(0,0,0,50));
            } else {
                gridPane.setPadding(new Insets(0,50,0,0));
            }

            for (Node node : gridPane.getChildren()) {
                if (node instanceof Label) {
                    ((Label) node).setFont(Font.font(20));
                }
                if (node instanceof JFXTextField) {
                    ((JFXTextField) node).setFont(Font.font(18));
                }
                if (node instanceof JFXPasswordField) {
                    ((JFXPasswordField) node).setFont(Font.font(18));
                }
            }
        }

        languageField.setPrefSize(215, 25);
        languageField.setStyle("-fx-font-size: 17px");

        accessControlCheckBox.setFont(Font.font(18));
        accessControlCheckBox.getStyleClass().add(STYLE_CLASS_BLUE_CHECK_BOX);

        userField.disableProperty().bind(accessControlCheckBox.selectedProperty().not());
        userPasswordField.disableProperty().bind(accessControlCheckBox.selectedProperty().not());
        confirmPasswordField.disableProperty().bind(accessControlCheckBox.selectedProperty().not());


        dbSectionTitleLabel.setFont(Font.font(24));

        AnchorPane.setTopAnchor(containerHBox, 100.0);
        AnchorPane.setBottomAnchor(containerHBox, 100.0);

        applyButton.setFont(Font.font(20));
        applyButton.getStyleClass().add(STYLE_CLASS_BLUE_BUTTON);
        applyButton.setTextFill(Color.WHITE);

        AnchorPane.setRightAnchor(applyButton, 30.0);
        AnchorPane.setBottomAnchor(applyButton, 30.0);

        ObservableList<String> languages  = FXCollections.observableArrayList();
        languages.addAll(languageList.values());
        languageField.setItems(languages);

        String currentLanguage = ApplicationPreferences.getInstance().getPreferences().get(PREFERENCES_KEY_LANGUAGE, DEFAULT_LANGUAGE);

        languageField.getSelectionModel().select(languageList.get(currentLanguage));

        boolean accessControl = ApplicationPreferences.getInstance().getPreferences().getBoolean(PREFERENCES_KEY_ACCESS_CONTROL, false);
        accessControlCheckBox.setSelected(accessControl);

        if (accessControl) {
            UserCredentials credentials = UserCredentials.getSavedCredentials();
            userField.setText(credentials.getUser());
            userPasswordField.setText(credentials.getPassword());
            confirmPasswordField.setText(credentials.getPassword());
        }

        boolean databaseConfigured = ApplicationPreferences.getInstance().getPreferences().getBoolean(PREFERENCES_KEY_DB_SETUP, false);

        if (databaseConfigured) {
            DatabaseCredentials credentials = DatabaseCredentials.getSavedCredentials();
            ipField.setText(credentials.getIp());
            portField.setText(credentials.getPort());
            dbUserField.setText(credentials.getUser());
            dbPasswordField.setText(credentials.getPassword());
        }
    }

    @FXML
    void handleApply() {

        String language = languageField.getSelectionModel().getSelectedItem();

        for (String key : languageList.keySet()) {
            if (languageList.get(key).equals(language)) {
                language = key;
                break;
            }
        }

        boolean accessControl = accessControlCheckBox.isSelected();
        String user = userField.getText();
        String password = userPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        String ip = ipField.getText();
        String port = portField.getText();
        String dbUser = dbUserField.getText();
        String dbPassword = dbPasswordField.getText();

        boolean emptyError = false;
        boolean passwordMatchError = false;

        if ((accessControl && (user.isEmpty() || password.isEmpty())) ||
        ip.isEmpty() || port.isEmpty() || dbUser.isEmpty()) {
            emptyError = true;
        }

        if (!emptyError && !password.equals(confirmPassword)) {
            passwordMatchError = true;
        }

        if (emptyError) {
           showRequiredFieldsErrorAlert(root);

        } else if (passwordMatchError){
            showPasswordMatchErrorAlert(root);

        } else {

            ApplicationPreferences.getInstance().getPreferences().put(PREFERENCES_KEY_LANGUAGE, language);
            ApplicationPreferences.getInstance().getPreferences().putBoolean(PREFERENCES_KEY_ACCESS_CONTROL, accessControl);
            ApplicationPreferences.getInstance().getPreferences().put(PREFERENCES_KEY_ACCESS_CONTROL_USER, user);
            ApplicationPreferences.getInstance().getPreferences().put(PREFERENCES_KEY_ACCESS_CONTROL_PASSWORD, password);
            ApplicationPreferences.getInstance().getPreferences().put(PREFERENCES_KEY_IP, ip);
            ApplicationPreferences.getInstance().getPreferences().put(PREFERENCES_KEY_PORT, port);
            ApplicationPreferences.getInstance().getPreferences().put(PREFERENCES_KEY_DB_USER, dbUser);
            ApplicationPreferences.getInstance().getPreferences().put(PREFERENCES_KEY_DB_PASSWORD, dbPassword);

            showAlert(Alert.AlertType.INFORMATION, root,
                    ((Stage) root.getScene().getWindow()).getTitle(),
                    getResources().getString(BUNDLE_KEY_SETTING_ALERT_HEADER_TEXT),
                    getResources().getString(BUNDLE_KEY_SETTING_ALERT_CONTENT_TEXT));

        }
    }
}
