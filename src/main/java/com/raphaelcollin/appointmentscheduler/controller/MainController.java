package com.raphaelcollin.appointmentscheduler.controller;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class MainController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private TabPane tabPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        double tabPaneWidth = 1200;
        double tabPaneHeight = 801;

        tabPane.setPrefSize(tabPaneWidth, tabPaneHeight);
        tabPane.setMinSize(tabPaneWidth, tabPaneHeight);
        tabPane.setMaxSize(tabPaneWidth, tabPaneHeight);

        tabPane.setTabMinWidth(50);
        tabPane.setTabMaxWidth(50);
        tabPane.setTabMinHeight(200);
        tabPane.setTabMaxHeight(200);

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

            Parent financialContent = FXMLLoader.load(getClass().getResource(FINANCIAL_VIEW_LOCATION), getResources());
            financialTab.setContent(financialContent);


            FontAwesomeIconView patientIcon = new FontAwesomeIconView(FontAwesomeIcon.USER);
            patientIcon.getStyleClass().add(STYLE_CLASS_TAB_ICON);
            Tab patientTab = new Tab();
            patientTab.setGraphic(createTabHeader(
                    getResources().getString(BUNDLE_KEY_TAB_TITLE_PATIENT),
                    patientIcon));

            Parent patientContent = FXMLLoader.load(getClass().getResource(PATIENT_VIEW_LOCATION), resources);
            patientTab.setContent(patientContent);

            FontAwesomeIconView doctorIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_MD);
            doctorIcon.getStyleClass().add(STYLE_CLASS_TAB_ICON);
            Tab doctorTab = new Tab();
            doctorTab.setGraphic(createTabHeader(
                    getResources().getString(BUNDLE_KEY_TAB_TITLE_DOCTOR),
                    doctorIcon));

            Parent doctorContent = FXMLLoader.load(getClass().getResource(DOCTOR_VIEW_LOCATION), resources);
            doctorTab.setContent(doctorContent);

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

            AnchorPane.setLeftAnchor(box, 200.0);

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

}
