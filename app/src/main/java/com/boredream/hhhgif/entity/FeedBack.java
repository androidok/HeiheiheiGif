package com.boredream.hhhgif.entity;

import com.boredream.hhhgif.base.BaseEntity;

public class FeedBack extends BaseEntity {

    private String content;
    private String email;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}