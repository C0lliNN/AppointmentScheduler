
package com.raphaelcollin.appointmentscheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/dashboard_view.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Appointment Scheduler");
        stage.show();

    }
}
