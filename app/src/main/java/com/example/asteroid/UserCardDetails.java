package com.example.asteroid;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserCardDetails implements Parcelable {
    String cardName, cardNumber, expMM, expYY, cvv;

    UserCardDetails(){};

    public UserCardDetails(String cardName, String cardNumber, String expMM, String expYY, String cvv) {
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.expMM = expMM;
        this.expYY = expYY;
        this.cvv = cvv;

    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpMM() {
        return expMM;
    }

    public void setExpMM(String expMM) {
        this.expMM = expMM;
    }

    public String getExpYY() {
        return expYY;
    }

    public void setExpYY(String expYY) {
        this.expYY = expYY;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
