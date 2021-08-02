package com.yuvalsuede.homefood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("system_id")
    @Expose
    private String systemId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("about")
    @Expose
    private Object about;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("profile_img_url")
    @Expose
    private String profileImgUrl;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("phone_number")
    @Expose
    private Object phoneNumber;
    @SerializedName("system_name")
    @Expose
    private String systemName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("user_currency")
    @Expose
    private String userCurrency;
    @SerializedName("user_language")
    @Expose
    private String userLanguage;
    @SerializedName("user_weight_unit")
    @Expose
    private String userWeightUnit;
    @SerializedName("user_distance_unit")
    @Expose
    private String userDistanceUnit;
    @SerializedName("allow_push_notification")
    @Expose
    private Boolean allowPushNotification;
    @SerializedName("gender")
    @Expose
    private String gender;

    /**
     * @return The systemId
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * @param systemId The system_id
     */
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    /**
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return The firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName The first_name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return The lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName The last_name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return The about
     */
    public Object getAbout() {
        return about;
    }

    /**
     * @param about The about
     */
    public void setAbout(Object about) {
        this.about = about;
    }

    /**
     * @return The token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token The token
     */
    public void setToken(String token) {
        this.token = token;
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
     * @return The countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode The country_code
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return The phoneNumber
     */
    public Object getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber The phone_number
     */
    public void setPhoneNumber(Object phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return The systemName
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * @param systemName The system_name
     */
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

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
    public String getUserWeightUnit() {
        return userWeightUnit;
    }

    /**
     * @param userWeightUnit The user_weight_unit
     */
    public void setUserWeightUnit(String userWeightUnit) {
        this.userWeightUnit = userWeightUnit;
    }

    /**
     * @return The userDistanceUnit
     */
    public String getUserDistanceUnit() {
        return userDistanceUnit;
    }

    /**
     * @param userDistanceUnit The user_distance_unit
     */
    public void setUserDistanceUnit(String userDistanceUnit) {
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

    /**
     * @return The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

}
