package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.raphaelcollin.appointmentscheduler.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class ContainerLoginController implements Initializable {

    @FXML
    private JFXButton minimizeButton;
    @FXML
    private JFXButton closeButton;
    @FXML
    private AnchorPane root;

    private double xOffset;
    private double yOffSet;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double width = Main.getScreenWidth() * 0.3125;
        double height = Main.getScreenWidth() * 0.3125 * 0.666666; // Maintain the aspect radio

        root.setPrefSize(width, height);
        root.setMinSize(width, height);
        root.setMaxSize(width, height);

        AnchorPane loginRoot = loadView(LOGIN_LOCATION, resources);

        root.getChildren().add(loginRoot);
        AnchorPane.setTopAnchor(loginRoot, 40.0);

        FontAwesomeIconView closeIcon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        closeIcon.getStyleClass().add(STYLE_CLASS_CLOSE_ICON);

        closeButton.setGraphic(closeIcon);
        closeButton.getStyleClass().add(STYLE_CLASS_CLOSE_BUTTON);

        FontAwesomeIconView minimizeIcon = new FontAwesomeIconView(FontAwesomeIcon.MINUS);
        minimizeIcon.getStyleClass().add(STYLE_CLASS_MINIMIZE_ICON);

        minimizeButton.setGraphic(minimizeIcon);
        minimizeButton.setContentDisplay(ContentDisplay.CENTER);
        minimizeButton.getStyleClass().add(STYLE_CLASS_MINIMIZE_BUTTON);
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
