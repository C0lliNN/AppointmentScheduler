package com.raphaelcollin.appointmentscheduler.db.dao;

import javafx.collections.ObservableList;

import java.sql.Connection;

public abstract class DAO<T> {

    protected Connection connection;

    public DAO(Connection connection) {

        this.connection = connection;

    }

    public abstract int add(T item);
    public abstract ObservableList<T> getAll();
    public abstract boolean update(T item);
    public abstract boolean delete(int id);
}
