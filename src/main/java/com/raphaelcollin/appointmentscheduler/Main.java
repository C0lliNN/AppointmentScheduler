
package com.raphaelcollin.appointmentscheduler;

import com.raphaelcollin.appointmentscheduler.controller.ContainerConfigurationController;
import com.raphaelcollin.appointmentscheduler.controller.MainController;
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
import javafx.scene.image.Image;
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

import static com.raphaelcollin.appointmentscheduler.ApplicationPreferences.*;

public class Main extends Application {

    // Global attributes

    private static Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
    private static Connection connection;
    private static ResourceBundle resources;

    // File Locations

    public static final String ACCESS_CONTROL_CONFIGURATION_LOCATION = "/access_control_configuration.fxml";
    public static final String CONTAINER_CONFIGURATION_LOCATION = "/container_configuration.fxml";
    public static final String CONTAINER_LOGIN_LOCATION = "/container_login.fxml";
    public static final String MAIN_VIEW_LOCATION = "/main_view.fxml";
    public static final String DATABASE_CONFIGURATION_LOCATION = "/database_configuration.fxml";
    public static final String LOGIN_LOCATION = "/login.fxml";
    public static final String RECOVER_CREDENTIALS_LOCATION = "/recover_credentials.fxml";
    public static final String APPLICATION_ICON_TITLE_BAR_LOCATION = "/title-bar-icon.png";
    public static final String DASHBOARD_CONTENT_LOCATION = "/dashboard_content.fxml";
    public static final String APPOINTMENT_CONTENT_LOCATION = "/appointment_content.fxml";

    // Bundle Keys

    public static final String BUNDLE_KEY_APPLICATION_TITLE = "application_title";
    public static final String BUNDLE_KEY_ERROR_ALERT_TITLE = "alert_error_title";
    public static final String BUNDLE_KEY_ERROR_EMPTY_MESSAGE = "alert_errorMessage_empty";
    public static final String BUNDLE_KEY_ERROR_HEADER_TEXT = "alert_errorMessage_headerText";
    public static final String BUNDLE_KEY_CONNECTION_ERROR_CONTENT_TEXT2 = "database_configuration_errorMessage3_contentText";
    public static final String BUNDLE_KEY_CONNECTION_ERROR_LABEL = "database_configuration_errorLabel";
    public static final String BUNDLE_KEY_CONNECTION_ERROR_HEADER_TEXT = "database_configuration_errorMessage2_headerText";
    public static final String BUNDLE_KEY_ERROR_PASSWORD_MATCH = "access_control_error_password_match";
    public static final String BUNDLE_KEY_ERROR_PORT_INVALID_MESSAGE = "database_configuration_errorMessage_invalidPort";
    public static final String BUNDLE_KEY_CONNECTION_ERROR_CONTENT_TEXT = "database_configuration_errorMessage2_contentText";
    public static final String BUNDLE_KEY_ERROR_IP_INVALID_MESSAGE = "database_configuration_errorMessage_invalidIP";
    public static final String BUNDLE_KEY_INVALID_CREDENTIALS_MESSAGE = "login_alert_invalid_credentials_message";
    public static final String BUNDLE_KEY_ERROR_INCORRECT_ANSWER = "recover_credentials_invalid_answer";
    public static final String BUNDLE_KEY_LOADING_DATA = "main_controller_loading_data";
    public static final String BUNDLE_KEY_TAB_TITLE_DASHBOARD = "tab_title_dashboard";
    public static final String BUNDLE_KEY_TAB_TITLE_APPOINTMENT = "tab_title_appointment";
    public static final String BUNDLE_KEY_TAB_TITLE_FINANCIAL = "tab_title_financial";
    public static final String BUNDLE_KEY_TAB_TITLE_PATIENT = "tab_title_patient";
    public static final String BUNDLE_KEY_TAB_TITLE_DOCTOR = "tab_title_doctor";
    public static final String BUNDLE_KEY_TAB_TITLE_TOOLS = "tab_title_tools";
    public static final String BUNDLE_KEY_TAB_TITLE_SETTINGS = "tab_title_settings";
    public static final String BUNDLE_KEY_TIME_FORMAT = "time_format_12hours";

    // Classes and Ids

    public static final String STYLE_CLASS_CONFIGURATION_GREEN_BUTTON = "green-button";
    public static final String STYLE_CLASS_CONFIGURATION_SUBTITLE = "configuration-subtitle";
    public static final String STYLE_CLASS_ORANGE_BUTTON = "orange-button";
    public static final String STYLE_CLASS_CLOSE_ICON = "close-icon";
    public static final String STYLE_CLASS_CLOSE_BUTTON = "title-button";
    public static final String STYLE_CLASS_MINIMIZE_ICON = "minimize-icon";
    public static final String STYLE_CLASS_MINIMIZE_BUTTON = "title-button";
    public static final String ID_RECOVER_CREDENTIALS_LABEL = "label-credentials";
    public static final String STYLE_CLASS_TAB_ICON = "tab-icon";
    public static final String ID_APPLICATION_TITLE_LABEL = "application-title";
    public static final String STYLE_CLASS_DASHBOARD_VALUE_LABEL = "value-label";
    public static final String STYLE_CLASS_DASHBOARD_UNCONFIRMED_BOX = "dashboard-tab-unconfirmed-box";
    public static final String STYLE_CLASS_DASHBOARD_UPCOMING_BOX = "dashboard-tab-upcoming-box";
    public static final String STYLE_CLASS_DASHBOARD_COMPLETED_BOX = "dashboard-tab-completed-box";
    public static final String STYLE_CLASS_DASHBOARD_EARNINGS_BOX = "dashboard-tab-earnings-box";
    public static final String STYLE_CLASS_BLUE_CHECK_BOX = "blue-check-box";
    public static final String STYLE_CLASS_BLUE_BUTTON = "blue-button";
    public static final String STYLE_CLASS_MAIN_VIEW_GREEN_BUTTON = "green-button";
    public static final String STYLE_CLASS_MAIN_VIEW_ORANGE_BUTTON = "orange-button";
    public static final String STYLE_CLASS_RED_BUTTON = "red-button";
    public static final String STYLE_CLASS_APPOINTMENT_TAB = "appointment-tab";
    public static final String STYLE_CLASS_TAB_PANE_LABEL = "tab-pane-label";


    // Class Constants

    private static final String BUNDLE_BASE_NAME = "language";
    public static final String DEFAULT_LANGUAGE = "en";
    private static final String LOCATION_STAGE_ICON = "/stage_icon.png";
    public static int TRANSITION_FROM_LEFT = 1;
    public static int TRANSITION_FROM_RIGHT = 2;

    @Override
    public void init() throws Exception {
        //ApplicationPreferences.getInstance().getPreferences().clear();
        //ApplicationPreferences.getInstance().getPreferences().putBoolean(PREFERENCES_KEY_ACCESS_CONTROL, false);
        super.init();
    }

    @Override
    public void start(Stage stage) throws Exception{

        System.setProperty("prism.lcdtext", "false");

        boolean databaseConfigured = ApplicationPreferences.getInstance().getPreferences().getBoolean(PREFERENCES_KEY_DB_SETUP, false);

        resources = ResourceBundle.getBundle(BUNDLE_BASE_NAME,
                new Locale(ApplicationPreferences.getInstance().getPreferences().get(PREFERENCES_KEY_LANGUAGE, DEFAULT_LANGUAGE)));

        FXMLLoader loader = new FXMLLoader();
        loader.setResources(resources);
        Parent root = null;

        if (databaseConfigured) {

            DatabaseCredentials dbCredentials = DatabaseCredentials.getSavedCredentials();

            connection = ConnectionFactory.getConnection(dbCredentials);

            if (connection == null) {

                loader.setLocation(getClass().getResource(CONTAINER_CONFIGURATION_LOCATION));
                root = loader.load();
                ContainerConfigurationController dbController = loader.getController();
                dbController.setupFields(dbCredentials.getIp(), dbCredentials.getPort(), dbCredentials.getUser(),
                        dbCredentials.getPassword());
            } else {

                boolean loginRequired = ApplicationPreferences.getInstance().getPreferences().getBoolean(PREFERENCES_KEY_ACCESS_CONTROL, false);

                if (loginRequired) {
                    loader.setLocation(getClass().getResource(CONTAINER_LOGIN_LOCATION));
                } else {
                    loader.setLocation(getClass().getResource(MAIN_VIEW_LOCATION));
                    root = loader.load();
                    MainController mainController = loader.getController();
                }
            }

        } else {
            loader.setLocation(getClass().getResource(CONTAINER_CONFIGURATION_LOCATION));
        }

        if (root == null) {
            root = loader.load();
        }

        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(getClass().getResourceAsStream(LOCATION_STAGE_ICON)));
        stage.setTitle(resources.getString(BUNDLE_KEY_APPLICATION_TITLE));
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


    public static void switchScenes(AnchorPane containerRoot, Parent outRoot, Parent inRoot, int option) {

        double translateXInRoot;
        double translateXOutRoot;

        if (option == TRANSITION_FROM_LEFT) {
            translateXInRoot = -containerRoot.getScene().getWidth();
            translateXOutRoot = +containerRoot.getScene().getWidth();
        } else {
            translateXInRoot = containerRoot.getScene().getWidth();
            translateXOutRoot = -containerRoot.getScene().getWidth();
        }

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

    public static void createMainViewStage() {

        FXMLLoader loader = new FXMLLoader(Main.class.getResource(MAIN_VIEW_LOCATION), resources);
        Parent dashboardRoot = null;
        try {
            dashboardRoot = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert dashboardRoot != null;

        MainController mainController = loader.getController();

        Stage newStage = new Stage();
        newStage.setTitle(resources.getString(BUNDLE_KEY_APPLICATION_TITLE));
        newStage.setScene(new Scene(dashboardRoot));
        newStage.getIcons().add(new Image(Main.class.getResourceAsStream(LOCATION_STAGE_ICON)));
        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.show();
    }

    public static Optional<ButtonType> showAlert(Alert.AlertType alertType, Parent root, String title, String headerText,
                                                 String contentText) {
        Alert alert = new Alert(alertType);
        if (root != null && root.getScene() != null) {
            alert.initOwner(root.getScene().getWindow());
        } else {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(Main.class.getResourceAsStream(LOCATION_STAGE_ICON)));
        }
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        return alert.showAndWait();
    }

    public static ResourceBundle getResources() {
        return resources;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        Main.connection = connection;
    }
}
