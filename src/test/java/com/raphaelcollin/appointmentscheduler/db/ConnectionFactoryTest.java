package com.raphaelcollin.appointmentscheduler.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionFactoryTest {

    @Test
    void testConnection() {

        Connection connection = ConnectionFactory.getConnection("127.0.0.1", "3307", "root", "");
        Assertions.assertNotNull(connection);

        Assertions.assertTrue(ConnectionFactory.closeConnection(connection));

        connection = ConnectionFactory.getConnection("321.543.23.234", "3212", "masdf2", "2dfas");
        Assertions.assertNull(connection);

        Assertions.assertFalse(ConnectionFactory.closeConnection(connection));

    }
}