package com.example.nguyenduchai.mydiary.model;

/**
 * Created by Nguyen Duc Hai on 4/11/2017.
 */

public class NoteModel {
    private int id;
    private String title;
    private String content;
    private String image;
    private String datetime;

    public NoteModel() {
    }

    public NoteModel(int id, String title, String content, String image, String datetime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
