package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.raphaelcollin.appointmentscheduler.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

/* This Container was created for allow transition between scenes */

public class ContainerConfigurationController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private JFXButton minimizeButton;
    @FXML
    private JFXButton closeButton;

    private double xOffset;
    private double yOffSet;

    @Override
    public void initialize(URL location, ResourceBundle resources){


        double width = Main.getScreenWidth() * 0.3125;
        double height = Main.getScreenWidth() * 0.3125 * 0.9166666; // Maintain the aspect radio

        root.setPrefSize(width, height);
        root.setMinSize(width, height);
        root.setMaxSize(width, height);

        try {
            AnchorPane databaseConfigurationRoot = FXMLLoader.load(getClass().getResource(DATABASE_CONFIGURATION_LOCATION), resources);

            root.getChildren().add(databaseConfigurationRoot);
            AnchorPane.setTopAnchor(databaseConfigurationRoot, 40.0);

            FontAwesomeIconView closeIcon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
            closeIcon.getStyleClass().add(STYLE_CLASS_CLOSE_ICON);

            closeButton.setGraphic(closeIcon);
            closeButton.getStyleClass().add(STYLE_CLASS_CLOSE_BUTTON);

            FontAwesomeIconView minimizeIcon = new FontAwesomeIconView(FontAwesomeIcon.MINUS);
            minimizeIcon.getStyleClass().add(STYLE_CLASS_MINIMIZE_ICON);

            minimizeButton.setGraphic(minimizeIcon);
            minimizeButton.setContentDisplay(ContentDisplay.CENTER);
            minimizeButton.getStyleClass().add(STYLE_CLASS_MINIMIZE_BUTTON);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void setupFields(String ipAddress, String port, String user, String password) {

        ((Label) ((AnchorPane) root.getChildren().get(1)).getChildren().get(0)).setText(
                getResources().getString(BUNDLE_KEY_CONNECTION_ERROR_LABEL));

        GridPane pane = ((GridPane) ((AnchorPane) root.getChildren().get(1)).getChildren().get(2));

        ((JFXTextField) pane.getChildren().get(1)).setText(ipAddress);
        ((JFXTextField) pane.getChildren().get(3)).setText(port);
        ((JFXTextField) pane.getChildren().get(5)).setText(user);
        ((JFXPasswordField) pane.getChildren().get(7)).setText(password);

        showAlert(Alert.AlertType.ERROR, root,
                getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                getResources().getString(BUNDLE_KEY_CONNECTION_ERROR_HEADER_TEXT),
                getResources().getString(BUNDLE_KEY_CONNECTION_ERROR_CONTENT_TEXT2));
    }

    @FXML
    private void handleMinimizeWindow() {
        ((Stage) root.getScene().getWindow()).setIconified(true);
    }

    @FXML
    private void handleCloseWindow() {
        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    private void handleMousePressed(MouseEvent mouseEvent) {
        Stage stage = (Stage) root.getScene().getWindow();
        xOffset = stage.getX() - mouseEvent.getScreenX();
        yOffSet = stage.getY() - mouseEvent.getScreenY();
    }

    @FXML
    private void handleMouseDragged(MouseEvent mouseEvent) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() + xOffset);
        stage.setY(mouseEvent.getScreenY() + yOffSet);
    }
}
