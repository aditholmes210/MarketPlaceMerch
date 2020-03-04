package com.aditas.marketplacemerch.Entity;

import com.google.gson.annotations.SerializedName;

public class Category{
    @SerializedName("categoryId")
    private long id;
    @SerializedName("categoryName")
    private String name;

    public Category(long id, String name){
        this.id = id;
        this.name = name;
    }
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
