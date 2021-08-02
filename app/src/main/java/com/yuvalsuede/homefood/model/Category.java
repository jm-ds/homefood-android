package com.yuvalsuede.homefood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_description")
    @Expose
    private Object categoryDescription;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;
    @SerializedName("category_name_he")
    @Expose
    private String categoryNameHe;
    @SerializedName("lang_he")
    @Expose
    private String langHe;

    private int count;

    public Category(String categoryName, String imgUrl) {
        this.categoryName = categoryName;
        this.imgUrl = imgUrl;
    }

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The categoryName
     */
    public String getCategoryName() {
        return categoryName.substring(0, 1).toUpperCase() + categoryName.substring(1);
    }

    /**
     * @param categoryName The category_name
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return The categoryDescription
     */
    public Object getCategoryDescription() {
        return categoryDescription;
    }

    /**
     * @param categoryDescription The category_description
     */
    public void setCategoryDescription(Object categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    /**
     * @return The imgUrl
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * @param imgUrl The img_url
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * @return The categoryNameHe
     */
    public String getCategoryNameHe() {
        return categoryNameHe;
    }

    /**
     * @param categoryNameHe The category_name_he
     */
    public void setCategoryNameHe(String categoryNameHe) {
        this.categoryNameHe = categoryNameHe;
    }

    /**
     * @return The langHe
     */
    public String getLangHe() {
        return langHe;
    }

    /**
     * @param langHe The lang_he
     */
    public void setLangHe(String langHe) {
        this.langHe = langHe;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}