package com.raphaelcollin.appointmentscheduler.db.model;

public interface Exportable {

    String getHeaderLineCSV();
    String convertToCSV();
    String convertToXML();
    String convertToJSON();

}
