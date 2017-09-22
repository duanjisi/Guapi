package com.guapi.model;

/**
 * Created by Johnny on 2017-09-14.
 */
public class PicEntity {
    private String pic;
    private String video;

    public PicEntity(String pic, String video) {
        this.pic = pic;
        this.video = video;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
