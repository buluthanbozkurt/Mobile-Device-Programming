package com.example.runningtracker;

//methods about the run session
//name, activity type, notes, rating,

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "run_session_table")
public class RunSession {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    private String name;
    private String type;
    private String notes;
    private String rating;

    public RunSession(@NonNull String name, String type, String notes, String rating) {
        this.name = name;
        this.type = type;
        this.notes = notes;
        this.rating = rating;
    }

    public String getType() {

        return type;
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public String getRating() { return rating; }
}
