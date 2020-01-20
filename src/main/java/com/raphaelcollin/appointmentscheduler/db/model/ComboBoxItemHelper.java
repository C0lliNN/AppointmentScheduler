package com.raphaelcollin.appointmentscheduler.db.model;

/* This class was created to ease code Internationalization */

public class ComboBoxItemHelper {
    private final String dbName;
    private final int comboBoxIndex;
    private final String bundleKey;

    public ComboBoxItemHelper(String dbName, int comboBoxIndex, String bundleKey) {
        this.dbName = dbName;
        this.comboBoxIndex = comboBoxIndex;
        this.bundleKey = bundleKey;
    }

    public String getDbName() {
        return dbName;
    }

    public int getComboBoxIndex() {
        return comboBoxIndex;
    }

    public String getBundleKey() {
        return bundleKey;
    }
}
