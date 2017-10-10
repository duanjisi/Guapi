package com.guapi.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * author: long
 * date: ON 2017/7/7.
 */

public class GetFriendsRequest {
    @SerializedName("type")
    private int type;
    @SerializedName("user_id")
    private String user_id;

    public int getType() {
        return type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setType(int type) {
        this.type = type;
    }
}
