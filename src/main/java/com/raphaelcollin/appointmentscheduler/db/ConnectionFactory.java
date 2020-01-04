package com.raphaelcollin.appointmentscheduler.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_NAME = "scheduler";

    public static Connection getConnection(String ipAddress, String port, String user, String password) {
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mysql://");
        url.append(ipAddress).append(":");
        url.append(port).append("/");
        url.append(DB_NAME);
        url.append("?useTimezone=true&serverTimezone=UTC&useSSL=false");
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(url.toString(), user, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

}
