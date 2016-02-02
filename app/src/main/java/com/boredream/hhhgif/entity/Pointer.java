package com.boredream.hhhgif.entity;

import java.io.Serializable;

public class Pointer implements Serializable {
    private static final long serialVersionUID = -2906907910428442090L;
    private String __type = "Pointer";
    private String className;
    private String objectId;

    public Pointer() {
    }

    public Pointer(String className, String objectId) {
        this.setClassName(className);
        this.setObjectId(objectId);
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getObjectId() {
        return this.objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

}
