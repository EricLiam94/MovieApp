package com.example.movieapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Credentials implements Parcelable {
    private String username;
    private Date signup = new Date();
    private String password;

    private Users uid;

    public Credentials(String username, String password, Users uid) {
        this.username = username;
        this.password = password;
        this.uid = uid;
    }

    protected Credentials(Parcel in) {
        username = in.readString();
        password = in.readString();
        uid = in.readParcelable(Users.class.getClassLoader());
    }

    public static final Creator<Credentials> CREATOR = new Creator<Credentials>() {
        @Override
        public Credentials createFromParcel(Parcel in) {
            return new Credentials(in);
        }

        @Override
        public Credentials[] newArray(int size) {
            return new Credentials[size];
        }
    };

    public Date getSignup() {
        return signup;
    }

    public void setSignup(Date signup) {
        this.signup = signup;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Users getUid() {
        return uid;
    }

    public void setUid(Users uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeParcelable(uid, flags);
    }
}
