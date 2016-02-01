package com.boredream.hhhgif.entity;

import java.util.List;

public class SearchListResponse<T> {

    private String searchKey;

    private List<T> results;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
