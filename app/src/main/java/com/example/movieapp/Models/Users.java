package com.example.movieapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Users implements Parcelable {
    private int uid;
    private String uname;
    private String surname;

    private Character gender;
    private Date dob;
    private String address;
    private String postcode;
    private String ustate;


    protected Users(Parcel in) {
        uid = in.readInt();
        uname = in.readString();
        surname = in.readString();
        int tmpGender = in.readInt();
        gender = tmpGender != Integer.MAX_VALUE ? (char) tmpGender : null;
        address = in.readString();
        postcode = in.readString();
        ustate = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public int getId() {
        return uid;
    }

    public void setId(int id) {
        this.uid = id;
    }

    public Users(String uname, String surname, Character gender, Date dob, String address, String postcode, String ustate) {
        this.uname = uname;
        this.surname = surname;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.postcode = postcode;
        this.ustate = ustate;
    }



    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getUstate() {
        return ustate;
    }

    public void setUstate(String ustate) {
        this.ustate = ustate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(uname);
        dest.writeString(surname);
        dest.writeInt(gender != null ? (int) gender : Integer.MAX_VALUE);
        dest.writeString(address);
        dest.writeString(postcode);
        dest.writeString(ustate);
    }
}
