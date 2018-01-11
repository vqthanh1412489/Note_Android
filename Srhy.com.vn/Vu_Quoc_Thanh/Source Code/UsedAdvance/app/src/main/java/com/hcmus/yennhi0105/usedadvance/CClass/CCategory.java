package com.hcmus.yennhi0105.usedadvance.CClass;

import java.io.Serializable;

/**
 * Created by Administrator on 14/03/2017.
 */

public class CCategory implements Serializable {
    private String Key;
    private String ID;
    private String Name;
    private String Icon;

    public CCategory(String key, String ID, String name, String icon) {
        Key = key;
        this.ID = ID;
        Name = name;
        Icon = icon;
    }

    public CCategory() {
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
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

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }
}
