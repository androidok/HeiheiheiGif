package com.boredream.hhhgif.base;

public class BaseEntity {

    private String createdAt;
    private String objectId;

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "createdAt='" + createdAt + '\'' +
                ", objectId='" + objectId + '\'' +
                '}';
    }
}
