package com.example.productivitytracker;

public class DataModel {
    private long id;
    private final String description;
    private final int duration;

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public DataModel(String description, int duration) {
        this.description = description;
        this.duration = duration;
    }
}
