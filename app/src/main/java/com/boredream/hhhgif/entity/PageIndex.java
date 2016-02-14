package com.boredream.hhhgif.entity;

/**
 * page no. for multi page load index
 */
public class PageIndex {

    /**
     * PageIndex
     *
     * @param startPageIndex usually 0 or 1
     */
    public PageIndex(int startPageIndex) {
        this.startPageIndex = startPageIndex;
        this.currentPage = startPageIndex;
    }

    /**
     * start page
     */
    public int startPageIndex;

    /**
     * loaded page
     */
    public int currentPage;

    /**
     * new page
     */
    public int newPage;

    /**
     * get newPage success, update currentPage with newPage
     */
    public void success() {
        currentPage = newPage;
    }

    /**
     * to next page
     * @return next page index
     */
    public int getNext() {
        newPage = currentPage + 1;
        return newPage;
    }

}
