package com.guapi.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * author: long
 * date: ON 2017/7/12.
 */

public class FoloowRequest {
    @SerializedName("destUids")
    public String destUids;

    public String getDestUids() {
        return destUids;
    }

    public void setDestUids(String destUids) {
        this.destUids = destUids;
    }
}
