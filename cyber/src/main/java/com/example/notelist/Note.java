package com.example.notelist;

public class Note {

    String date;
    String content;
    String id;

    public Note(String id, String date, String content) {
        this.date = date;
        this.content = content;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
