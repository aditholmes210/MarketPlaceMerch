package com.aditas.marketplacemerch.Entity;

import com.google.gson.annotations.SerializedName;

public class Merchant {
    @SerializedName("merchantId")
    private long id;
    @SerializedName("merchantName")
    private String name;
    @SerializedName("merchantSlug")
    private String slug;

    public Merchant(long id, String name, String slug){
        this.id = id;
        this.name = name;
        this.slug = slug;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }
}
