package com.yuvalsuede.homefood.rest;

import com.google.gson.JsonObject;
import com.yuvalsuede.homefood.model.EditedCategory;
import com.yuvalsuede.homefood.model.Favorite;
import com.yuvalsuede.homefood.model.FavoriteDishes;
import com.yuvalsuede.homefood.model.MakeDish;
import com.yuvalsuede.homefood.model.Profile;
import com.yuvalsuede.homefood.model.SearchModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface HomeFoodService {

    @FormUrlEncoded
    @POST("categories")
    Call<JsonObject> getCategories(@Field("language") String lang);

    @FormUrlEncoded
    @POST("cuisines")
    Call<JsonObject> getCuisines(@Field("language") String lang);

    @POST("currencies")
    Call<JsonObject> getCurrencies();

    @POST("terms")
    Call<String> getTermsOfUse();

    @FormUrlEncoded
    @POST("dishinfo")
    Call<JsonObject> getDishInfo(@Field("dish_id") String dish_id);

    @FormUrlEncoded
    @POST("profile")
    Call<JsonObject> getUserProfileById(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("profile")
    Call<Profile> getUserProfileByIdUser(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("dishfilter")
    Call<JsonObject> getDishesByFilter(@FieldMap HashMap<String, String> map);

    @POST("dishfilter")
    Call<JsonObject> getDishesByFilter(@Body SearchModel model);

    @FormUrlEncoded
    @POST("https://www.homefood.mobi/api/v1/app/userbysystem")
    Call<JsonObject> getUserBySystem(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("https://www.homefood.mobi/api/v1/app/user")
    Call<JsonObject> createUserFromFB(@FieldMap HashMap<String, String> hashMap);


    /**
     * @param hashMap contains dish_id, app_secret and token
     */
    @FormUrlEncoded
    @POST("api/v1/userlikes")
    Call<Favorite> getUserLikes(@FieldMap HashMap<String, String> hashMap);

    /**
     * @param hashMap contains access_token, from_user_id, dish_id
     */

    @FormUrlEncoded
    @POST("api/v1/setlike")
    Call<JsonObject> setLike(@FieldMap HashMap<String, String> hashMap);

    /**
     * @param hashMap contains access_token, from_user_id, dish_id
     */

    @FormUrlEncoded
    @POST("api/v1/unlike")
    Call<JsonObject> setUnlike(@FieldMap HashMap<String, String> hashMap);

    /**
     * @param user_id id of user in homefood
     * @return dishes info in json object
     */

    @FormUrlEncoded
    @POST("userdishes")
    Call<JsonObject> getUserDishesWithUserId(@Field("user_id") String user_id);

    /**
     * @param hashMap
     * @return
     */

    @FormUrlEncoded
    @POST("api/v1/dish")
    Call<JsonObject> createDishForUser(@FieldMap HashMap<String, Object> hashMap);

    @POST("api/v1/dish")
    Call<JsonObject> createDishForUser(@Body MakeDish dish);


    /**
     * @param hashMap contains access_token, user_id, phone_number
     * @return
     */

    @FormUrlEncoded
    @POST("api/v1/savephone")
    Call<JsonObject> savePhone(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("api/v1/userconfig")
    Call<JsonObject> getUserConfig(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("api/v1/setconfig")
    Call<JsonObject> setUserConfig(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("api/v1/deletedish")
    Call<JsonObject> deleteDish(@FieldMap HashMap<String, String> hashMap);

    /**
     * @param hashMap "access_token" : [self loadToken],
     *                "user_id" : [MainDb sharedDb].data.userId.stringValue,
     *                "dish_id" : dishId.stringValue,
     *                "price" : price.stringValue,
     *                "title" : title,
     *                "description" : description,
     *                "currency" : currency,
     *                "phone_number" : phoneNumber,
     *                "cuisine_id" : cusineId.stringValue
     */
    @FormUrlEncoded
    @POST("api/v1/updatedish")
    Call<JsonObject> updateDish(@FieldMap HashMap<String, String> hashMap);

    @POST("api/v1/updatedishcategories")
    Call<JsonObject> updateDishCategories(@Body EditedCategory params);

    @FormUrlEncoded
    @POST("api/v1/savedishphoto")
    Call<JsonObject> updateDishPhoto(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("dishcategories")
    Call<JsonObject> getDishCategories(@Field("dish_id") String dish_id);

    @FormUrlEncoded
    @POST("api/v1/popdish")
    Call<JsonObject> popDish(@FieldMap HashMap<String, String> hashMap);

    @POST("api/v1/updateprofile")
    Call<JsonObject> updateProfile(@Body Profile profile);

    @FormUrlEncoded
    @POST("api/v1/likes")
    Call<FavoriteDishes> getLikedDishes(@FieldMap HashMap<String, String> hashMap);
}
