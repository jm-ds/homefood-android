
package com.yuvalsuede.homefood.model.dishinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DishPhoto {

    @SerializedName("dish_id")
    @Expose
    private Integer dishId;
    @SerializedName("photo_id")
    @Expose
    private String photoId;

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
     * @return The photoId
     */
    public String getPhotoId() {
        return photoId;
    }

    /**
     * @param photoId The photo_id
     */
    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

}
