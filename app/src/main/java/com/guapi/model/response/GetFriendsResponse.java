package com.guapi.model.response;

import com.ewuapp.framework.common.http.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// FIXME generate failure  field _$Frend_list120

/**
 * author: long
 * date: ON 2017/7/7.
 */

public class GetFriendsResponse extends Result {

    @SerializedName("frend_list")
    private List<FriendListBean> friendListBeans;

    public List<FriendListBean> getFriendListBeans() {
        return friendListBeans;
    }

    public void setFriendListBeans(List<FriendListBean> friendListBeans) {
        this.friendListBeans = friendListBeans;
    }

    public static class FriendListBean {
        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        private int type;//自己添加的状态，1好友2通讯录联系人
        /**
         * destUid : 3
         * destHid : 124543
         * destName  : xx
         * imageUrl  :
         */
        @SerializedName("destUid")
        private String destUid;
        @SerializedName("destHid")
        private String destHid;
        @SerializedName("destName")
        private String destName;
        @SerializedName("imageUrl")
        private String imageUrl;
        @SerializedName("last_time")
        private String lastTime;
        @SerializedName("note")
        private String note;
        @SerializedName("sex")
        private int sex;
        @SerializedName("phone")
        private String phone;
        @SerializedName("age")
        private String age;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getLastTime() {
            return lastTime;
        }

        public void setLastTime(String lastTime) {
            this.lastTime = lastTime;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getDestUid() {
            return destUid;
        }

        public void setDestUid(String destUid) {
            this.destUid = destUid;
        }

        public String getDestHid() {
            return destHid;
        }

        public void setDestHid(String destHid) {
            this.destHid = destHid;
        }

        public String getDestName() {
            return destName;
        }

        public void setDestName(String destName) {
            this.destName = destName;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
