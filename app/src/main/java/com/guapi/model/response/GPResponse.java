package com.guapi.model.response;

import com.ewuapp.framework.common.http.Result;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * ╭╯☆★☆★╭╯
 * 　　╰╮★☆★╭╯
 * 　　　 ╯☆╭─╯
 * 　　 ╭ ╭╯
 * 　╔╝★╚╗  ★☆╮     BUG兄，没时间了快上车    ╭☆★
 * 　║★☆★║╔═══╗　╔═══╗　╔═══╗  ╔═══╗
 * 　║☆★☆║║★　☆║　║★　☆║　║★　☆║  ║★　☆║
 * ◢◎══◎╚╝◎═◎╝═╚◎═◎╝═╚◎═◎╝═╚◎═◎╝..........
 *
 * @author Jewel
 * @version 1.0
 * @since 2017/7/10 0010
 */

public class GPResponse extends Result {
    /**
     * total : 2
     * gp_list : [{"type":2,"gp_id":"adsfadsfafafds2","user_name":"小名","lat":"23.23","lng":"34.22","user_imag_url":"www.xxx.xxx.xx/pic.png","user_id":"2sdfsadf23343434","desc":"好美的风景啊1","pic_file1_url":"www.xxxxx.xxx/pic2.xxx","comment_list":[{"comment_user_id":"xxxxxxx","comment_id":"sdfsafd2323232","comment_user_name":"xxxxxxx","comment_time":"2017-02-02:21:23:11","comment_info":"一起玩啊2","comment_user_imag_url":"xxx.xxx.xxx/pic2.png"}]}]
     */
    @SerializedName("total")
    private int total;
    @SerializedName("gp_list")
    private List<GpListBean> gpList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<GpListBean> getGpList() {
        return gpList;
    }

    public void setGpList(List<GpListBean> gpList) {
        this.gpList = gpList;
    }

    public static class GpListBean implements Serializable {
        /**
         * type : 2
         * gp_id : adsfadsfafafds2
         * user_name : 小名
         * lat : 23.23
         * lng : 34.22
         * user_imag_url : www.xxx.xxx.xx/pic.png
         * user_id : 2sdfsadf23343434
         * desc : 好美的风景啊1
         * pic_file1_url : www.xxxxx.xxx/pic2.xxx
         * comment_list : [{"comment_user_id":"xxxxxxx","comment_id":"sdfsafd2323232","comment_user_name":"xxxxxxx","comment_time":"2017-02-02:21:23:11","comment_info":"一起玩啊2","comment_user_imag_url":"xxx.xxx.xxx/pic2.png"}]
         */
        @SerializedName("type")
        private String type;
        @SerializedName("gp_id")
        private String gpId;
        @SerializedName("user_name")
        private String userName;
        @SerializedName("lat")
        private double lat;
        @SerializedName("lng")
        private double lng;
        @SerializedName("is_find")
        private String is_find;
        @SerializedName("user_imag_url")
        private String userImagUrl;
        @SerializedName("user_id")
        private String userId;
        @SerializedName("user_hid")
        private String user_hid;
        @SerializedName("note")
        private String note;
        @SerializedName("sex")
        private int sex;
        @SerializedName("age")
        private String age;
        @SerializedName("desc")
        private String desc;
        @SerializedName("pic_file1_url")
        private String picFile1Url;
        @SerializedName("pic_file2_url")
        private String picFile2Url;
        @SerializedName("pic_file3_url")
        private String picFile3Url;
        @SerializedName("pic_file4_url")
        private String picFile4Url;
        @SerializedName("pic_file5_url")
        private String picFile5Url;
        @SerializedName("pic_file6_url")
        private String picFile6Url;
        @SerializedName("pic_file7_url")
        private String picFile7Url;
        @SerializedName("pic_file8_url")
        private String picFile8Url;
        @SerializedName("pic_file9_url")
        private String picFile9Url;
        @SerializedName("create_time")
        private String createTime;
        @SerializedName("comment_total")
        private String commentTotal;
        @SerializedName("grant_total")
        private String grantTotal;
        @SerializedName("view_total")
        private String view_total;
        @SerializedName("line")
        private String line;
        @SerializedName("hxid")
        private String hxid;
        @SerializedName("key_file_url")
        private String pointPicture;
        @SerializedName("video_url")
        private String video_url;
        @SerializedName("video_pic")
        private String video_pic;

        @SerializedName("comment_list")
        private List<CommentListBean> commentList;

        public String address;

        public float distance;

        public String getIs_find() {
            return is_find;
        }

        public void setIs_find(String is_find) {
            this.is_find = is_find;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getView_total() {
            return view_total;
        }

        public void setView_total(String view_total) {
            this.view_total = view_total;
        }

        public String getVideo_pic() {
            return video_pic;
        }

        public void setVideo_pic(String video_pic) {
            this.video_pic = video_pic;
        }

        public String getUser_hid() {
            return user_hid;
        }

        public void setUser_hid(String user_hid) {
            this.user_hid = user_hid;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public String getPointPicture() {
            return pointPicture;
        }

        public void setPointPicture(String pointPicture) {
            this.pointPicture = pointPicture;
        }

        public String getLine() {
            return line;
        }

        public void setLine(String line) {
            this.line = line;
        }

        public String getHxid() {
            return hxid;
        }

        public void setHxid(String hxid) {
            this.hxid = hxid;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getGpId() {
            return gpId;
        }

        public void setGpId(String gpId) {
            this.gpId = gpId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public String getUserImagUrl() {
            return userImagUrl;
        }

        public void setUserImagUrl(String userImagUrl) {
            this.userImagUrl = userImagUrl;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPicFile1Url() {
            return picFile1Url;
        }

        public String getPicFile2Url() {
            return picFile2Url;
        }

        public void setPicFile2Url(String picFile2Url) {
            this.picFile2Url = picFile2Url;
        }

        public String getPicFile3Url() {
            return picFile3Url;
        }

        public void setPicFile3Url(String picFile3Url) {
            this.picFile3Url = picFile3Url;
        }

        public String getPicFile4Url() {
            return picFile4Url;
        }

        public void setPicFile4Url(String picFile4Url) {
            this.picFile4Url = picFile4Url;
        }

        public String getPicFile5Url() {
            return picFile5Url;
        }

        public void setPicFile5Url(String picFile5Url) {
            this.picFile5Url = picFile5Url;
        }

        public String getPicFile6Url() {
            return picFile6Url;
        }

        public void setPicFile6Url(String picFile6Url) {
            this.picFile6Url = picFile6Url;
        }

        public String getPicFile7Url() {
            return picFile7Url;
        }

        public void setPicFile7Url(String picFile7Url) {
            this.picFile7Url = picFile7Url;
        }

        public String getPicFile8Url() {
            return picFile8Url;
        }

        public void setPicFile8Url(String picFile8Url) {
            this.picFile8Url = picFile8Url;
        }

        public String getPicFile9Url() {
            return picFile9Url;
        }

        public void setPicFile9Url(String picFile9Url) {
            this.picFile9Url = picFile9Url;
        }

        public void setPicFile1Url(String picFile1Url) {
            this.picFile1Url = picFile1Url;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCommentTotal() {
            return commentTotal;
        }

        public void setCommentTotal(String commentTotal) {
            this.commentTotal = commentTotal;
        }

        public String getGrantTotal() {
            return grantTotal;
        }

        public void setGrantTotal(String grantTotal) {
            this.grantTotal = grantTotal;
        }

        public List<CommentListBean> getCommentList() {
            return commentList;
        }

        public void setCommentList(List<CommentListBean> commentList) {
            this.commentList = commentList;
        }

        public static class CommentListBean implements Serializable {
            /**
             * comment_user_id : xxxxxxx
             * comment_id : sdfsafd2323232
             * comment_user_name : xxxxxxx
             * comment_time : 2017-02-02:21:23:11
             * comment_info : 一起玩啊2
             * comment_user_imag_url : xxx.xxx.xxx/pic2.png
             */
            @SerializedName("age")
            private int age;
            @SerializedName("sex")
            private int sex;
            @SerializedName("comment_user_id")
            private String commentUserId;
            @SerializedName("comment_id")
            private String commentId;
            @SerializedName("comment_user_name")
            private String commentUserName;
            @SerializedName("comment_time")
            private String commentTime;
            @SerializedName("comment_info")
            private String commentInfo;
            @SerializedName("comment_user_imag_url")
            private String commentUserImagUrl;

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getCommentUserId() {
                return commentUserId;
            }

            public void setCommentUserId(String commentUserId) {
                this.commentUserId = commentUserId;
            }

            public String getCommentId() {
                return commentId;
            }

            public void setCommentId(String commentId) {
                this.commentId = commentId;
            }

            public String getCommentUserName() {
                return commentUserName;
            }

            public void setCommentUserName(String commentUserName) {
                this.commentUserName = commentUserName;
            }

            public String getCommentTime() {
                return commentTime;
            }

            public void setCommentTime(String commentTime) {
                this.commentTime = commentTime;
            }

            public String getCommentInfo() {
                return commentInfo;
            }

            public void setCommentInfo(String commentInfo) {
                this.commentInfo = commentInfo;
            }

            public String getCommentUserImagUrl() {
                return commentUserImagUrl;
            }

            public void setCommentUserImagUrl(String commentUserImagUrl) {
                this.commentUserImagUrl = commentUserImagUrl;
            }
        }
    }
}
