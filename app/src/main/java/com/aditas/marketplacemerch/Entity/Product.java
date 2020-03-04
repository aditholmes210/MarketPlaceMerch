package com.aditas.marketplacemerch.Entity;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("productId")
    private long id;
    @SerializedName("productQty")
    private int qty;
    @SerializedName("productName")
    private String name;
    @SerializedName("productSlug")
    private String slug;
    @SerializedName("productImage")
    private String image;
    @SerializedName("merchant")
    private Merchant merch;
    @SerializedName("category")
    private Category catg;
    @SerializedName("productPrice")
    private int price;
    @SerializedName("productDesc")
    private String desc;

    public Product(long id, int qty, String name, String slug, String image, Merchant merch, Category catg, int price, String desc){
        this.id = id;
        this.qty = qty;
        this.name = name;
        this.slug = slug;
        this.image = image;
        this.merch = merch;
        this.catg = catg;
        this.price = price;
        this.desc = desc;
    }

    public long getId() {
        return id;
    }

    public int getQty() {
        return qty;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getImage() {
        return image;
    }

    public Merchant getMerch() {
        return merch;
    }

    public Category getCatg() {
        return catg;
    }

    public int getPrice(){
        return price;
    }

    public String getDesc(){
        return desc;
    }

    public static Product fromJson(String json){
        Gson gson = new Gson();
        Product product = gson.fromJson(json, Product.class);
        return product;
    }
}
