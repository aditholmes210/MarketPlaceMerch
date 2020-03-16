package com.aditas.marketplacemerch.Activity;

import com.aditas.marketplacemerch.Entity.Product;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

class ListProd {
    @SerializedName("data")
    private ArrayList<Product> prod;

    public ListProd(ArrayList<Product> prod){
        this.prod = prod;
    }

    public ArrayList<Product> getProd() {
        return prod;
    }
}
