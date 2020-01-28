
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

    private static ResourceBundle resources;
    public static ObservableList<ComboBoxItemHelper> statusList;
    public static ObservableList<ComboBoxItemHelper> genderList;
    public static Map<String, String> languageList;

    // Files Location

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
    public static final String APPOINTMENT_UPDATE_LOCATION = "/appointment_update.fxml";
    public static final String APPOINTMENT_DETAILS_LOCATION = "/appointment_details.fxml";
    public static final String FINANCIAL_VIEW_LOCATION = "/financial_view.fxml";
    public static final String PATIENT_VIEW_LOCATION = "/patient_view.fxml";
    public static final String PATIENT_FIELDS_LOCATION = "/patient_fields.fxml";
    public static final String PATIENT_DIALOG_LOCATION = "/patient_dialog.fxml";
    public static final String DOCTOR_VIEW_LOCATION = "/doctor_view.fxml";
    public static final String DOCTOR_DIALOG_LOCATION = "/doctor_dialog.fxml";
    public static final String TOOLS_VIEW_LOCATION = "/tools_content.fxml";
    public static final String SETTINGS_VIEW_LOCATION = "/settings_view.fxml";
    private static final String STAGE_ICON_LOCATION = "/stage_icon.png";

    // Bundle Keys

    public static final String BUNDLE_KEY_APPLICATION_TITLE = "appointment_scheduler";
    public static final String BUNDLE_KEY_ERROR_ALERT_TITLE = "error";
    public static final String BUNDLE_KEY_ERROR_EMPTY_MESSAGE = "empty_message";
    public static final String BUNDLE_KEY_ERROR_INVALID_INPUT = "invalid_input";
    public static final String BUNDLE_KEY_ERROR_CONNECTION_HEADER = "database_connection_error_header";
    public static final String BUNDLE_KEY_ERROR_TEST_CONNECTION_MESSAGE = "database_configuration_test_error_message";
    public static final String BUNDLE_KEY_ERROR_CONNECTION_LABEL = "database_configuration_error_label";
    public static final String BUNDLE_KEY_ERROR_PASSWORD_MATCH = "password_match";
    public static final String BUNDLE_KEY_ERROR_PORT_INVALID_MESSAGE = "invalid_port";
    public static final String BUNDLE_KEY_ERROR_CONNECTION_MESSAGE = "database_connection_error_message";
    public static final String BUNDLE_KEY_ERROR_INVALID_CREDENTIALS = "user_password_incorrect";
    public static final String BUNDLE_KEY_ERROR_INCORRECT_ANSWER = "answer_incorrect";
    public static final String BUNDLE_KEY_ERROR_DATABASE_MESSAGE = "checkout_db_connection";
    public static final String BUNDLE_KEY_ERROR_INVALID_PRICE = "invalid_price";
    public static final String BUNDLE_KEY_ERROR_INVALID_SELECTION_HEADER = "no_item_selected";
    public static final String BUNDLE_KEY_ERROR_INVALID_SELECTION_MESSAGE = "select_item";
    public static final String BUNDLE_KEY_ERROR_LOCATION_HEADER = "location_missing";
    public static final String BUNDLE_KEY_ERROR_LOCATION_MESSAGE = "select_location_message";
    public static final String BUNDLE_KEY_ERROR_DATA_EXPORT_HEADER = "error_data_exportation";
    public static final String BUNDLE_KEY_ERROR_DATA_EXPORT_CONTENT = "make_sure_valid_location";
    public static final String BUNDLE_KEY_LOADING_DATA = "loading_data";
    public static final String BUNDLE_KEY_TAB_DASHBOARD = "dashboard";
    public static final String BUNDLE_KEY_APPOINTMENT = "appointment";
    public static final String BUNDLE_KEY_FINANCIAL = "financial";
    public static final String BUNDLE_KEY_PATIENT = "patient";
    public static final String BUNDLE_KEY_DOCTOR = "doctor";
    public static final String BUNDLE_KEY_TOOLS = "tools";
    public static final String BUNDLE_KEY_SETTINGS = "settings";
    public static final String BUNDLE_KEY_DATE_FORMAT = "date_format";
    public static final String BUNDLE_KEY_TIME_FORMAT = "time_format";
    public static final String BUNDLE_KEY_STATUS_ALL = "all";
    public static final String BUNDLE_KEY_STATUS_UNCONFIRMED = "unconfirmed";
    public static final String BUNDLE_KEY_STATUS_CONFIRMED = "confirmed";
    public static final String BUNDLE_KEY_STATUS_CANCELED = "canceled";
    public static final String BUNDLE_KEY_STATUS_COMPLETED = "completed";
    public static final String BUNDLE_KEY_GENDER_MALE = "male";
    public static final String BUNDLE_KEY_GENDER_FEMALE = "female";
    public static final String BUNDLE_KEY_ADD_PATIENT = "add_patient";
    public static final String BUNDLE_KEY_EDIT_PATIENT = "edit_patient";
    public static final String BUNDLE_KEY_INVALID_EMAIL = "invalid_email";
    public static final String BUNDLE_KEY_ADD_DOCTOR = "add_doctor";
    public static final String BUNDLE_KEY_EDIT_DOCTOR = "edit_doctor";
    public static final String BUNDLE_KEY_SELECT_LOCATION = "select_location";
    public static final String BUNDLE_KEY_FILES = "files";
    public static final String BUNDLE_KEY_SUCCESS_DATA_EXPORT_HEADER = "data_exported";
    public static final String BUNDLE_KEY_SUCCESS_DATA_EXPORT_MESSAGE = "data_exported_successfully";
    public static final String BUNDLE_KEY_APPOINTMENT_ID = "appointmentId";
    public static final String BUNDLE_KEY_DATE = "date";
    public static final String BUNDLE_KEY_PRICE = "price";
    public static final String BUNDLE_KEY_DESCRIPTION = "description";
    public static final String BUNDLE_KEY_STATUS = "status";
    public static final String BUNDLE_KEY_PATIENT_ID = "patientId";
    public static final String BUNDLE_KEY_DOCTOR_ID = "doctorId";
    public static final String BUNDLE_KEY_FIRST_NAME = "firstName";
    public static final String BUNDLE_KEY_LAST_NAME = "lastName";
    public static final String BUNDLE_KEY_GENDER = "gender";
    public static final String BUNDLE_KEY_BIRTH_DATE = "birthDate";
    public static final String BUNDLE_KEY_PHONE_NUMBER = "phoneNumber";
    public static final String BUNDLE_KEY_EMAIL = "email";
    public static final String BUNDLE_KEY_CITY = "city";
    public static final String BUNDLE_KEY_ZIP_CODE = "zipCode";
    public static final String BUNDLE_KEY_STREET_NAME = "streetName";
    public static final String BUNDLE_KEY_HOUSE_NUMBER = "houseNumber";
    public static final String BUNDLE_KEY_NAME = "name";
    public static final String BUNDLE_KEY_LICENSE_NUMBER = "licenseNumber";
    public static final String BUNDLE_KEY_SETTING_ALERT_HEADER = "changes_applied";
    public static final String BUNDLE_KEY_SETTING_ALERT_MESSAGE = "you_need_restart";
    public static final String BUNDLE_KEY_CLEAR = "clear";
    public static final String BUNDLE_KEY_ADD = "add";
    public static final String BUNDLE_KEY_SAVE = "save";

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
    public static final int MALE_INDEX = 0;
    public static final int FEMALE_INDEX = 1;

    @Override
    public void init() throws Exception {

        super.init();

        ApplicationPreferences.getInstance().getPreferences().clear();

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

        languageList = new HashMap<>();

        languageList.put("pt", "Português");
        languageList.put("en", "English");

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

            Connection connection = ConnectionFactory.getConnection(dbCredentials);

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

        Parent containerRoot = createNewView(rootWidth, rootHeight, childRoot);

        stage.setScene(new Scene(containerRoot));
        stage.getIcons().add(new Image(getClass().getResourceAsStream(STAGE_ICON_LOCATION)));
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

    public static String getTranslatedStatus(String dbName) {
        String status = "";

        for (ComboBoxItemHelper statusItem : statusList) {
            if (statusItem.getDbName().equals(dbName)) {
                status = getResources().getString(statusItem.getBundleKey());
                break;
            }
        }

        return status;
    }

    public static String getTranslatedGender (String dbName) {
        String gender = "";

        for (ComboBoxItemHelper genderItem : genderList) {
            if (genderItem.getDbName().equals(dbName)) {
                gender = getResources().getString(genderItem.getBundleKey());
                break;
            }
        }

        return gender;
    }

    public static Optional<ButtonType> showAlert(Alert.AlertType alertType, Parent root, String title, String headerText,
                                                 String contentText) {
        Alert alert = new Alert(alertType);
        if (root != null && root.getScene() != null) {
            alert.initOwner(root.getScene().getWindow());
        } else {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(Main.class.getResourceAsStream(STAGE_ICON_LOCATION)));
        }
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        return alert.showAndWait();
    }

    public static void showRequiredFieldsErrorAlert(Parent root) {
        showAlert(Alert.AlertType.ERROR, root,
                getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                getResources().getString(BUNDLE_KEY_ERROR_INVALID_INPUT),
                getResources().getString(BUNDLE_KEY_ERROR_EMPTY_MESSAGE));
    }

    public static void showPasswordMatchErrorAlert(Parent root) {
        showAlert(Alert.AlertType.ERROR,
                root,
                getResources().getString(Main.BUNDLE_KEY_ERROR_ALERT_TITLE),
                getResources().getString(Main.BUNDLE_KEY_ERROR_INVALID_INPUT),
                getResources().getString(BUNDLE_KEY_ERROR_PASSWORD_MATCH));
    }

    public static void showSelectionErrorAlert(Parent root) {
        showAlert(Alert.AlertType.ERROR, root,
                getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                getResources().getString(BUNDLE_KEY_ERROR_INVALID_SELECTION_HEADER),
                getResources().getString(BUNDLE_KEY_ERROR_INVALID_SELECTION_MESSAGE));
    }

    public static void showDatabaseErrorAlert(Parent root) {
        showAlert(Alert.AlertType.ERROR, root,
                getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                getResources().getString(BUNDLE_KEY_ERROR_CONNECTION_LABEL),
                getResources().getString(BUNDLE_KEY_ERROR_DATABASE_MESSAGE));
    }

    public static AnchorPane createNewView(double width, double height, Parent childRoot) {
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

    public static void createNewStage(Parent sceneRoot, Parent ownerRoot) {
        Scene scene = new Scene(sceneRoot);
        Stage newStage = new Stage();
        if (ownerRoot != null) {
            newStage.initOwner(ownerRoot.getScene().getWindow());
        }
        newStage.setTitle(getResources().getString(BUNDLE_KEY_APPLICATION_TITLE));
        newStage.setScene(scene);
        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.getIcons().add(new Image(Main.class.getResourceAsStream(STAGE_ICON_LOCATION)));
        newStage.show();
    }

    public static void createMainViewStage() {

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(MAIN_VIEW_LOCATION), resources);
            Parent mainViewRoot = loader.load();

            Parent root = createNewView(1208, 845, mainViewRoot);

            createNewStage(root, null);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ResourceBundle getResources() {
        return resources;
    }


}
