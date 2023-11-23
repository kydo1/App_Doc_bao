package com.example.myapplication.modal;

import java.io.Serializable;
import java.util.ArrayList;

public class Story  implements Serializable {
    private String _id;
    private String name;
    private  String description;
    private  String author;
    private  String time;
    private String image;
    private ArrayList<String> content;

    public String getTheloai() {
        return theloai;
    }

    public void setTheloai(String theloai) {
        this.theloai = theloai;
    }

    private String theloai;

    public Story() {
    }

    public Story(String _id, String name, String description, String author, String time, String image, ArrayList<String> content,String theloai) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.author = author;
        this.time = time;
        this.image = image;
        this.content = content;
        this.theloai = theloai;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Story{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", time='" + time + '\'' +
                ", image='" + image + '\'' +
                ", content=" + content +
                '}';
    }
}
