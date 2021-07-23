package com.example.tsnews;

public class model
{
    String header, image, link,time;

    public model() {
    }

    public model(String header, String image, String link,String time) {
        this.header = header;
        this.image = image;
        this.link = link;
        this.time=time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
