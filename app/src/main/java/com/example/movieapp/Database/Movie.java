package com.example.movieapp.Database;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Movie {
    @PrimaryKey
    @NonNull
    private
    String id;

    @ColumnInfo(name="name")
    private
    String name;

    @ColumnInfo(name="release")
    private
    String release;

    @ColumnInfo(name="addTime")
    private
    String addTime ;

    @Ignore
    public Movie(String id) {
        this.id = id;
    }

    public Movie(String id, String name, String release, String addTime) {
        this.id = id;
        this.name = name;
        this.release = release;
        this.addTime = addTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
