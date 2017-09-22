package com.guapi.model.response;

import com.ewuapp.framework.common.http.Result;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author: long
 * date: ON 2017/7/10.
 */

public class UserGetCountResponse extends Result implements Serializable{
    @SerializedName("data")//互相关注的数量
    private DataBean dataBean;

    public DataBean getDataBean() {
        return dataBean;
    }

    public void setDataBean(DataBean dataBean) {
        this.dataBean = dataBean;
    }

    public static class DataBean implements Serializable{
        @SerializedName("friendsCount")//互相关注的数量
        private long friendsCount;
        @SerializedName("mFocusCount")//我关注的数量
        private long mFocusCount;
        @SerializedName("fFocusCount")//关注我的数量
        private long fFocusCount;
        @SerializedName("myGpCount")//藏瓜皮数量
        private int myGpCount;
        @SerializedName("findGpCount")//找到藏瓜皮数量
        private int findGpCount;
        @SerializedName("lableInfor")//标签信息，多个的话逗号分割
        private String lableInfor;
        @SerializedName("lingInfor")//地点足迹信息，多个的话逗号分割
        private String lingInfor;
        @SerializedName("avatarUrl")
        private String avatarUrl;
        @SerializedName("pic_file1_url")
        private String pic_file1_url;
        @SerializedName("pic_file2_url")
        private String pic_file2_url;
        @SerializedName("pic_file3_url")
        private String pic_file3_url;
        @SerializedName("pic_file4_url")
        private String pic_file4_url;
        @SerializedName("pic_file5_url")
        private String pic_file5_url;
        @SerializedName("pic_file6_url")
        private String pic_file6_url;
        @SerializedName("birthday")
        private String birthday;
        @SerializedName("note")
        private String note;
        @SerializedName("sex")
        private String sex;
        @SerializedName("isFocus")
        private String isFocus;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("age")
        private String age;

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getIsFocus() {
            return isFocus;
        }

        public void setIsFocus(String isFocus) {
            this.isFocus = isFocus;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getPic_file1_url() {
            return pic_file1_url;
        }

        public void setPic_file1_url(String pic_file1_url) {
            this.pic_file1_url = pic_file1_url;
        }

        public String getPic_file2_url() {
            return pic_file2_url;
        }

        public void setPic_file2_url(String pic_file2_url) {
            this.pic_file2_url = pic_file2_url;
        }

        public String getPic_file3_url() {
            return pic_file3_url;
        }

        public void setPic_file3_url(String pic_file3_url) {
            this.pic_file3_url = pic_file3_url;
        }

        public String getPic_file4_url() {
            return pic_file4_url;
        }

        public void setPic_file4_url(String pic_file4_url) {
            this.pic_file4_url = pic_file4_url;
        }

        public String getPic_file5_url() {
            return pic_file5_url;
        }

        public void setPic_file5_url(String pic_file5_url) {
            this.pic_file5_url = pic_file5_url;
        }

        public String getPic_file6_url() {
            return pic_file6_url;
        }

        public void setPic_file6_url(String pic_file6_url) {
            this.pic_file6_url = pic_file6_url;
        }

        public long getFriendsCount() {
            return friendsCount;
        }

        public void setFriendsCount(long friendsCount) {
            this.friendsCount = friendsCount;
        }

        public long getmFocusCount() {
            return mFocusCount;
        }

        public void setmFocusCount(long mFocusCount) {
            this.mFocusCount = mFocusCount;
        }

        public long getfFocusCount() {
            return fFocusCount;
        }

        public void setfFocusCount(long fFocusCount) {
            this.fFocusCount = fFocusCount;
        }

        public int getMyGpCount() {
            return myGpCount;
        }

        public void setMyGpCount(int myGpCount) {
            this.myGpCount = myGpCount;
        }

        public int getFindGpCount() {
            return findGpCount;
        }

        public void setFindGpCount(int findGpCount) {
            this.findGpCount = findGpCount;
        }

        public String getLableInfor() {
            return lableInfor;
        }

        public void setLableInfor(String lableInfor) {
            this.lableInfor = lableInfor;
        }

        public String getLingInfor() {
            return lingInfor;
        }

        public void setLingInfor(String lingInfor) {
            this.lingInfor = lingInfor;
        }
    }
}
