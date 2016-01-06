package com.boredream.hhhgif.entity;

import com.boredream.hhhgif.base.BaseEntity;

/**
 * Created by moyun on 2015/12/29.
 */
public class User extends BaseEntity {

    private String sessionToken;
    private String updatedAt;
    private String username;

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
