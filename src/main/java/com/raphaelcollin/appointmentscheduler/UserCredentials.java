package com.raphaelcollin.appointmentscheduler;

import static com.raphaelcollin.appointmentscheduler.ApplicationPreferences.*;

public class UserCredentials {

    private String user;
    private String password;
    private String securityQuestion;
    private String answer;

    private UserCredentials(String user, String password, String securityQuestion, String answer) {
        this.user = user;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.answer = answer;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public static void saveCredentials(String user, String password, String securityQuestion, String answer) {
        ApplicationPreferences.getInstance().getPreferences().putBoolean(PREFERENCES_KEY_ACCESS_CONTROL, true);
        ApplicationPreferences.getInstance().getPreferences().put(PREFERENCES_KEY_ACCESS_CONTROL_USER, user);
        ApplicationPreferences.getInstance().getPreferences().put(PREFERENCES_KEY_ACCESS_CONTROL_PASSWORD, password);
        ApplicationPreferences.getInstance().getPreferences().put(PREFERENCES_KEY_ACCESS_CONTROL_SECURITY_QUESTION, securityQuestion);
        ApplicationPreferences.getInstance().getPreferences().put(PREFERENCES_KEY_ACCESS_CONTROL_ANSWER, answer);
    }

    public static UserCredentials getSavedCredentials() {
        boolean accessControl = ApplicationPreferences.getInstance().getPreferences().getBoolean(PREFERENCES_KEY_ACCESS_CONTROL, false);

        UserCredentials credentials = null;

        if (accessControl) {
            String user = ApplicationPreferences.getInstance().getPreferences().get(PREFERENCES_KEY_ACCESS_CONTROL_USER, null);
            String password = ApplicationPreferences.getInstance().getPreferences().get(PREFERENCES_KEY_ACCESS_CONTROL_PASSWORD, null);
            String securityQuestion = ApplicationPreferences.getInstance().getPreferences().get(PREFERENCES_KEY_ACCESS_CONTROL_SECURITY_QUESTION, null);
            String answer = ApplicationPreferences.getInstance().getPreferences().get(PREFERENCES_KEY_ACCESS_CONTROL_ANSWER, null);
            credentials = new UserCredentials(user, password, securityQuestion, answer);
        }

        return credentials;

    }
}
