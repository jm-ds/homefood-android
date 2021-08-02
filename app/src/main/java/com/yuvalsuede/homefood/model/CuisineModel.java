package com.yuvalsuede.homefood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CuisineModel {

    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("cuisine_id")
    @Expose
    private Integer cuisineId;

    /**
     * @return The count
     */
    public String getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(String count) {
        this.count = count;
    }

    /**
     * @return The cuisineId
     */
    public Integer getCuisineId() {
        return cuisineId;
    }

    /**
     * @param cuisineId The cuisine_id
     */
    public void setCuisineId(Integer cuisineId) {
        this.cuisineId = cuisineId;
    }

}
