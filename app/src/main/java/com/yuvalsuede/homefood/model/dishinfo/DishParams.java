
package com.yuvalsuede.homefood.model.dishinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DishParams {

    @SerializedName("dish_id")
    @Expose
    private Integer dishId;
    @SerializedName("delivery")
    @Expose
    private Boolean delivery;
    @SerializedName("cold_warm")
    @Expose
    private float coldWarm;
    @SerializedName("weight")
    @Expose
    private float weight;
    @SerializedName("num_of_pieces")
    @Expose
    private Integer numOfPieces;
    @SerializedName("num_of_people")
    @Expose
    private Integer numOfPeople;
    @SerializedName("kosher")
    @Expose
    private Boolean kosher;
    @SerializedName("containes_milk")
    @Expose
    private Boolean containesMilk;
    @SerializedName("vegeterian")
    @Expose
    private Boolean vegeterian;
    @SerializedName("vegan")
    @Expose
    private Boolean vegan;
    @SerializedName("gluten_free")
    @Expose
    private Boolean glutenFree;
    @SerializedName("sugar_free")
    @Expose
    private Boolean sugarFree;
    @SerializedName("main_course")
    @Expose
    private Boolean mainCourse;
    @SerializedName("spicy_level")
    @Expose
    private float spicyLevel;
    @SerializedName("glat")
    @Expose
    private Boolean glat;

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
     * @return The delivery
     */
    public Boolean getDelivery() {
        return delivery;
    }

    /**
     * @param delivery The delivery
     */
    public void setDelivery(Boolean delivery) {
        this.delivery = delivery;
    }

    /**
     * @return The coldWarm
     */
    public float getColdWarm() {
        return coldWarm;
    }

    /**
     * @param coldWarm The cold_warm
     */
    public void setColdWarm(Integer coldWarm) {
        this.coldWarm = coldWarm;
    }

    /**
     * @return The weight
     */
    public float getWeight() {
        return weight;
    }

    /**
     * @param weight The weight
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * @return The numOfPieces
     */
    public Integer getNumOfPieces() {
        return numOfPieces;
    }

    /**
     * @param numOfPieces The num_of_pieces
     */
    public void setNumOfPieces(Integer numOfPieces) {
        this.numOfPieces = numOfPieces;
    }

    /**
     * @return The numOfPeople
     */
    public Integer getNumOfPeople() {
        return numOfPeople;
    }

    /**
     * @param numOfPeople The num_of_people
     */
    public void setNumOfPeople(Integer numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    /**
     * @return The kosher
     */
    public Boolean getKosher() {
        return kosher;
    }

    /**
     * @param kosher The kosher
     */
    public void setKosher(Boolean kosher) {
        this.kosher = kosher;
    }

    /**
     * @return The containesMilk
     */
    public Boolean getContainesMilk() {
        return containesMilk;
    }

    /**
     * @param containesMilk The containes_milk
     */
    public void setContainesMilk(Boolean containesMilk) {
        this.containesMilk = containesMilk;
    }

    /**
     * @return The vegeterian
     */
    public Boolean getVegeterian() {
        return vegeterian;
    }

    /**
     * @param vegeterian The vegeterian
     */
    public void setVegeterian(Boolean vegeterian) {
        this.vegeterian = vegeterian;
    }

    /**
     * @return The vegan
     */
    public Boolean getVegan() {
        return vegan;
    }

    /**
     * @param vegan The vegan
     */
    public void setVegan(Boolean vegan) {
        this.vegan = vegan;
    }

    /**
     * @return The glutenFree
     */
    public Boolean getGlutenFree() {
        return glutenFree;
    }

    /**
     * @param glutenFree The gluten_free
     */
    public void setGlutenFree(Boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    /**
     * @return The sugarFree
     */
    public Boolean getSugarFree() {
        return sugarFree;
    }

    /**
     * @param sugarFree The sugar_free
     */
    public void setSugarFree(Boolean sugarFree) {
        this.sugarFree = sugarFree;
    }

    /**
     * @return The mainCourse
     */
    public Boolean getMainCourse() {
        return mainCourse;
    }

    /**
     * @param mainCourse The main_course
     */
    public void setMainCourse(Boolean mainCourse) {
        this.mainCourse = mainCourse;
    }

    /**
     * @return The spicyLevel
     */
    public float getSpicyLevel() {
        return spicyLevel;
    }

    /**
     * @param spicyLevel The spicy_level
     */
    public void setSpicyLevel(Integer spicyLevel) {
        this.spicyLevel = spicyLevel;
    }

    /**
     * @return The glat
     */
    public Boolean getGlat() {
        return glat;
    }

    /**
     * @param glat The glat
     */
    public void setGlat(Boolean glat) {
        this.glat = glat;
    }

}
