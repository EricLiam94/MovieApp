package com.example.movieapp.Models;

import java.util.Date;

public class Memoir {
    private Integer mid;

    private String MName;

    private Date MRelease;

    private Date watchDate;

    private Double rate;

    private Cinema cid;

    private Users uid;

    public Memoir(String mName, Date mRelease, Date watchDate, Double rate, Cinema cid, Users uid) {
        this.MName = mName;
        this.MRelease = mRelease;
        this.watchDate = watchDate;
        this.rate = rate;
        this.cid = cid;
        this.uid = uid;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getmName() {
        return MName;
    }

    public void setmName(String mName) {
        this.MName = mName;
    }

    public Date getmRelease() {
        return MRelease;
    }

    public void setmRelease(Date mRelease) {
        this.MRelease = mRelease;
    }

    public Date getWatchDate() {
        return watchDate;
    }

    public void setWatchDate(Date watchDate) {
        this.watchDate = watchDate;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Cinema getCid() {
        return cid;
    }

    public void setCid(Cinema cid) {
        this.cid = cid;
    }

    public Users getUid() {
        return uid;
    }

    public void setUid(Users uid) {
        this.uid = uid;
    }
}
