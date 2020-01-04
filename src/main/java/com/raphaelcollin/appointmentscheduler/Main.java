
package com.raphaelcollin.appointmentscheduler;

import com.raphaelcollin.appointmentscheduler.db.ConnectionFactory;
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
    public static final String PREFERENCES_KEY_LANGUAGE = "language";
    public static final String PREFERENCES_KEY_DB_SETUP = "db_setup";
    public static final String PREFERENCES_KEY_IP = "db_ip";
    public static final String PREFERENCES_KEY_PORT = "db_port";
    public static final String PREFERENCES_KEY_DB_USER = "db_user";
    public static final String PREFERENCES_KEY_DB_PASSWORD = "db_password";
    public static final String PREFERENCES_KEY_ACCESS_CONTROL = "access_control";
    public static final String PREFERENCES_KEY_ACCESS_CONTROL_USER = "ac_user";
    public static final String PREFERENCES_KEY_ACCESS_CONTROL_PASSWORD = "ac_password";
    public static final String PREFERENCES_KEY_ACCESS_CONTROL_SECURITY_QUESTION = "ac_security_question";
    public static final String PREFERENCES_KEY_ACCESS_CONTROL_ANSWER = "ac_answer";

    // Global Constants

    public static final String BUNDLE_KEY_APPLICATION_TITLE = "application_title";
    public static final String BUNDLE_KEY_ERROR_ALERT_TITLE = "alert_error_title";
    public static final String BUNDLE_KEY_ERROR_EMPTY_MESSAGE = "alert_errorMessage_empty";
    public static final String BUNDLE_KEY_ERROR_HEADER_TEXT = "alert_errorMessage_headerText";
    public static final String STYLE_CLASS_CONFIGURATION_GREEN_BUTTON = "green-button";
    public static final String LOCATION_DASHBOARD_VIEW = "/dashboard.fxml";
    public static final String STYLE_CLASS_CONFIGURATION_SUBTITLE = "configuration-subtitle";

    // Class Constants

    private static final String BUNDLE_BASE_NAME = "language";
    private static final String DEFAULT_LANGUAGE = "en";
    private static final String LOCATION_CONTAINER_CONFIGURATION = "/container_configuration.fxml";
    private static final String LOCATION_CONTAINER_LOGIN = "/container_login.fxml";

    @Override
    public void init() throws Exception {
        //preferences.clear();
        super.init();
    }

    @Override
    public void start(Stage stage) throws Exception{

        System.setProperty("prism.lcdtext", "false");

        boolean databaseConfigured = preferences.getBoolean(PREFERENCES_KEY_DB_SETUP, false);

        String rootLocation;

        if (databaseConfigured) {

            String dbIp = preferences.get(PREFERENCES_KEY_IP, null);
            String dbPort = preferences.get(PREFERENCES_KEY_PORT, null);
            String dbUser = preferences.get(PREFERENCES_KEY_DB_USER, null);
            String dbPassword = preferences.get(PREFERENCES_KEY_DB_PASSWORD, null);

            connection = ConnectionFactory.getConnection(dbIp, dbPort, dbUser, dbPassword);

            if (connection == null) {
                rootLocation = LOCATION_CONTAINER_CONFIGURATION;
            } else {

                boolean loginRequired = preferences.getBoolean(PREFERENCES_KEY_ACCESS_CONTROL, false);

                if (loginRequired) {
                    rootLocation = LOCATION_CONTAINER_LOGIN;
                } else {
                    rootLocation = LOCATION_DASHBOARD_VIEW;
                }
            }

        } else {
            rootLocation = LOCATION_CONTAINER_CONFIGURATION;
        }


        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME,
                new Locale(preferences.get(PREFERENCES_KEY_LANGUAGE, DEFAULT_LANGUAGE)));

       Parent root = FXMLLoader.load(getClass().getResource(rootLocation), bundle);
        stage.setScene(new Scene(root));
        stage.setTitle(bundle.getString(BUNDLE_KEY_APPLICATION_TITLE));
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
