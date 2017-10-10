package com.guapi.model.response;

import com.ewuapp.framework.common.http.Result;
import com.google.gson.annotations.SerializedName;

/**
 * author: long
 * date: ON 2017/6/23.
 */
public class LoginResponse extends Result {


    /**
     * expire : 1498887096098
     * sessionId : [B@6d3522
     * user : {"uid":"U_USER-8b8280b05cca2f1f015cca364a420004","hid":"环信接口未开通","lastAccessTime":"2017-06-24 13:31:36","phone":"18933976778","avatarUrl":"https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2238123315,1552697185&fm=26&gp=0.jpg","createTime":"2017-06-21 18:32:55","sex":"","nickname":"","achi":{"grade":1,"point":0}}
     * status : 2000000
     */

    @SerializedName("expire")
    private long expire;
    @SerializedName("sessionId")
    private String sessionId;
    @SerializedName("user")
    private UserBean user;

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * uid : U_USER-8b8280b05cca2f1f015cca364a420004
         * hid : 环信接口未开通
         * lastAccessTime : 2017-06-24 13:31:36
         * phone : 18933976778
         * avatarUrl : https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2238123315,1552697185&fm=26&gp=0.jpg
         * createTime : 2017-06-21 18:32:55
         * sex :
         * nickname :
         * achi : {"grade":1,"point":0}
         */

        @SerializedName("uid")
        private String uid;
        @SerializedName("hid")
        private String hid;
        @SerializedName("hpass")
        private String hpass;
        @SerializedName("lastAccessTime")
        private String lastAccessTime;
        @SerializedName("phone")
        private String phone;
        @SerializedName("avatarUrl")
        private String avatarUrl;
        @SerializedName("createTime")
        private String createTime;
        @SerializedName("sex")
        private String sex;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("achi")
        private AchiBean achi;
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
        @SerializedName("age")
        private String age;
        @SerializedName("note")
        private String note;
        @SerializedName("line")
        private String line;

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getLine() {
            return line;
        }

        public void setLine(String line) {
            this.line = line;
        }

        public String getHpass() {
            return hpass;
        }

        public void setHpass(String hpass) {
            this.hpass = hpass;
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

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getHid() {
            return hid;
        }

        public void setHid(String hid) {
            this.hid = hid;
        }

        public String getLastAccessTime() {
            return lastAccessTime;
        }

        public void setLastAccessTime(String lastAccessTime) {
            this.lastAccessTime = lastAccessTime;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public AchiBean getAchi() {
            return achi;
        }

        public void setAchi(AchiBean achi) {
            this.achi = achi;
        }

        public static class AchiBean {
            /**
             * grade : 1
             * point : 0
             */

            @SerializedName("grade")
            private int grade;
            @SerializedName("point")
            private int point;

            public int getGrade() {
                return grade;
            }

            public void setGrade(int grade) {
                this.grade = grade;
            }

            public int getPoint() {
                return point;
            }

            public void setPoint(int point) {
                this.point = point;
            }
        }
    }
}
