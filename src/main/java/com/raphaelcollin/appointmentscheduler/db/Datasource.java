package com.raphaelcollin.appointmentscheduler.db;

public class Datasource {

    private Datasource() {

    }

    private static class SingletonHelper {
        private static Datasource instance = new Datasource();
    }

    public static Datasource getInstance() {
        return SingletonHelper.instance;
    }

}
