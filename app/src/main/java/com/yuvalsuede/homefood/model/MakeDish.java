package com.yuvalsuede.homefood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MakeDish {

    @SerializedName("country_name")
    @Expose
    private String countryName;
    private String kosher;

    @SerializedName("containes_milk")
    @Expose
    private String containesMilk;

    @SerializedName("country_iso_code")
    @Expose
    private String countryIsoCode;
    private String weight;

    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;

    @SerializedName("cuisine_id")
    @Expose
    private String cuisineId;

    @SerializedName("main_course")
    @Expose
    private String mainCourse;
    private String currency;
    private String delivery;

    @SerializedName("cold_warm")
    @Expose
    private String coldWarm;

    @SerializedName("num_of_pieces")
    @Expose
    private String numOfPieces;
    private String lng;
    private String vegan;
    private String price;
    private String title;
    private String description;

    @SerializedName("access_token")
    @Expose
    private String accessToken;

    @SerializedName("gluten_free")
    @Expose
    private String glutenFree;

    @SerializedName("spicy_level")
    @Expose
    private String spicyLevel;

    @SerializedName("num_of_people")
    @Expose
    private String numOfPeople;
    private String glat;
    private String lat;
    private String vegeterian;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("sugar_free")
    @Expose
    private String sugarFree;
    private List<MakePhoto> photos;
    private List<MakeCategory> categories;

    public MakeDish() {

    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getKosher() {
        return kosher;
    }

    public void setKosher(String kosher) {
        this.kosher = kosher;
    }

    public String getContainesMilk() {
        return containesMilk;
    }

    public void setContainesMilk(String containesMilk) {
        this.containesMilk = containesMilk;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCuisineId() {
        return cuisineId;
    }

    public void setCuisineId(String cuisineId) {
        this.cuisineId = cuisineId;
    }

    public String getMainCourse() {
        return mainCourse;
    }

    public void setMainCourse(String mainCourse) {
        this.mainCourse = mainCourse;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getColdWarm() {
        return coldWarm;
    }

    public void setColdWarm(String coldWarm) {
        this.coldWarm = coldWarm;
    }

    public String getNumOfPieces() {
        return numOfPieces;
    }

    public void setNumOfPieces(String numOfPieces) {
        this.numOfPieces = numOfPieces;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getVegan() {
        return vegan;
    }

    public void setVegan(String vegan) {
        this.vegan = vegan;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(String glutenFree) {
        this.glutenFree = glutenFree;
    }

    public String getSpicyLevel() {
        return spicyLevel;
    }

    public void setSpicyLevel(String spicyLevel) {
        this.spicyLevel = spicyLevel;
    }

    public String getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(String numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public String getGlat() {
        return glat;
    }

    public void setGlat(String glat) {
        this.glat = glat;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getVegeterian() {
        return vegeterian;
    }

    public void setVegeterian(String vegeterian) {
        this.vegeterian = vegeterian;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String user_id) {
        this.userId = user_id;
    }

    public String getSugarFree() {
        return sugarFree;
    }

    public void setSugarFree(String sugarFree) {
        this.sugarFree = sugarFree;
    }

    public List<MakePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<MakePhoto> photos) {
        this.photos = photos;
    }

    public List<MakeCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<MakeCategory> categories) {
        this.categories = categories;
    }
}
