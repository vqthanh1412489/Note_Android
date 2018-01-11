package com.hcmus.yennhi0105.usedadvance.CClass;

/**
 * Created by Administrator on 27/03/2017.
 */

public class CNews {
    private String Title;
    private String Picture;
    private String Link;

    public CNews(String title, String picture, String link) {
        Title = title;
        Picture = picture;
        Link = link;
    }

    public CNews() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
