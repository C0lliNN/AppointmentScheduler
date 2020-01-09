package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class DashboardController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private VBox vBoxEarnings;
    @FXML
    private VBox vBoxCompletedAppointments;
    @FXML
    private VBox vBoxUnconfirmedAppointments;
    @FXML
    private VBox vBoxUpcomingAppointments;
    @FXML
    private JFXProgressBar completedAppointmentsProgressBar;
    @FXML
    private JFXProgressBar unconfirmedAppointmentsProgressBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        double width = 1000;
        double height = 800;

        root.setMinSize(width,height);
        root.setPrefSize(width, height);
        root.setMaxSize(width, height);

        for (Node node : root.getChildren()) {

            double boxWidth = 501;
            double boxHeight = 401;

            VBox box = (VBox) node;

            box.setSpacing(20);
            box.setPadding(new Insets(75,0,0,0));
            box.setMinSize(boxWidth, boxHeight);
            box.setPrefSize(boxWidth, boxHeight);
            box.setMaxSize(boxWidth, boxHeight);

            ((Label) box.getChildren().get(0)).setFont(Font.font(32));

            Node nodeValue = (box.getChildren().get(1));

            ObservableList<Label> valueLabels = FXCollections.observableArrayList();

            if (nodeValue instanceof Label) {
                valueLabels.add((Label) nodeValue);
            } else if (nodeValue instanceof HBox) {
                ((HBox) nodeValue).getChildren().forEach(label -> valueLabels.add((Label) label));
            }

            valueLabels.forEach(label -> {
                label.setFont(Font.font(40));
                label.getStyleClass().add(STYLE_CLASS_DASHBOARD_VALUE_LABEL);
            });

            int buttonIndex = box.getChildren().size() - 1;
            ((JFXButton) box.getChildren().get(buttonIndex)).setFont(Font.font(22));

        }

        vBoxUpcomingAppointments.getStyleClass().add(STYLE_CLASS_DASHBOARD_UPCOMING_BOX);
        vBoxUnconfirmedAppointments.getStyleClass().add(STYLE_CLASS_DASHBOARD_UNCONFIRMED_BOX);
        vBoxUnconfirmedAppointments.setLayoutX(500);
        vBoxCompletedAppointments.getStyleClass().add(STYLE_CLASS_DASHBOARD_COMPLETED_BOX);
        vBoxCompletedAppointments.setLayoutY(400);
        vBoxEarnings.getStyleClass().add(STYLE_CLASS_DASHBOARD_EARNINGS_BOX);
        vBoxEarnings.setLayoutX(500);
        vBoxEarnings.setLayoutY(400);

        double progressBarWidth = 300;
        double progressBarHeight = 30;

        completedAppointmentsProgressBar.setMinSize(progressBarWidth, progressBarHeight);
        completedAppointmentsProgressBar.setPrefSize(progressBarWidth, progressBarHeight);
        completedAppointmentsProgressBar.setMaxSize(progressBarWidth, progressBarHeight);

        completedAppointmentsProgressBar.setProgress(0.55);

        unconfirmedAppointmentsProgressBar.setMinSize(progressBarWidth, progressBarHeight);
        unconfirmedAppointmentsProgressBar.setPrefSize(progressBarWidth, progressBarHeight);
        unconfirmedAppointmentsProgressBar.setMaxSize(progressBarWidth, progressBarHeight);

        unconfirmedAppointmentsProgressBar.setProgress(0.4);
    }
}
