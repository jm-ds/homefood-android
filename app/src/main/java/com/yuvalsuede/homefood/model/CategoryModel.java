package com.yuvalsuede.homefood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryModel {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;

    /**
     * @return The count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(int count) {
        this.count = count;
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
