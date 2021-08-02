package com.yuvalsuede.homefood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cuisine {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cuisine_name")
    @Expose
    private String cuisineName;
    @SerializedName("cuisine_description")
    @Expose
    private Object cuisineDescription;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;
    @SerializedName("lang_he")
    @Expose
    private String langHe;

    public String numberDishes;

    public int count;

    /**
     * No args constructor for use in serialization
     */
    public Cuisine() {
    }

    /**
     * @param imgUrl
     * @param id
     * @param langHe
     * @param cuisineDescription
     * @param cuisineName
     */
    public Cuisine(Integer id, String cuisineName, Object cuisineDescription, String imgUrl, String langHe) {
        this.id = id;
        this.cuisineName = cuisineName;
        this.cuisineDescription = cuisineDescription;
        this.imgUrl = imgUrl;
        this.langHe = langHe;
    }

    public Cuisine(String cuisineName, String imgUrl, String numberDishes) {
        this.cuisineName = cuisineName;
        this.imgUrl = imgUrl;
        this.numberDishes = numberDishes;
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
     * @return The cuisineName
     */
    public String getCuisineName() {
        return cuisineName;
    }

    /**
     * @param cuisineName The cuisine_name
     */
    public void setCuisineName(String cuisineName) {
        this.cuisineName = cuisineName;
    }

    /**
     * @return The cuisineDescription
     */
    public Object getCuisineDescription() {
        return cuisineDescription;
    }

    /**
     * @param cuisineDescription The cuisine_description
     */
    public void setCuisineDescription(Object cuisineDescription) {
        this.cuisineDescription = cuisineDescription;
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

}

