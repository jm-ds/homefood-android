package com.yuvalsuede.homefood.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DishModel implements Parcelable {

    @SerializedName("dish_id")
    @Expose
    private Integer dishId;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("cuisine_id")
    @Expose
    private Integer cuisineId;

    @SerializedName("creation_time")
    @Expose
    private String creationTime;

    @SerializedName("user_id")
    @Expose
    private Integer userId;

    @SerializedName("lng")
    @Expose
    private Double lng;

    @SerializedName("lat")
    @Expose
    private Double lat;

    @SerializedName("last_promote_time")
    @Expose
    private String lastPromoteTime;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("country_name")
    @Expose
    private String countryName;

    @SerializedName("base_currency")
    @Expose
    private String baseCurrency;

    @SerializedName("exchange_rate")
    @Expose
    private String exchangeRate;

    @SerializedName("country_iso_code")
    @Expose
    private String countryIsoCode;

    @SerializedName("main_photo")
    @Expose
    private String mainPhoto;

    @SerializedName("profile_img_url")
    @Expose
    private String profileImgUrl;

    @SerializedName("likes")
    @Expose
    private Integer likes;

    public DishModel() {

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

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
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

    /**
     * @return The creationTime
     */
    public String getCreationTime() {
        return creationTime;
    }

    /**
     * @param creationTime The creation_time
     */
    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * @return The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return The lng
     */
    public Double getLng() {
        return lng;
    }

    /**
     * @param lng The lng
     */
    public void setLng(Double lng) {
        this.lng = lng;
    }

    /**
     * @return The lat
     */
    public Double getLat() {
        return lat;
    }

    /**
     * @param lat The lat
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * @return The lastPromoteTime
     */
    public String getLastPromoteTime() {
        return lastPromoteTime;
    }

    /**
     * @param lastPromoteTime The last_promote_time
     */
    public void setLastPromoteTime(String lastPromoteTime) {
        this.lastPromoteTime = lastPromoteTime;
    }

    /**
     * @return The price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * @return The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency The currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return The countryName
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * @param countryName The country_name
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * @return The baseCurrency
     */
    public String getBaseCurrency() {
        return baseCurrency;
    }

    /**
     * @param baseCurrency The base_currency
     */
    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    /**
     * @return The exchangeRate
     */
    public String getExchangeRate() {
        return exchangeRate;
    }

    /**
     * @param exchangeRate The exchange_rate
     */
    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    /**
     * @return The countryIsoCode
     */
    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    /**
     * @param countryIsoCode The country_iso_code
     */
    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    /**
     * @return The mainPhoto
     */
    public String getMainPhoto() {
        return mainPhoto;
    }

    /**
     * @param mainPhoto The main_photo
     */
    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    /**
     * @return The profileImgUrl
     */
    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    /**
     * @param profileImgUrl The profile_img_url
     */
    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    /**
     * @return The likes
     */
    public Integer getLikes() {
        return likes;
    }

    /**
     * @param likes The likes
     */
    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(dishId);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeInt(cuisineId);
        parcel.writeString(creationTime);
        parcel.writeInt(userId);
        parcel.writeDouble(lng);
        parcel.writeDouble(lat);
        parcel.writeString(lastPromoteTime);
        parcel.writeString(price);
        parcel.writeString(currency);
        parcel.writeString(countryName);
        parcel.writeString(baseCurrency);
        parcel.writeString(exchangeRate);
        parcel.writeString(countryIsoCode);
        parcel.writeString(mainPhoto);
        parcel.writeString(profileImgUrl);
        if (likes != null) parcel.writeInt(likes);
    }

    public static final Parcelable.Creator<DishModel> CREATOR = new Parcelable.Creator<DishModel>() {
        // распаковываем объект из Parcel
        public DishModel createFromParcel(Parcel in) {
            return new DishModel(in);
        }

        public DishModel[] newArray(int size) {
            return new DishModel[size];
        }
    };

    // конструктор, считывающий данные из Parcel
    private DishModel(Parcel parcel) {
        dishId = parcel.readInt();
        title = parcel.readString();
        description = parcel.readString();
        cuisineId = parcel.readInt();
        creationTime = parcel.readString();
        userId = parcel.readInt();
        lng = parcel.readDouble();
        lat = parcel.readDouble();
        lastPromoteTime = parcel.readString();
        price = parcel.readString();
        currency = parcel.readString();
        countryName = parcel.readString();
        baseCurrency = parcel.readString();
        exchangeRate = parcel.readString();
        countryIsoCode = parcel.readString();
        mainPhoto = parcel.readString();
        profileImgUrl = parcel.readString();
        likes = parcel.readInt();
    }
}
