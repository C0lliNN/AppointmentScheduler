package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.raphaelcollin.appointmentscheduler.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BaseConfigurationController implements Initializable {
    @FXML
    private AnchorPane root;
    @FXML
    private JFXButton minimizeButton;
    @FXML
    private JFXButton closeButton;

    private double xOffset;
    private double yOffSet;

    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.resources = resources;

        double width = Main.getScreenWidth() * 0.3125;
        double height = Main.getScreenWidth() * 0.3125 * 0.9166666; // Maintain the aspect radio

        root.setPrefSize(width, height);
        root.setMinSize(width, height);
        root.setMaxSize(width, height);

        AnchorPane databaseConfigurationRoot = loadView("/database_configuration.fxml");

        root.getChildren().add(databaseConfigurationRoot);
        AnchorPane.setTopAnchor(databaseConfigurationRoot, 25.0);

        FontAwesomeIconView closeIcon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        closeIcon.getStyleClass().add("close-icon");

        closeButton.setGraphic(closeIcon);
        closeButton.getStyleClass().add("title-button");

        FontAwesomeIconView minimizeIcon = new FontAwesomeIconView(FontAwesomeIcon.MINUS);
        minimizeIcon.getStyleClass().add("minimize-icon");

        minimizeButton.setGraphic(minimizeIcon);
        minimizeButton.setContentDisplay(ContentDisplay.CENTER);
        minimizeButton.getStyleClass().add("title-button");

        // Dropshadow, ControlFlow, switch scenes, constants
    }

    @FXML
    private void handleMinimizeWindow() {
        ((Stage) root.getScene().getWindow()).setIconified(true);
    }

    @FXML
    private void handleCloseWindow() {
        ((Stage) root.getScene().getWindow()).close();
    }

    private AnchorPane loadView(String location){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(location), resources);
        try {
            return loader.load();
        } catch (IOException e) {
            return null;
        }
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
