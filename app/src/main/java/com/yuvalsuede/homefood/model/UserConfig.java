package com.yuvalsuede.homefood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserConfig {

    @SerializedName("user_currency")
    @Expose
    private String userCurrency;
    @SerializedName("user_language")
    @Expose
    private String userLanguage;
    @SerializedName("user_weight_unit")
    @Expose
    private Integer userWeightUnit;
    @SerializedName("user_distance_unit")
    @Expose
    private Integer userDistanceUnit;
    @SerializedName("allow_push_notification")
    @Expose
    private Boolean allowPushNotification;

    /**
     * @return The userCurrency
     */
    public String getUserCurrency() {
        return userCurrency;
    }

    /**
     * @param userCurrency The user_currency
     */
    public void setUserCurrency(String userCurrency) {
        this.userCurrency = userCurrency;
    }

    /**
     * @return The userLanguage
     */
    public String getUserLanguage() {
        return userLanguage;
    }

    /**
     * @param userLanguage The user_language
     */
    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    /**
     * @return The userWeightUnit
     */
    public Integer getUserWeightUnit() {
        return userWeightUnit;
    }

    /**
     * @param userWeightUnit The user_weight_unit
     */
    public void setUserWeightUnit(Integer userWeightUnit) {
        this.userWeightUnit = userWeightUnit;
    }

    /**
     * @return The userDistanceUnit
     */
    public Integer getUserDistanceUnit() {
        return userDistanceUnit;
    }

    /**
     * @param userDistanceUnit The user_distance_unit
     */
    public void setUserDistanceUnit(Integer userDistanceUnit) {
        this.userDistanceUnit = userDistanceUnit;
    }

    /**
     * @return The allowPushNotification
     */
    public Boolean getAllowPushNotification() {
        return allowPushNotification;
    }

    /**
     * @param allowPushNotification The allow_push_notification
     */
    public void setAllowPushNotification(Boolean allowPushNotification) {
        this.allowPushNotification = allowPushNotification;
    }

}
