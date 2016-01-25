package com.boredream.hhhgif.entity;

/**
 * 更多item信息
 */
public class MoreItem {

    /**
     * 左侧图片
     */
    public int leftImgRes;

    /**
     * 中间文字
     */
    public String midText;

    /**
     * 右侧文字
     */
    public String rightText;

    public MoreItem() {

    }

    public MoreItem(int leftImgRes, String midText) {
        this.leftImgRes = leftImgRes;
        this.midText = midText;
    }

}
