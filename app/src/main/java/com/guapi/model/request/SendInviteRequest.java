package com.guapi.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by long on 2017/9/18.
 */

public class SendInviteRequest {
    @SerializedName("phone")
    public String phone;
    @SerializedName("name")
    public String name;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
