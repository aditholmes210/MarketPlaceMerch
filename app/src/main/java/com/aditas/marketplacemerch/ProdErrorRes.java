package com.aditas.marketplacemerch;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductErrorResponse {
    @SerializedName("productName")
    List<String> prodNameError = new ArrayList<>();
    @SerializedName("productQty")
    List<String> prodQtyError = new ArrayList<>();
    @SerializedName("productPrice")
    List<String> prodPriceError = new ArrayList<>();

    public List<String> getProdNameError() {
        return prodNameError;
    }

    public List<String> getProdQtyError() {
        return prodQtyError;
    }

    public List<String> getProdPriceError() {
        return prodPriceError;
    }
}
