package com.guapi.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by long on 2017/7/18.
 */

public class BindPhoneRequest {
    @SerializedName("phone")
    public String phone;
    @SerializedName("code")
    public String code;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
