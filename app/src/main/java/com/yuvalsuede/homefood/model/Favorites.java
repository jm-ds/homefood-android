package com.yuvalsuede.homefood.model;

import com.google.gson.annotations.SerializedName;

public class Favorites {

    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private Favorite data;

    public Favorite getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public void setData(Favorite data) {
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
