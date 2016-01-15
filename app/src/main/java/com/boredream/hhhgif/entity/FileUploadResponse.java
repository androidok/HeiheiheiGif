package com.boredream.hhhgif.entity;

public class FileUploadResponse {
    private String filename;
    private String group;
    private String url;

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public String getGroup() {
        return group;
    }

    public String getUrl() {
        return url;
    }
}
