package com.example.asteroid;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserAddress implements Parcelable {

    private String fullName, addressLine, flatNumber, postcode, city;

    UserAddress(){};

    public UserAddress(String fullName, String addressLine, String flatNumber, String postcode, String city) {
        this.fullName = fullName;
        this.addressLine = addressLine;
        this.flatNumber = flatNumber;
        this.postcode = postcode;
        this.city = city;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
