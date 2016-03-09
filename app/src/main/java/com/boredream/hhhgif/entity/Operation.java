package com.boredream.hhhgif.entity;

/**
 * 原子计算请求
 */
public class Operation {

    /**
     * 自增
     */
    public static final String OP_INCREMENT = "Increment";

    /**
     * 计算类型
     */
    private String __op;

    public String get__op() {
        return __op;
    }

    public void set__op(String __op) {
        this.__op = __op;
    }
}
