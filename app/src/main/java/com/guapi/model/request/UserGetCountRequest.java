package com.guapi.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * author: long
 * date: ON 2017/7/10.
 */

public class UserGetCountRequest {
    @SerializedName("user_id")
    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
