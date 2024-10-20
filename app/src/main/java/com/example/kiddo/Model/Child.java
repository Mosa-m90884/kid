package com.example.kiddo.Model;

public class Child {
    private String id;

    private String gendar;
    private String old;
    private String imageUrl; // رابط الصورة

    private String username;
    private String email;
    private String parentId; // معرف الأب

    public Child() {
        // مطلوب لمكتبة Firebase
    }

    public Child(String username, String imageUrl, String old, String gendar) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.old = old;
        this.gendar = gendar;
    }

    public Child(String username, String email, String parentId) {
        this.username = username;
        this.email = email;
        this.parentId = parentId;
    }


    public String getGendar() {
        return gendar;
    }

    public void setGendar(String gendar) {
        this.gendar = gendar;
    }

    public String getOld() {
        return old;
    }

    public void setOld(String old) {
        this.old = old;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}