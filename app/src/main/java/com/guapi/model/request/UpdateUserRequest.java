package com.guapi.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * author: long
 * date: ON 2017/7/11.
 */

public class UpdateUserRequest {
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("sex")
    private int sex;
    @SerializedName("province")
    private String province;
    @SerializedName("city")
    private String city;
    @SerializedName("note")
    private String note;
    @SerializedName("lableInfor")
    private String lableInfor;
    @SerializedName("age")
    private int age;
    @SerializedName("birthday")
    private String birthday;

    public String getLableInfor() {
        return lableInfor;
    }

    public void setLableInfor(String lableInfor) {
        this.lableInfor = lableInfor;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
