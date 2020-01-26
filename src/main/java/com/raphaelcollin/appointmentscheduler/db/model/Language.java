package com.raphaelcollin.appointmentscheduler.db.model;

public class Language {

    private String abbreviation;
    private String completeName;

    public Language(String abbreviation, String completeName) {
        this.abbreviation = abbreviation;
        this.completeName = completeName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getCompleteName() {
        return completeName;
    }
}
