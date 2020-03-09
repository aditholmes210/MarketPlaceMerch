package com.aditas.marketplacemerch.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RegistErrorRes {
    @SerializedName("email")
    List<String> emailError = new ArrayList<>();

    public List<String> getEmailError() {
        return emailError;
    }
}
