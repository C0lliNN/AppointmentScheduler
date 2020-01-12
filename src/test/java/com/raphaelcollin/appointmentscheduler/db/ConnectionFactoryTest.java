package com.raphaelcollin.appointmentscheduler.db;

import com.raphaelcollin.appointmentscheduler.DatabaseCredentials;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

class ConnectionFactoryTest {

    @Test
    void testConnection() {

        DatabaseCredentials credentials = new DatabaseCredentials("127.0.0.1", "3307", "root", "");

        Connection connection = ConnectionFactory.getConnection(credentials);
        Assertions.assertNotNull(connection);

        Assertions.assertTrue(ConnectionFactory.closeConnection(connection));

        credentials = new DatabaseCredentials("321.543.23.234", "3212", "masdf2", "2dfas");

        connection = ConnectionFactory.getConnection(credentials);
        Assertions.assertNull(connection);

        Assertions.assertFalse(ConnectionFactory.closeConnection(connection));

    }
}