package com.guapi.model.response;

import com.ewuapp.framework.common.http.Result;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Johnny on 2017-09-11.
 */
public class GpSingleRespone extends Result {

    @SerializedName("gpInfor")
    private GPResponse.GpListBean bean;

    public GPResponse.GpListBean getBean() {
        return bean;
    }

    public void setBean(GPResponse.GpListBean bean) {
        this.bean = bean;
    }
}
