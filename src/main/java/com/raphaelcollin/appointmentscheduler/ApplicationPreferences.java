package com.raphaelcollin.appointmentscheduler;

import java.util.prefs.Preferences;

public class ApplicationPreferences {

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

    private Preferences preferences;

    private ApplicationPreferences(){
        preferences = Preferences.userNodeForPackage(Main.class);
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public static ApplicationPreferences getInstance() {
        return SingletonHelper.instance;
    }

    public static class SingletonHelper {
        private static ApplicationPreferences instance = new ApplicationPreferences();
    }

}
