package com.boredream.hhhgif.entity;

public class IncrementOption extends Operation {

    public IncrementOption() {
        set__op(OP_INCREMENT);
    }

    /**
     * 计算使用数值
     */
    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
