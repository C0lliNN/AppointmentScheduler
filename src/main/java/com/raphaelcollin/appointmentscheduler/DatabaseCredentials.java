package com.raphaelcollin.appointmentscheduler;

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
}
