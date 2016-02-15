package com.boredream.hhhgif.entity;

import com.boredream.hhhgif.base.BaseEntity;

public class Gif extends BaseEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 标签
     */
    private String tag;

    /**
     * 搜索图地址
     */
    private String thumbnailImgUrl;

    /**
     * 动态图地址
     */
    private String imgUrl;

    /**
     * 收藏数量
     */
    private int favCount;

    /**
     * 收藏该动态图的用户列表(多对多关系)
     */
    private Relation favUsers;

    /**
     * 评论数量
     */
    private int commentCount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getThumbnailImgUrl() {
        return thumbnailImgUrl;
    }

    public void setThumbnailImgUrl(String thumbnailImgUrl) {
        this.thumbnailImgUrl = thumbnailImgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getFavCount() {
        return favCount;
    }

    public void setFavCount(int favCount) {
        this.favCount = favCount;
    }

    public Relation getFavUsers() {
        return favUsers;
    }

    public void setFavUsers(Relation favUsers) {
        this.favUsers = favUsers;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
