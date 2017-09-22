package com.guapi.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * author: long
 * date: ON 2017/7/11.
 */

public class QueryUserInfoBuHxRequest {
    @SerializedName("hx_id")
    public String hx_id;

    public String getHx_id() {
        return hx_id;
    }

    public void setHx_id(String hx_id) {
        this.hx_id = hx_id;
    }
}
