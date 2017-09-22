package com.guapi.model.response;

import com.ewuapp.framework.common.http.Result;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by long on 2017/7/18.
 */

public class RefreshOneMessageResponse extends Result implements Serializable{
    @SerializedName("ms_list")
    private List<MsListBean> msListBeen;

    public List<MsListBean> getMsListBeen() {
        return msListBeen;
    }

    public void setMsListBeen(List<MsListBean> msListBeen) {
        this.msListBeen = msListBeen;
    }

    public static class MsListBean implements Serializable{

        /**
         * ms_time : 2017-07-18 17:14:59.0
         * sms_user_imag_url : http://120.26.94.214:8080//2017-07-17/U_USER-8ab394915d322f8b015d4be23096003favatarUrl.png
         * ms_type : 4
         * ms_content : xxxxxxx111111
         * ms_user_id : U_USER-8ab394915d322f8b015d4be23096003f
         * comment_id : null
         * ms_user_name : 自习误会
         * ms_user_hid : 30fd80d49ab582deb22803eead017556
         * gp_id : U_GP_INFOR-8ab394915d322f8b015d4f5ebace0043
         */
        @SerializedName("ms_time")
        private String ms_time;//消息时间
        @SerializedName("sms_user_imag_url")
        private String sms_user_imag_url;//消息用户图标
        @SerializedName("ms_type")
        private String ms_type;//1：系统消息，2：瓜皮动态，3：瓜皮评论，4：瓜皮点赞，5：红包，6关注消息
        @SerializedName("ms_content")
        private String ms_content;//消息内容
        @SerializedName("ms_user_id")
        private String ms_user_id;//消息用户ID
        @SerializedName("comment_id")
        private Object comment_id;//评论ID
        @SerializedName("ms_user_name")
        private String ms_user_name;//消息用户昵称
        @SerializedName("ms_user_hid")
        private String ms_user_hid;//消息用户环信id
        @SerializedName("gp_id")
        private String gp_id;//瓜皮ID

        public String getMs_time() {
            return ms_time;
        }

        public void setMs_time(String ms_time) {
            this.ms_time = ms_time;
        }

        public String getSms_user_imag_url() {
            return sms_user_imag_url;
        }

        public void setSms_user_imag_url(String sms_user_imag_url) {
            this.sms_user_imag_url = sms_user_imag_url;
        }

        public String getMs_type() {
            return ms_type;
        }

        public void setMs_type(String ms_type) {
            this.ms_type = ms_type;
        }

        public String getMs_content() {
            return ms_content;
        }

        public void setMs_content(String ms_content) {
            this.ms_content = ms_content;
        }

        public String getMs_user_id() {
            return ms_user_id;
        }

        public void setMs_user_id(String ms_user_id) {
            this.ms_user_id = ms_user_id;
        }

        public Object getComment_id() {
            return comment_id;
        }

        public void setComment_id(Object comment_id) {
            this.comment_id = comment_id;
        }

        public String getMs_user_name() {
            return ms_user_name;
        }

        public void setMs_user_name(String ms_user_name) {
            this.ms_user_name = ms_user_name;
        }

        public String getMs_user_hid() {
            return ms_user_hid;
        }

        public void setMs_user_hid(String ms_user_hid) {
            this.ms_user_hid = ms_user_hid;
        }

        public String getGp_id() {
            return gp_id;
        }

        public void setGp_id(String gp_id) {
            this.gp_id = gp_id;
        }
    }

}
