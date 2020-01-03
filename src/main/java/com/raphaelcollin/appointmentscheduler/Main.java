
package com.raphaelcollin.appointmentscheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class Main extends Application {

    private static Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
    public static Preferences preferences = Preferences.userNodeForPackage(Main.class);


    // Preferences Key
    public static final String PREFERENCES_KEY_IP = "db_ip";
    public static final String PREFERENCES_KEY_PORT = "db_port";
    public static final String PREFERENCES_KEY_USER = "db_user";
    public static final String PREFERENCES_KEY_PASSWORD = "db_password";

    @Override
    public void init() throws Exception {
        System.out.println("IP Address: " + preferences.get(PREFERENCES_KEY_IP, null));
        System.out.println("Port: " + preferences.get(PREFERENCES_KEY_PORT, null));
        System.out.println("User: " + preferences.get(PREFERENCES_KEY_USER, null));
        System.out.println("Password: " + preferences.get(PREFERENCES_KEY_PASSWORD, null));
        super.init();
    }

    @Override
    public void start(Stage stage) throws Exception{

        System.setProperty("prism.lcdtext", "false");

        ResourceBundle bundle = ResourceBundle.getBundle("language", new Locale("en"));

        Parent root = FXMLLoader.load(getClass().getResource("/base_configuration.fxml"), bundle);
        stage.setScene(new Scene(root));
        stage.setTitle("Appointment Scheduler");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    // Helper Methods

    public static double getScreenWidth() {
        return screenSize.getWidth();
    }

    public static double getScreenHeight() {
        return screenSize.getHeight();
    }

    public static Optional<ButtonType> showAlert(Alert.AlertType alertType, Parent root, String headerText,
                                                 String contentText) {
        Alert alert = new Alert(alertType);
        alert.initOwner(root.getScene().getWindow());
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        return alert.showAndWait();
    }
}
