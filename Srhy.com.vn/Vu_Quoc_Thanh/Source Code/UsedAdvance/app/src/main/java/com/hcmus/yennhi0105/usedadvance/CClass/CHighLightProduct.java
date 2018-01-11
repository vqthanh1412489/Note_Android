package com.hcmus.yennhi0105.usedadvance.CClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 23/03/2017.
 */

public class CHighLightProduct implements Serializable, Parcelable {
    private String ID;
    private String Name;
    private int Price;
    private String Description;
    private String NameCompany;
    private String Phone;
    private String Email;
    private String Address;
    private String DataPost;
    private String linkVideo;
    private int StatusSell;

    public CHighLightProduct(String ID, String name, int price, String description, String nameCompany, String phone, String email, String address, String dataPost, String linkVideo, int statusSell) {
        this.ID = ID;
        Name = name;
        Price = price;
        Description = description;
        NameCompany = nameCompany;
        Phone = phone;
        Email = email;
        Address = address;
        DataPost = dataPost;
        this.linkVideo = linkVideo;
        StatusSell = statusSell;
    }

    public CHighLightProduct() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getNameCompany() {
        return NameCompany;
    }

    public void setNameCompany(String nameCompany) {
        NameCompany = nameCompany;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDataPost() {
        return DataPost;
    }

    public void setDataPost(String dataPost) {
        DataPost = dataPost;
    }

    public String getLinkVideo() {
        return linkVideo;
    }

    public void setLinkVideo(String linkVideo) {
        this.linkVideo = linkVideo;
    }

    public int getStatusSell() {
        return StatusSell;
    }

    public void setStatusSell(int statusSell) {
        StatusSell = statusSell;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(Name);
        dest.writeInt(Price);
        dest.writeString(Description);
        dest.writeString(NameCompany);
        dest.writeString(Phone);
        dest.writeString(Email);
        dest.writeString(Address);
        dest.writeString(DataPost);
        dest.writeString(linkVideo);
        dest.writeInt(StatusSell);
    }

    public static final Parcelable.Creator<CHighLightProduct> CREATOR
            = new Parcelable.Creator<CHighLightProduct>() {
        public CHighLightProduct createFromParcel(Parcel in) {
            return new CHighLightProduct(in);
        }

        public CHighLightProduct[] newArray(int size) {
            return new CHighLightProduct[size];
        }
    };

    private CHighLightProduct(Parcel in) {
        ID = in.readString();
        Name = in.readString();
        Price = in.readInt();
        Description = in.readString();
        NameCompany = in.readString();
        Phone = in.readString();
        Email = in.readString();
        Address = in.readString();
        DataPost = in.readString();
        linkVideo = in.readString();
        StatusSell = in.readInt();
    }
}
