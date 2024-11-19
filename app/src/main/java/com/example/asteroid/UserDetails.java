package com.example.asteroid;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserDetails implements Parcelable {

    String username, email, mobile;

    UserDetails(){};

    public UserDetails(String email, String mobile, String username) {
        this.email = email;
        this.mobile = mobile;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
