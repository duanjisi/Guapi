package com.guapi.model.response;

import com.ewuapp.framework.common.http.Result;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author: long
 * date: ON 2017/7/11.
 */

public class QueryUserInfoByHxResponse extends Result{
    @SerializedName("data")//互相关注的数量
    private DataBean dataBean;

    public DataBean getDataBean() {
        return dataBean;
    }

    public void setDataBean(DataBean dataBean) {
        this.dataBean = dataBean;
    }
    public static class DataBean implements Serializable {
        /**
         * note : 之子于归
         * destName : 王龙
         * pic_file3_url : null
         * sex : 0
         * pic_file5_url : null
         * pic_file1_url : http://120.26.94.214:8080//2017-07-11/U_USER-8ab394915cca7945015cda749c4a000dpic_file1.png
         * pic_file6_url : null
         * phone : 18300072223
         * pic_file4_url : null
         * imageUrl : http://120.26.94.214:8080//2017-07-11/U_USER-8ab394915cca7945015cda749c4a000davatarUrl.png
         * pic_file2_url : null
         * pic_file7_url : null
         * age : 24
         */
        @SerializedName("note")
        private String note;
        @SerializedName("destName")
        private String destName;
        @SerializedName("pic_file3_url")
        private Object pic_file3_url;
        @SerializedName("sex")
        private String sex;
        @SerializedName("pic_file5_url")
        private Object pic_file5_url;
        @SerializedName("pic_file1_url")
        private String pic_file1_url;
        @SerializedName("pic_file6_url")
        private Object pic_file6_url;
        @SerializedName("phone")
        private String phone;
        @SerializedName("pic_file4_url")
        private Object pic_file4_url;
        @SerializedName("imageUrl")
        private String imageUrl;
        @SerializedName("pic_file2_url")
        private Object pic_file2_url;
        @SerializedName("pic_file7_url")
        private Object pic_file7_url;
        @SerializedName("age")
        private String age;

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getDestName() {
            return destName;
        }

        public void setDestName(String destName) {
            this.destName = destName;
        }

        public Object getPic_file3_url() {
            return pic_file3_url;
        }

        public void setPic_file3_url(Object pic_file3_url) {
            this.pic_file3_url = pic_file3_url;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Object getPic_file5_url() {
            return pic_file5_url;
        }

        public void setPic_file5_url(Object pic_file5_url) {
            this.pic_file5_url = pic_file5_url;
        }

        public String getPic_file1_url() {
            return pic_file1_url;
        }

        public void setPic_file1_url(String pic_file1_url) {
            this.pic_file1_url = pic_file1_url;
        }

        public Object getPic_file6_url() {
            return pic_file6_url;
        }

        public void setPic_file6_url(Object pic_file6_url) {
            this.pic_file6_url = pic_file6_url;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Object getPic_file4_url() {
            return pic_file4_url;
        }

        public void setPic_file4_url(Object pic_file4_url) {
            this.pic_file4_url = pic_file4_url;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Object getPic_file2_url() {
            return pic_file2_url;
        }

        public void setPic_file2_url(Object pic_file2_url) {
            this.pic_file2_url = pic_file2_url;
        }

        public Object getPic_file7_url() {
            return pic_file7_url;
        }

        public void setPic_file7_url(Object pic_file7_url) {
            this.pic_file7_url = pic_file7_url;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }

}
