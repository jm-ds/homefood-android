package com.yuvalsuede.homefood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Filter {
    @SerializedName("category_id")
    @Expose
    private String categoryId;

    @SerializedName("cuisine_id")
    @Expose
    private String cuisineId;

    @SerializedName("delivery")
    @Expose
    private String delivery;

    @SerializedName("kosher")
    @Expose
    private String kosher;

    @SerializedName("contains_milk")
    @Expose
    private String containesMilk;

    @SerializedName("glat")
    @Expose
    private String glat;

    @SerializedName("vegeterian")
    @Expose
    private String vegeterian;

    @SerializedName("vegan")
    @Expose
    private String vegan;

    @SerializedName("gluten_free")
    @Expose
    private String glutenFree;

    @SerializedName("sugar_free")
    @Expose
    private String sugarFree;

    @SerializedName("main_course")
    @Expose
    private String mainCourse;

    @SerializedName("distance")
    @Expose
    private String distance;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCuisineId() {
        return cuisineId;
    }

    public void setCuisineId(String cuisineId) {
        this.cuisineId = cuisineId;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getContainesMilk() {
        return containesMilk;
    }

    public void setContainesMilk(String containesMilk) {
        this.containesMilk = containesMilk;
    }

    public String getGlat() {
        return glat;
    }

    public void setGlat(String glat) {
        this.glat = glat;
    }

    public String getVegeterian() {
        return vegeterian;
    }

    public void setVegeterian(String vegeterian) {
        this.vegeterian = vegeterian;
    }

    public String getVegan() {
        return vegan;
    }

    public void setVegan(String vegan) {
        this.vegan = vegan;
    }

    public String getGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(String glutenFree) {
        this.glutenFree = glutenFree;
    }

    public String getSugarFree() {
        return sugarFree;
    }

    public void setSugarFree(String sugarFree) {
        this.sugarFree = sugarFree;
    }

    public String getMainCourse() {
        return mainCourse;
    }

    public void setMainCourse(String mainCourse) {
        this.mainCourse = mainCourse;
    }

    public String getKosher() {
        return kosher;
    }

    public void setKosher(String kosher) {
        this.kosher = kosher;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
