package com.yuvalsuede.homefood.model.dishinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DishInfo {

    @SerializedName("dish_params")
    @Expose
    private DishParams dishParams;
    @SerializedName("dish_info")
    @Expose
    private DishInfo_ dishInfo;
    @SerializedName("dish_photos")
    @Expose
    private List<DishPhoto> dishPhotos = new ArrayList<DishPhoto>();
    @SerializedName("dish_likes")
    @Expose
    private List<DishLike> dishLikes = new ArrayList<DishLike>();

    /**
     * @return The dishParams
     */
    public DishParams getDishParams() {
        return dishParams;
    }

    /**
     * @param dishParams The dish_params
     */
    public void setDishParams(DishParams dishParams) {
        this.dishParams = dishParams;
    }

    /**
     * @return The dishInfo
     */
    public DishInfo_ getDishInfo() {
        return dishInfo;
    }

    /**
     * @param dishInfo The dish_info
     */
    public void setDishInfo(DishInfo_ dishInfo) {
        this.dishInfo = dishInfo;
    }

    /**
     * @return The dishPhotos
     */
    public List<DishPhoto> getDishPhotos() {
        return dishPhotos;
    }

    /**
     * @param dishPhotos The dish_photos
     */
    public void setDishPhotos(List<DishPhoto> dishPhotos) {
        this.dishPhotos = dishPhotos;
    }

    /**
     * @return The dishLikes
     */
    public List<DishLike> getDishLikes() {
        return dishLikes;
    }

    /**
     * @param dishLikes The dish_likes
     */
    public void setDishLikes(List<DishLike> dishLikes) {
        this.dishLikes = dishLikes;
    }

}
