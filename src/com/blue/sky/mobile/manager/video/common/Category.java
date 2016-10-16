package com.blue.sky.mobile.manager.video.common;


public class Category {

    private String categoryId;
    private String categoryName;

    public Category(){

    }

    public Category(String id, String name){
        this.categoryId  = id;
        this.categoryName = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
