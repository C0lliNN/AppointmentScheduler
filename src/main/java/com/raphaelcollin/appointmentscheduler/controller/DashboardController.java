package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class DashboardController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView imageView;
    @FXML
    private TabPane tabPane;
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

        // Title Bar, Wireframes

        double width = 1208;
        double height = 845;

        root.setPrefSize(width, height);
        root.setMinSize(width, height);
        root.setMaxSize(width, height);

        imageView.setImage(new Image(getClass().getResourceAsStream(LOCATION_APPLICATION_ICON_TITLE_BAR)));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        applicationTitleLabel.setFont(Font.font(20));
        applicationTitleLabel.setId(ID_APPLICATION_TITLE_LABEL);

        FontAwesomeIconView closeIcon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        closeIcon.getStyleClass().add(STYLE_CLASS_CLOSE_ICON);

        closeButton.setGraphic(closeIcon);
        closeButton.getStyleClass().add(STYLE_CLASS_CLOSE_BUTTON);

        FontAwesomeIconView minimizeIcon = new FontAwesomeIconView(FontAwesomeIcon.MINUS);
        minimizeIcon.getStyleClass().add(STYLE_CLASS_MINIMIZE_ICON);

        minimizeButton.setGraphic(minimizeIcon);
        minimizeButton.setContentDisplay(ContentDisplay.CENTER);
        minimizeButton.getStyleClass().add(STYLE_CLASS_MINIMIZE_BUTTON);

        FontAwesomeIconView dashBoardIcon = new FontAwesomeIconView(FontAwesomeIcon.DASHBOARD);
        dashBoardIcon.getStyleClass().add(STYLE_CLASS_TAB_ICON);

        Tab dashboardTab = new Tab();
        dashboardTab.setGraphic(createTabHeader(
                resources.getString(BUNDLE_KEY_TAB_TITLE_DASHBOARD),
                dashBoardIcon));

        HBox box = new HBox();
        box.setStyle("-fx-background-color: #f4f4f4");
        box.setPrefSize(1200, 800);
        box.setMinSize(1200, 800);
        box.setMaxSize(1200, 800);
        box.getChildren().add(new Button("test"));
        dashboardTab.setContent(box);

        FontAwesomeIconView appointmentIcon = new FontAwesomeIconView(FontAwesomeIcon.CALENDAR);
        appointmentIcon.getStyleClass().add(STYLE_CLASS_TAB_ICON);
        Tab appointmentTab = new Tab();
        appointmentTab.setGraphic(createTabHeader(
                resources.getString(BUNDLE_KEY_TAB_TITLE_APPOINTMENT),
                appointmentIcon));

        HBox box2 = new HBox();
        box2.setStyle("-fx-background-color: #f4f4f4");
        box2.setPrefSize(1200, 800);
        box2.setMinSize(1200, 800);
        box2.setMaxSize(1200, 800);
        box2.getChildren().add(new Button("test"));
        appointmentTab.setContent(box2);

        FontAwesomeIconView financialIcon = new FontAwesomeIconView(FontAwesomeIcon.DOLLAR);
        financialIcon.getStyleClass().add(STYLE_CLASS_TAB_ICON);
        Tab financialTab = new Tab();
        financialTab.setGraphic(createTabHeader(
                resources.getString(BUNDLE_KEY_TAB_TITLE_FINANCIAL),
                financialIcon));

        FontAwesomeIconView patientIcon = new FontAwesomeIconView(FontAwesomeIcon.ADDRESS_CARD);
        patientIcon.getStyleClass().add(STYLE_CLASS_TAB_ICON);
        Tab patientTab = new Tab();
        patientTab.setGraphic(createTabHeader(
                resources.getString(BUNDLE_KEY_TAB_TITLE_PATIENT),
                patientIcon));

        FontAwesomeIconView doctorIcon = new FontAwesomeIconView(FontAwesomeIcon.HOSPITAL_ALT);
        doctorIcon.getStyleClass().add(STYLE_CLASS_TAB_ICON);
        Tab doctorTab = new Tab();
        doctorTab.setGraphic(createTabHeader(
                resources.getString(BUNDLE_KEY_TAB_TITLE_DOCTOR),
                doctorIcon));

        FontAwesomeIconView toolsIcon = new FontAwesomeIconView(FontAwesomeIcon.MAGIC);
        toolsIcon.getStyleClass().add(STYLE_CLASS_TAB_ICON);
        Tab toolsTab = new Tab();
        toolsTab.setGraphic(createTabHeader(
                resources.getString(BUNDLE_KEY_TAB_TITLE_TOOLS),
                toolsIcon));

        FontAwesomeIconView settingsIcon = new FontAwesomeIconView(FontAwesomeIcon.COG);
        settingsIcon.getStyleClass().add(STYLE_CLASS_TAB_ICON);
        Tab settingsTab = new Tab();
        settingsTab.setGraphic(createTabHeader(
                resources.getString(BUNDLE_KEY_TAB_TITLE_SETTINGS),
                settingsIcon));

        tabPane.getTabs().addAll(dashboardTab, appointmentTab, financialTab, patientTab, doctorTab, toolsTab, settingsTab);

        tabPane.setTabMinWidth(50);
        tabPane.setTabMaxWidth(50);
        tabPane.setTabMinHeight(200);
        tabPane.setTabMaxHeight(200);

        AnchorPane.setTopAnchor(tabPane, 40.0);
        AnchorPane.setLeftAnchor(tabPane, 4.0);

    }

    private StackPane createTabHeader(String text, Node graphics){
        Label label = new Label(" " + text, graphics);
        label.setContentDisplay(ContentDisplay.LEFT);

        label.getStyleClass().add("tab-pane-label");
        return new StackPane(new Group(label));
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
