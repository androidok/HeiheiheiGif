package com.boredream.hhhgif.entity;

import com.boredream.hhhgif.base.BaseEntity;
import com.google.gson.Gson;

public class Comment extends BaseEntity {
    /**
     * 所属动态图id
     */
    private String gifId;

    /**
     * 发送用户, Pointer or User
     */
    private Object user;

    /**
     * 评论内容
     */
    private String content;

    public String getGifId() {
        return gifId;
    }

    public void setGifId(String gifId) {
        this.gifId = gifId;
    }

    public User getUser() {
        // 发送时作为Pointer,而获取时,则可以解析为目标对象.注意必须要包含include才能获取完整参数
        Gson gson = new Gson();
        String json = gson.toJson(user);
        User user = gson.fromJson(json, User.class);
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
