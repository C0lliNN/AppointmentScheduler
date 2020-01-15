package com.raphaelcollin.appointmentscheduler.db;

import com.raphaelcollin.appointmentscheduler.DatabaseCredentials;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_NAME = "scheduler";

    public static Connection getConnection(DatabaseCredentials dbCredentials) {
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mysql://");
        url.append(dbCredentials.getIp()).append(":");
        url.append(dbCredentials.getPort()).append("/");
        url.append(DB_NAME);
        url.append("?useTimezone=true&serverTimezone=UTC&useSSL=false");

        Connection connection = null;

        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(url.toString(), dbCredentials.getUser(), dbCredentials.getPassword());
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Exception in Connection Factory - getConnection - " + e.getMessage());
        }

        return connection;
    }

    public static boolean closeConnection(Connection connection) {

        boolean closed = false;

        if (connection != null) {
            try {
                connection.close();
                closed = true;
            } catch (SQLException e) {
                System.err.println("Exception in Connection Factory - closeConnection - " + e.getMessage());
            }
        }
        return closed;
    }

}
