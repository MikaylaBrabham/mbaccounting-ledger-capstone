package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
    private String date;
    private String time;
    private String name;
    private String entity;
    private double amount;

    public Transaction(String date, String time, String name, String entity, double amount) {
        this.date = date;
        this.time = time;
        this.name = name;
        this.entity = entity;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getEntity() {
        return entity;
    }

    public double getAmount() {
        return amount;
    }
}
