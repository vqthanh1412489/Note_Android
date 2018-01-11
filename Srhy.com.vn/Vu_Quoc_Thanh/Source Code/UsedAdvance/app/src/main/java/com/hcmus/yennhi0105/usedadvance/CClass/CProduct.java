package com.hcmus.yennhi0105.usedadvance.CClass;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 14/03/2017.
 */

public class CProduct implements Serializable{
    private String ID;
    private String Title;
    private String Category;
    private int Price;
    private String Descritption;
    private String Zone;
    private int Type; // Cần bán (1), cần mua(2), cần cho(3) /// Do Số 0 bị lấy làm đề mục
    private int Status; // Mới (1), qua sử dụng(2)
    private int StatusSell; // Public (1), Private (2)
    private ArrayList<String> arrayIcon;
    private String User; // Key trên Firebase

    public CProduct(String ID, String title, String category, int price, String descritption, String zone, int type, int status, int statusSell, ArrayList<String> arrayIcon, String user) {
        this.ID = ID;
        Title = title;
        Category = category;
        Price = price;
        Descritption = descritption;
        Zone = zone;
        Type = type;
        Status = status;
        StatusSell = statusSell;
        this.arrayIcon = arrayIcon;
        User = user;
    }

    public CProduct() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getDescritption() {
        return Descritption;
    }

    public void setDescritption(String descritption) {
        Descritption = descritption;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String zone) {
        Zone = zone;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getStatusSell() {
        return StatusSell;
    }

    public void setStatusSell(int statusSell) {
        StatusSell = statusSell;
    }

    public ArrayList<String> getArrayIcon() {
        return arrayIcon;
    }

    public void setArrayIcon(ArrayList<String> arrayIcon) {
        this.arrayIcon = arrayIcon;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }
}
