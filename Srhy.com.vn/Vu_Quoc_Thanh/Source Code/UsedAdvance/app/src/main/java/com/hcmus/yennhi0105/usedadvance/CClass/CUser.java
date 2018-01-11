package com.hcmus.yennhi0105.usedadvance.CClass;

import java.io.Serializable;

/**
 * Created by Administrator on 10/03/2017.
 */

public class CUser implements Serializable{
    private String ID;
    private String Username;
    private String Name;
    private String Address;
    private String Phone;
    private String Facebook;
    private String Email;
    private int TypeAccount; // (1) Admin, (2) Member
    private String Avartar;

    public CUser(String ID, String username, String name, String address, String phone, String facebook, String email, int typeAccount, String avartar) {
        this.ID = ID;
        Username = username;
        Name = name;
        Address = address;
        Phone = phone;
        Facebook = facebook;
        Email = email;
        TypeAccount = typeAccount;
        Avartar = avartar;
    }

    public CUser() {

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getFacebook() {
        return Facebook;
    }

    public void setFacebook(String facebook) {
        Facebook = facebook;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getTypeAccount() {
        return TypeAccount;
    }

    public void setTypeAccount(int typeAccount) {
        TypeAccount = typeAccount;
    }

    public String getAvartar() {
        return Avartar;
    }

    public void setAvartar(String avartar) {
        Avartar = avartar;
    }
}
