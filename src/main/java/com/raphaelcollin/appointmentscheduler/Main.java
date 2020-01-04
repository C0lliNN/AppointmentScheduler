
package com.raphaelcollin.appointmentscheduler;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class Main extends Application {

    // Global attributes

    private static Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
    private static Preferences preferences = Preferences.userNodeForPackage(Main.class);

    private static Connection connection;

    // Preferences Key
    public static final String PREFERENCES_KEY_DB_SETUP = "db_setup";
    public static final String PREFERENCES_KEY_IP = "db_ip";
    public static final String PREFERENCES_KEY_PORT = "db_port";
    public static final String PREFERENCES_KEY_USER = "db_user";
    public static final String PREFERENCES_KEY_PASSWORD = "db_password";

    // Global Constants

    public static final String BUNDLE_KEY_ERROR_ALERT_TITLE = "alert_error_title";
    public static final String STYLE_CLASS_CONFIGURATION_GREEN_BUTTON = "green-button";

    @Override
    public void init() throws Exception {
        System.out.println("DB Setup: " + preferences.getBoolean(PREFERENCES_KEY_DB_SETUP, false));
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

        Parent root = FXMLLoader.load(getClass().getResource("/container_configuration.fxml"), bundle);
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

    public static void switchScenes(AnchorPane containerRoot, Parent outRoot, Parent inRoot) {

        double translateXInRoot = containerRoot.getScene().getWidth();
        double translateXOutRoot = -containerRoot.getScene().getWidth();

        inRoot.setTranslateX(translateXInRoot);
        outRoot.setTranslateX(0.0);

        containerRoot.getChildren().add(inRoot);
        AnchorPane.setTopAnchor(inRoot, 40.0);

        KeyValue keyValue1 = new KeyValue(inRoot.translateXProperty(), 0.0, Interpolator.EASE_IN);
        KeyValue keyValue2 = new KeyValue(outRoot.translateXProperty(), translateXOutRoot);
        KeyFrame frame1 = new KeyFrame(Duration.millis(800), keyValue1, keyValue2);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().setAll(frame1);
        timeline.play();

    }

    public static AnchorPane loadView(String location, ResourceBundle resources){
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(location), resources);
        try {
            return loader.load();
        } catch (IOException e) {
            return null;
        }
    }

    public static Optional<ButtonType> showAlert(Alert.AlertType alertType, Parent root, String title, String headerText,
                                                 String contentText) {
        Alert alert = new Alert(alertType);
        alert.initOwner(root.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        return alert.showAndWait();
    }

    public static Preferences getPreferences() {
        return preferences;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        Main.connection = connection;
    }
}
