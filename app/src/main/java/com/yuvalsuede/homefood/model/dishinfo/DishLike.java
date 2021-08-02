
package com.yuvalsuede.homefood.model.dishinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DishLike {

    @SerializedName("dish_id")
    @Expose
    private Integer dishId;
    @SerializedName("from_user_id")
    @Expose
    private Integer fromUserId;
    @SerializedName("date")
    @Expose
    private String date;

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
     * @return The fromUserId
     */
    public Integer getFromUserId() {
        return fromUserId;
    }

    /**
     * @param fromUserId The from_user_id
     */
    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    /**
     * @return The date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date The date
     */
    public void setDate(String date) {
        this.date = date;
    }

}
