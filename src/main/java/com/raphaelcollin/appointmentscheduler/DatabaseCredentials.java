package com.raphaelcollin.appointmentscheduler;

import static com.raphaelcollin.appointmentscheduler.ApplicationPreferences.*;
import static com.raphaelcollin.appointmentscheduler.ApplicationPreferences.PREFERENCES_KEY_DB_PASSWORD;

public class DatabaseCredentials {
    private String ip;
    private String port;
    private String user;
    private String password;

    public DatabaseCredentials(String ip, String port, String user, String password) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public static void saveCredentials(String ip, String port, String user, String password) {
        ApplicationPreferences.getInstance().getPreferences().putBoolean(PREFERENCES_KEY_DB_SETUP, true);
        ApplicationPreferences.getInstance().getPreferences().put(PREFERENCES_KEY_IP, ip);
        ApplicationPreferences.getInstance().getPreferences().put(PREFERENCES_KEY_PORT, port);
        ApplicationPreferences.getInstance().getPreferences().put(PREFERENCES_KEY_DB_USER, user);
        ApplicationPreferences.getInstance().getPreferences().put(PREFERENCES_KEY_DB_PASSWORD, password);
    }

    public static DatabaseCredentials getSavedCredentials() {
        String dbIp = ApplicationPreferences.getInstance().getPreferences().get(PREFERENCES_KEY_IP, null);
        String dbPort = ApplicationPreferences.getInstance().getPreferences().get(PREFERENCES_KEY_PORT, null);
        String dbUser = ApplicationPreferences.getInstance().getPreferences().get(PREFERENCES_KEY_DB_USER, null);
        String dbPassword = ApplicationPreferences.getInstance().getPreferences().get(PREFERENCES_KEY_DB_PASSWORD, null);

        return new DatabaseCredentials(dbIp, dbPort, dbUser, dbPassword);
    }
}
