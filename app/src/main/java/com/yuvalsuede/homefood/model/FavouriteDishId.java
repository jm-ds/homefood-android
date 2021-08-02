package com.yuvalsuede.homefood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavouriteDishId {

    @SerializedName("dish_id")
    @Expose
    private Integer dishId;


    public FavouriteDishId(Integer dishId) {
        this.dishId = dishId;
    }

    /**
     * @return The dishId
     */
    public Integer getDishId() {
        return dishId;
    }

    /**
     * @param dishId The dish_id
     */
    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }

}
