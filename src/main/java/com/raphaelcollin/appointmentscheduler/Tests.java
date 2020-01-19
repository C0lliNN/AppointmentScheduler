package com.raphaelcollin.appointmentscheduler;

import java.time.LocalTime;

public class Tests {
    public static void main(String[] args) {

        LocalTime time = LocalTime.of(0, 30);


        System.out.println(time.getHour() % 12 + ":" + time.getMinute() + " " + (( time.getHour() >= 12) ? "PM" : "AM"));
    }
}
