package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.raphaelcollin.appointmentscheduler.db.DataSource;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class MainController implements Initializable {

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

        imageView.setImage(new Image(getClass().getResourceAsStream(APPLICATION_ICON_TITLE_BAR_LOCATION)));
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

        double tabPaneWidth = 224;
        double tabPaneHeight = 800;

        tabPane.setPrefSize(tabPaneWidth, tabPaneHeight);
        tabPane.setMinSize(tabPaneWidth, tabPaneHeight);
        tabPane.setMaxSize(tabPaneWidth, tabPaneHeight);
        

        tabPane.setTabMinWidth(50);
        tabPane.setTabMaxWidth(50);
        tabPane.setTabMinHeight(200);
        tabPane.setTabMaxHeight(200);

        AnchorPane.setTopAnchor(tabPane, 40.0);
        AnchorPane.setLeftAnchor(tabPane, 4.0);

        try {

            FontAwesomeIconView dashBoardIcon = new FontAwesomeIconView(FontAwesomeIcon.DASHBOARD);
            dashBoardIcon.getStyleClass().add(STYLE_CLASS_TAB_ICON);

            Tab dashboardTab = new Tab();
            dashboardTab.setGraphic(createTabHeader(
                    getResources().getString(BUNDLE_KEY_TAB_TITLE_DASHBOARD),
                    dashBoardIcon));

            Parent dashboardContent = FXMLLoader.load(getClass().getResource(DASHBOARD_CONTENT_LOCATION), getResources());
            dashboardTab.setContent(dashboardContent);

            FontAwesomeIconView appointmentIcon = new FontAwesomeIconView(FontAwesomeIcon.CALENDAR);
            appointmentIcon.getStyleClass().add(STYLE_CLASS_TAB_ICON);

            Tab appointmentTab = new Tab();
            appointmentTab.setGraphic(createTabHeader(
                    getResources().getString(BUNDLE_KEY_TAB_TITLE_APPOINTMENT),
                    appointmentIcon));

            Parent appointmentContent = FXMLLoader.load(getClass().getResource(APPOINTMENT_CONTENT_LOCATION), getResources());
            appointmentTab.setContent(appointmentContent);

            FontAwesomeIconView financialIcon = new FontAwesomeIconView(FontAwesomeIcon.DOLLAR);
            financialIcon.getStyleClass().add(STYLE_CLASS_TAB_ICON);
            Tab financialTab = new Tab();
            financialTab.setGraphic(createTabHeader(
                    getResources().getString(BUNDLE_KEY_TAB_TITLE_FINANCIAL),
                    financialIcon));

            FontAwesomeIconView patientIcon = new FontAwesomeIconView(FontAwesomeIcon.USER);
            patientIcon.getStyleClass().add(STYLE_CLASS_TAB_ICON);
            Tab patientTab = new Tab();
            patientTab.setGraphic(createTabHeader(
                    getResources().getString(BUNDLE_KEY_TAB_TITLE_PATIENT),
                    patientIcon));

            FontAwesomeIconView doctorIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_MD);
            doctorIcon.getStyleClass().add(STYLE_CLASS_TAB_ICON);
            Tab doctorTab = new Tab();
            doctorTab.setGraphic(createTabHeader(
                    getResources().getString(BUNDLE_KEY_TAB_TITLE_DOCTOR),
                    doctorIcon));

            FontAwesomeIconView toolsIcon = new FontAwesomeIconView(FontAwesomeIcon.WRENCH);
            toolsIcon.getStyleClass().add(STYLE_CLASS_TAB_ICON);
            Tab toolsTab = new Tab();
            toolsTab.setGraphic(createTabHeader(
                    getResources().getString(BUNDLE_KEY_TAB_TITLE_TOOLS),
                    toolsIcon));

            FontAwesomeIconView settingsIcon = new FontAwesomeIconView(FontAwesomeIcon.COG);
            settingsIcon.getStyleClass().add(STYLE_CLASS_TAB_ICON);
            Tab settingsTab = new Tab();
            settingsTab.setGraphic(createTabHeader(
                    getResources().getString(BUNDLE_KEY_TAB_TITLE_SETTINGS),
                    settingsIcon));

            tabPane.getTabs().addAll(dashboardTab, appointmentTab, financialTab, patientTab, doctorTab, toolsTab, settingsTab);

            tabPane.getTabs().forEach(tab -> tab.setDisable(true));
            dashboardTab.getContent().setOpacity(0.0);

            VBox box = new VBox(50);
            box.setAlignment(Pos.CENTER);
            box.setPrefSize(1000,800);

            Label label = new Label(resources.getString(BUNDLE_KEY_LOADING_DATA));
            label.setFont(Font.font(40));

            JFXSpinner spinner = new JFXSpinner();
            spinner.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            spinner.setPrefSize(400, 400);

            box.getChildren().setAll(label, spinner);
            root.getChildren().add(box);

            AnchorPane.setTopAnchor(box, 40.0);
            AnchorPane.setRightAnchor(box, 4.0);

            Task<Void> loadInitialDataTask = new Task<Void>() {
                @Override
                protected Void call() {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    DataSource.getInstance().loadInitialData();

                    return null;
                }
            };

            loadInitialDataTask.setOnSucceeded(event -> {

                root.getChildren().remove(box);
                tabPane.getTabs().forEach(tab -> tab.setDisable(false));
                dashboardTab.getContent().setOpacity(1.0);

            });

            new Thread(loadInitialDataTask).start();

            // DataSource Class, Appointment Filtering/ Ordering, Dashboard,

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private StackPane createTabHeader(String text, Node graphics){
        Label label = new Label(" " + text, graphics);
        label.setContentDisplay(ContentDisplay.LEFT);

        label.getStyleClass().add(STYLE_CLASS_TAB_PANE_LABEL);
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
