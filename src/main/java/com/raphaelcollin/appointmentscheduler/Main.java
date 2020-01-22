
package com.raphaelcollin.appointmentscheduler;

import com.raphaelcollin.appointmentscheduler.controller.ContainerController;
import com.raphaelcollin.appointmentscheduler.controller.DatabaseConfigurationController;
import com.raphaelcollin.appointmentscheduler.db.ConnectionFactory;
import com.raphaelcollin.appointmentscheduler.db.model.ComboBoxItemHelper;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.util.*;

import static com.raphaelcollin.appointmentscheduler.ApplicationPreferences.*;

public class Main extends Application {
    // Global attributes

    private static Connection connection;
    private static ResourceBundle resources;
    public static ObservableList<ComboBoxItemHelper> statusList;
    public static ObservableList<ComboBoxItemHelper> genderList;

    // File Locations

    public static final String ACCESS_CONTROL_CONFIGURATION_LOCATION = "/access_control_configuration.fxml";
    public static final String MAIN_VIEW_LOCATION = "/main_view.fxml";
    public static final String DATABASE_CONFIGURATION_LOCATION = "/database_configuration.fxml";
    public static final String LOGIN_LOCATION = "/login.fxml";
    public static final String RECOVER_CREDENTIALS_LOCATION = "/recover_credentials.fxml";
    public static final String APPLICATION_ICON_TITLE_BAR_LOCATION = "/title-bar-icon.png";
    public static final String DASHBOARD_CONTENT_LOCATION = "/dashboard_content.fxml";
    public static final String APPOINTMENT_CONTENT_LOCATION = "/appointment_content.fxml";
    public static final String CONTAINER_LOCATION = "/container.fxml";
    public static final String APPOINTMENT_FIELDS_LOCATION = "/appointment_fields.fxml";
    public static final String APPOINTMENT_ADD_LOCATION ="/appointment_add.fxml";
    public static final String APPOINTMENT_DETAILS_LOCATION = "/appointment_details.fxml";
    public static final String FINANCIAL_VIEW_LOCATION = "/financial_view.fxml";
    public static final String PATIENT_VIEW_LOCATION = "/patient_view.fxml";

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
    public static final String BUNDLE_KEY_TIME_FORMAT = "time_format";
    public static final String BUNDLE_KEY_STATUS_ALL = "tab_appointments_status_all";
    public static final String BUNDLE_KEY_STATUS_UNCONFIRMED = "tab_appointments_status_unconfirmed";
    public static final String BUNDLE_KEY_STATUS_CONFIRMED = "tab_appointments_status_confirmed";
    public static final String BUNDLE_KEY_STATUS_CANCELED ="tab_appointments_status_canceled" ;
    public static final String BUNDLE_KEY_STATUS_COMPLETED = "tab_appointments_status_completed";
    public static final String BUNDLE_KEY_STATUS_TEXT = "tab_appointments_status_text";
    public static final String BUNDLE_KEY_DATABASE_ERROR_HEADER_TEXT = "alert_database_error_headerText";
    public static final String BUNDLE_KEY_DATABASE_ERROR_CONTENT_TEXT = "alert_database_error_contentText";
    public static final String BUNDLE_KEY_APPOINTMENT_FIELD_MESSAGE = "alert_appointment_field_message";
    public static final String BUNDLE_KEY_APPOINTMENT_FIELD_MESSAGE2 = "alert_appointment_field_priceMessage";
    public static final String BUNDLE_KEY_INVALID_SELECTION_HEADER_TEXT = "alert_invalid_selection_headerText";
    public static final String BUNDLE_KEY_INVALID_SELECTION_CONTENT_TEXT = "alert_invalid_selection_contentText";
    public static final String BUNDLE_KEY_GENDER_MALE = "gender_male";
    public static final String BUNDLE_KEY_GENDER_FEMALE = "gender_female";
    public static final String BUNDLE_KEY_DATE_FORMAT = "date_format";

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
    public static final String ID_CONTAINER_ROOT = "container-root";
    public static final String STYLE_CLASS_BUTTON_ICON = "button-icon";

    // Class Constants

    private static final String BUNDLE_BASE_NAME = "language";
    public static final String DEFAULT_LANGUAGE = "en";
    private static final String LOCATION_STAGE_ICON = "/stage_icon.png";
    public static int TRANSITION_FROM_LEFT = 1;
    public static int TRANSITION_FROM_RIGHT = 2;

    // Appointment Status Constants

    public static final String UNCONFIRMED = "Unconfirmed";
    public static final String COMPLETED = "Completed";
    public static final String CANCELED = "Canceled";
    public static final String CONFIRMED = "Confirmed";
    public static final int UNCONFIRMED_INDEX = 1;
    public static final int CONFIRMED_INDEX = 2;
    public static final int CANCELED_INDEX = 3;
    public static final int COMPLETED_INDEX = 4;

    // Gender Constants

    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    public static final int MALE_INDEX = 1;
    public static final int FEMALE_INDEX = 2;



    @Override
    public void init() throws Exception {
        //ApplicationPreferences.getInstance().getPreferences().clear();
        //ApplicationPreferences.getInstance().getPreferences().putBoolean(PREFERENCES_KEY_ACCESS_CONTROL, false);
        super.init();

        statusList = FXCollections.observableList(Arrays.asList(
                new ComboBoxItemHelper("", 0, BUNDLE_KEY_STATUS_ALL),
                new ComboBoxItemHelper(UNCONFIRMED, UNCONFIRMED_INDEX, BUNDLE_KEY_STATUS_UNCONFIRMED),
                new ComboBoxItemHelper(CONFIRMED, CONFIRMED_INDEX, BUNDLE_KEY_STATUS_CONFIRMED),
                new ComboBoxItemHelper(CANCELED, CANCELED_INDEX, BUNDLE_KEY_STATUS_CANCELED),
                new ComboBoxItemHelper(COMPLETED, COMPLETED_INDEX, BUNDLE_KEY_STATUS_COMPLETED)
        ));

        genderList = FXCollections.observableList(Arrays.asList(
                new ComboBoxItemHelper(MALE, MALE_INDEX, BUNDLE_KEY_GENDER_MALE),
                new ComboBoxItemHelper(FEMALE, FEMALE_INDEX, BUNDLE_KEY_GENDER_FEMALE)
        ));


    }

    @Override
    public void start(Stage stage) throws Exception{

        System.setProperty("prism.lcdtext", "false");

        boolean databaseConfigured = ApplicationPreferences.getInstance().getPreferences().getBoolean(PREFERENCES_KEY_DB_SETUP, false);

        resources = ResourceBundle.getBundle(BUNDLE_BASE_NAME,
                new Locale(ApplicationPreferences.getInstance().getPreferences().get(PREFERENCES_KEY_LANGUAGE, DEFAULT_LANGUAGE)));

        FXMLLoader loader = new FXMLLoader();
        loader.setResources(resources);
        Parent childRoot = null;
        double rootWidth = 600;
        double rootHeight = 550;

        if (databaseConfigured) {

            DatabaseCredentials dbCredentials = DatabaseCredentials.getSavedCredentials();

            connection = ConnectionFactory.getConnection(dbCredentials);

            if (connection == null) {

                loader.setLocation(getClass().getResource(DATABASE_CONFIGURATION_LOCATION));
                childRoot = loader.load();
                DatabaseConfigurationController dbController = loader.getController();
                dbController.setupFields(dbCredentials.getIp(), dbCredentials.getPort(), dbCredentials.getUser(),
                        dbCredentials.getPassword());

            } else {

                boolean loginRequired = ApplicationPreferences.getInstance().getPreferences().getBoolean(PREFERENCES_KEY_ACCESS_CONTROL, false);

                if (loginRequired) {
                    loader.setLocation(getClass().getResource(LOGIN_LOCATION));
                    rootWidth = 600;
                    rootHeight = 400;
                } else {
                    loader.setLocation(getClass().getResource(MAIN_VIEW_LOCATION));
                    childRoot = loader.load();
                    rootWidth = 1208;
                    rootHeight = 845;
                }
            }

        } else {
            loader.setLocation(getClass().getResource(DATABASE_CONFIGURATION_LOCATION));
        }

        if (childRoot == null) {
            childRoot = loader.load();
        }

        Parent containerRoot = createView(rootWidth, rootHeight, childRoot);

        stage.setScene(new Scene(containerRoot));
        stage.getIcons().add(new Image(getClass().getResourceAsStream(LOCATION_STAGE_ICON)));
        stage.setTitle(resources.getString(BUNDLE_KEY_APPLICATION_TITLE));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    // Helper Methods


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
        AnchorPane.setLeftAnchor(inRoot, 4.0);
        AnchorPane.setRightAnchor(inRoot, 4.0);

        KeyValue keyValue1 = new KeyValue(inRoot.translateXProperty(), 0.0, Interpolator.EASE_IN);
        KeyValue keyValue2 = new KeyValue(outRoot.translateXProperty(), translateXOutRoot);
        KeyFrame frame1 = new KeyFrame(Duration.millis(800), keyValue1, keyValue2);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().setAll(frame1);
        timeline.play();

    }

    public static void createMainViewStage() {

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(MAIN_VIEW_LOCATION), resources);
            Parent mainViewRoot = loader.load();

            Parent root = createView(1208, 845, mainViewRoot);

            Stage newStage = new Stage();
            newStage.setTitle(resources.getString(BUNDLE_KEY_APPLICATION_TITLE));
            newStage.setScene(new Scene(root));
            newStage.getIcons().add(new Image(Main.class.getResourceAsStream(LOCATION_STAGE_ICON)));
            newStage.initStyle(StageStyle.TRANSPARENT);
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public static AnchorPane createView(double width, double height, Parent childRoot) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(CONTAINER_LOCATION), resources);
        AnchorPane root = null;
        try {
            root = loader.load();
            ContainerController containerController = loader.getController();
            containerController.setSize(width, height);
            containerController.addChild(childRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;
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
