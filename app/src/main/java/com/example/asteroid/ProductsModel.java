package com.example.asteroid;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ProductsModel implements Parcelable {

    private String brand, dimension, flavour, img1, img2, name, price, quantity, suggested_use;

    ProductsModel() {} //this is for Firebase

    public ProductsModel(String brand, String dimension, String flavour, String img1, String img2,
                         String name, String price, String quantity, String suggested_use) {
        this.brand = brand;
        this.dimension = dimension;
        this.flavour = flavour;
        this.img1 = img1;
        this.img2 = img2;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.suggested_use = suggested_use;
    }

    protected ProductsModel(Parcel in) {
        brand = in.readString();
        dimension = in.readString();
        flavour = in.readString();
        img1 = in.readString();
        img2 = in.readString();
        name = in.readString();
        price = in.readString();
        quantity = in.readString();
        suggested_use = in.readString();
    }

    public static final Creator<ProductsModel> CREATOR = new Creator<ProductsModel>() {
        @Override
        public ProductsModel createFromParcel(Parcel in) {
            return new ProductsModel(in);
        }

        @Override
        public ProductsModel[] newArray(int size) {
            return new ProductsModel[size];
        }
    };

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getFlavour() {
        return flavour;
    }

    public void setFlavour(String flavour) {
        this.flavour = flavour;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() { return img2; }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuggested_use() {
        return suggested_use;
    }

    public void setSuggested_use(String suggested_use) {
        this.suggested_use = suggested_use;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(brand);
        dest.writeString(dimension);
        dest.writeString(flavour);
        dest.writeString(img1);
        dest.writeString(img2);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(quantity);
        dest.writeString(suggested_use);
    }
}
