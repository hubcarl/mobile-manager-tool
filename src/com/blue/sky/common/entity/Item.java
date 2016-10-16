package com.blue.sky.common.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2014/11/16.
 */
public class Item implements Serializable{

    public String getItemOne() {
        return itemOne;
    }

    public void setItemOne(String itemOne) {
        this.itemOne = itemOne;
    }

    public String getItemTwo() {
        return itemTwo;
    }

    public void setItemTwo(String itemTwo) {
        this.itemTwo = itemTwo;
    }

    public String getItemThree() {
        return itemThree;
    }

    public void setItemThree(String itemThree) {
        this.itemThree = itemThree;
    }

    public String getItemFour() {
        return itemFour;
    }

    public void setItemFour(String itemFour) {
        this.itemFour = itemFour;
    }

    public String getItemFive() {
        return itemFive;
    }

    public void setItemFive(String itemFive) {
        this.itemFive = itemFive;
    }

    private String itemOne;

    private String itemTwo;

    private String itemThree;

    private String itemFour;

    private String itemFive;

}
