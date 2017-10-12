package com.guapi.model.response;

import com.ewuapp.framework.common.http.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by long on 2017/10/9.
 */

public class QueryFocusGpResponse extends Result {
    @SerializedName("total")
    private int total;
    @SerializedName("gp_list")
    private List<GpListBean> gpListBeans;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<GpListBean> getGpListBeans() {
        return gpListBeans;
    }

    public void setGpListBeans(List<GpListBean> gpListBeans) {
        this.gpListBeans = gpListBeans;
    }
    public static class GpListBean {

        /**
         * note : 之子于归
         * total_value :
         * pic_file3_url : null
         * user_name : 之子于归
         * line : 河北省唐山市丰南区学院南路7号靠近四王庄村
         * key_file_url : http://120.26.94.214:8080//2017-09-23/U_GP_INFOR-8ab394915ea77ae7015eae6fd831000bkeyfile.png
         * pic_file8_url : null
         * type : 2
         * grant_total : 0
         * view_total : 4
         * get_type :
         * video_url : null
         * pic_file6_url : null
         * comment_list : [{"comment_time":"2017-09-23 19:18:22.0","comment_user_name":"之子于归","comment_user_imag_url":"http://120.26.94.214:8080//2017-09-16/U_USER-8ab394915cca7945015cda749c4a000davatarUrl.png","to_comment_id":null,"sex":"1","comment_user_id":"U_USER-8ab394915cca7945015cda749c4a000d","comment_info":"kkkq","comment_id":"U_GP_INFOR-8ab394915ea77ae7015eae6fd831000b","age":"23"}]
         * pic_file2_url : null
         * pic_file7_url : null
         * lat : 39.571187
         * red_list : []
         * lng : 118.131963
         * create_time : 2017-09-23 19:11:59.0
         * total_count :
         * sex : 1
         * pic_file5_url : null
         * user_imag_url : http://120.26.94.214:8080//2017-09-16/U_USER-8ab394915cca7945015cda749c4a000davatarUrl.png
         * pic_file1_url : http://120.26.94.214:8080//2017-09-23/U_GP_INFOR-8ab394915ea77ae7015eae6fd831000bpic_file1.png
         * user_hid : 37229cff51ae7b83fdfc51dbebf4bede
         * gp_id : U_GP_INFOR-8ab394915ea77ae7015eae6fd831000b
         * pic_file9_url : null
         * user_id : U_USER-8ab394915cca7945015cda749c4a000d
         * pic_file4_url : null
         * comment_total : 1
         * age : 23
         * video_pic : null
         * desc :
         */
        @SerializedName("note")
        private String note;
        @SerializedName("total_value")
        private String total_value;
        @SerializedName("pic_file3_url")
        private String pic_file3_url;
        @SerializedName("user_name")
        private String user_name;
        @SerializedName("line")
        private String line;
        @SerializedName("key_file_url")
        private String key_file_url;
        @SerializedName("pic_file8_url")
        private String pic_file8_url;
        @SerializedName("type")
        private String type;
        @SerializedName("grant_total")
        private int grant_total;
        @SerializedName("view_total")
        private int view_total;
        @SerializedName("get_type")
        private String get_type;
        @SerializedName("video_url")
        private String video_url;
        @SerializedName("pic_file6_url")
        private String pic_file6_url;
        @SerializedName("pic_file2_url")
        private String pic_file2_url;
        @SerializedName("pic_file7_url")
        private String pic_file7_url;
        @SerializedName("lat")
        private String lat;
        @SerializedName("lng")
        private String lng;
        @SerializedName("create_time")
        private String create_time;
        @SerializedName("total_count")
        private String total_count;
        @SerializedName("sex")
        private String sex;
        @SerializedName("pic_file5_url")
        private String pic_file5_url;
        @SerializedName("user_imag_url")
        private String user_imag_url;
        @SerializedName("pic_file1_url")
        private String pic_file1_url;
        @SerializedName("user_hid")
        private String user_hid;
        @SerializedName("gp_id")
        private String gp_id;
        @SerializedName("pic_file9_url")
        private String pic_file9_url;
        @SerializedName("user_id")
        private String user_id;
        @SerializedName("pic_file4_url")
        private String pic_file4_url;
        @SerializedName("comment_total")
        private int comment_total;
        @SerializedName("age")
        private String age;
        @SerializedName("video_pic")
        private String video_pic;
        @SerializedName("desc")
        private String desc;
        @SerializedName("comment_list")
        private List<CommentListBean> comment_list;

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getTotal_value() {
            return total_value;
        }

        public void setTotal_value(String total_value) {
            this.total_value = total_value;
        }

        public String getPic_file3_url() {
            return pic_file3_url;
        }

        public void setPic_file3_url(String pic_file3_url) {
            this.pic_file3_url = pic_file3_url;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getLine() {
            return line;
        }

        public void setLine(String line) {
            this.line = line;
        }

        public String getKey_file_url() {
            return key_file_url;
        }

        public void setKey_file_url(String key_file_url) {
            this.key_file_url = key_file_url;
        }

        public String getPic_file8_url() {
            return pic_file8_url;
        }

        public void setPic_file8_url(String pic_file8_url) {
            this.pic_file8_url = pic_file8_url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getGrant_total() {
            return grant_total;
        }

        public void setGrant_total(int grant_total) {
            this.grant_total = grant_total;
        }

        public int getView_total() {
            return view_total;
        }

        public void setView_total(int view_total) {
            this.view_total = view_total;
        }

        public String getGet_type() {
            return get_type;
        }

        public void setGet_type(String get_type) {
            this.get_type = get_type;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public String getPic_file6_url() {
            return pic_file6_url;
        }

        public void setPic_file6_url(String pic_file6_url) {
            this.pic_file6_url = pic_file6_url;
        }

        public String getPic_file2_url() {
            return pic_file2_url;
        }

        public void setPic_file2_url(String pic_file2_url) {
            this.pic_file2_url = pic_file2_url;
        }

        public String getPic_file7_url() {
            return pic_file7_url;
        }

        public void setPic_file7_url(String pic_file7_url) {
            this.pic_file7_url = pic_file7_url;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getTotal_count() {
            return total_count;
        }

        public void setTotal_count(String total_count) {
            this.total_count = total_count;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getPic_file5_url() {
            return pic_file5_url;
        }

        public void setPic_file5_url(String pic_file5_url) {
            this.pic_file5_url = pic_file5_url;
        }

        public String getUser_imag_url() {
            return user_imag_url;
        }

        public void setUser_imag_url(String user_imag_url) {
            this.user_imag_url = user_imag_url;
        }

        public String getPic_file1_url() {
            return pic_file1_url;
        }

        public void setPic_file1_url(String pic_file1_url) {
            this.pic_file1_url = pic_file1_url;
        }

        public String getUser_hid() {
            return user_hid;
        }

        public void setUser_hid(String user_hid) {
            this.user_hid = user_hid;
        }

        public String getGp_id() {
            return gp_id;
        }

        public void setGp_id(String gp_id) {
            this.gp_id = gp_id;
        }

        public String getPic_file9_url() {
            return pic_file9_url;
        }

        public void setPic_file9_url(String pic_file9_url) {
            this.pic_file9_url = pic_file9_url;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getPic_file4_url() {
            return pic_file4_url;
        }

        public void setPic_file4_url(String pic_file4_url) {
            this.pic_file4_url = pic_file4_url;
        }

        public int getComment_total() {
            return comment_total;
        }

        public void setComment_total(int comment_total) {
            this.comment_total = comment_total;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getVideo_pic() {
            return video_pic;
        }

        public void setVideo_pic(String video_pic) {
            this.video_pic = video_pic;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<CommentListBean> getComment_list() {
            return comment_list;
        }

        public void setComment_list(List<CommentListBean> comment_list) {
            this.comment_list = comment_list;
        }

        public static class CommentListBean {
            /**
             * comment_time : 2017-09-23 19:18:22.0
             * comment_user_name : 之子于归
             * comment_user_imag_url : http://120.26.94.214:8080//2017-09-16/U_USER-8ab394915cca7945015cda749c4a000davatarUrl.png
             * to_comment_id : null
             * sex : 1
             * comment_user_id : U_USER-8ab394915cca7945015cda749c4a000d
             * comment_info : kkkq
             * comment_id : U_GP_INFOR-8ab394915ea77ae7015eae6fd831000b
             * age : 23
             */
            @SerializedName("comment_time")
            private String comment_time;
            @SerializedName("comment_user_name")
            private String comment_user_name;
            @SerializedName("comment_user_imag_url")
            private String comment_user_imag_url;
            @SerializedName("to_comment_id")
            private Object to_comment_id;
            @SerializedName("sex")
            private String sex;
            @SerializedName("comment_user_id")
            private String comment_user_id;
            @SerializedName("comment_info")
            private String comment_info;
            @SerializedName("comment_id")
            private String comment_id;
            @SerializedName("age")
            private String age;

            public String getComment_time() {
                return comment_time;
            }

            public void setComment_time(String comment_time) {
                this.comment_time = comment_time;
            }

            public String getComment_user_name() {
                return comment_user_name;
            }

            public void setComment_user_name(String comment_user_name) {
                this.comment_user_name = comment_user_name;
            }

            public String getComment_user_imag_url() {
                return comment_user_imag_url;
            }

            public void setComment_user_imag_url(String comment_user_imag_url) {
                this.comment_user_imag_url = comment_user_imag_url;
            }

            public Object getTo_comment_id() {
                return to_comment_id;
            }

            public void setTo_comment_id(Object to_comment_id) {
                this.to_comment_id = to_comment_id;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getComment_user_id() {
                return comment_user_id;
            }

            public void setComment_user_id(String comment_user_id) {
                this.comment_user_id = comment_user_id;
            }

            public String getComment_info() {
                return comment_info;
            }

            public void setComment_info(String comment_info) {
                this.comment_info = comment_info;
            }

            public String getComment_id() {
                return comment_id;
            }

            public void setComment_id(String comment_id) {
                this.comment_id = comment_id;
            }

            public String getAge() {
                return age;
            }

            public void setAge(String age) {
                this.age = age;
            }
        }
    }

}
