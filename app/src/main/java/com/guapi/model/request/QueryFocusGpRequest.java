package com.guapi.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by long on 2017/10/9.
 */

public class QueryFocusGpRequest {
    @SerializedName("type")
    public String type;
    @SerializedName("user_id")
    public String user_id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
