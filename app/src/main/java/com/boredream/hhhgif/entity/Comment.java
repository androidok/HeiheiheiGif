package com.boredream.hhhgif.entity;

import com.boredream.hhhgif.base.BaseEntity;

public class Comment extends BaseEntity {
    /**
     * 所属动态图id
     */
    private String gifId;

    /**
     * 发送用户id
     */
    private String userId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
