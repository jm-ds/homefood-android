package com.yuvalsuede.homefood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DishCategory {

    @SerializedName("dish_id")
    @Expose
    private Integer dishId;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;

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

    /**
     * @return The categoryId
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId The category_id
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

}
