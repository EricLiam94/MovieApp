package com.example.movieapp.Models;

import androidx.annotation.NonNull;

public class Cinema {
    private Integer cid;
    private String cname;
    private String location;

    public Cinema(Integer cid, String cname, String location) {
        this.cid = cid;
        this.cname = cname;
        this.location = location;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @NonNull
    @Override
    public String toString() {
        return cname + " " + location;
    }
}
