package com.guapi.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by long on 2017/7/18.
 */

public class RefreshOneMessageRequest {
    @SerializedName("ms_type")
    private String ms_type;

    public String getMs_type() {
        return ms_type;
    }

    public void setMs_type(String ms_type) {
        this.ms_type = ms_type;
    }
}
