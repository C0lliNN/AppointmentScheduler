package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class ContainerController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView appIconImageView;
    @FXML
    private Label applicationTitleLabel;
    @FXML
    private JFXButton minimizeButton;
    @FXML
    private JFXButton closeButton;

    private double xOffset;
    private double yOffSet;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        root.setId("container-root");

        appIconImageView.setImage(new Image(getClass().getResourceAsStream(APPLICATION_ICON_TITLE_BAR_LOCATION)));
        appIconImageView.setFitWidth(30);
        appIconImageView.setFitHeight(30);

        AnchorPane.setTopAnchor(appIconImageView, 6.0);
        AnchorPane.setLeftAnchor(appIconImageView, 8.0);

        applicationTitleLabel.setFont(Font.font(20));
        applicationTitleLabel.setId(ID_APPLICATION_TITLE_LABEL);

        AnchorPane.setTopAnchor(applicationTitleLabel, 5.0);
        AnchorPane.setLeftAnchor(applicationTitleLabel, 100.0);
        AnchorPane.setRightAnchor(applicationTitleLabel, 100.0);

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

    public void setSize(double width, double height) {
        root.setMinSize(width, height);
        root.setPrefSize(width, height);
        root.setMaxSize(width, height);
    }

    public void addChild(Parent parent) {
        root.getChildren().add(parent);
        AnchorPane.setTopAnchor(parent, 40.0);
        AnchorPane.setLeftAnchor(parent, 4.0);
        AnchorPane.setRightAnchor(parent, 4.0);
        AnchorPane.setBottomAnchor(parent, 4.0);
    }
}
