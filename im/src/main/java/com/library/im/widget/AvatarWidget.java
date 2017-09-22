package com.library.im.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ewuapp.framework.common.utils.GlideUtil;
import com.library.im.R;

import java.util.List;

/**
 * Created by longbh on 16/7/1.
 */
public class AvatarWidget extends LinearLayout {

    ImageView avatar1, avatar2, avatar3, avatar4;
    LinearLayout line2;

    public AvatarWidget(Context context) {
        super(context);
        init(context);
    }

    public AvatarWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AvatarWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        LayoutInflater.from(context).inflate(com.library.im.R.layout.widget_avatar, this);
        avatar1 = (ImageView) findViewById(R.id.avatar_1);
        avatar2 = (ImageView) findViewById(R.id.avatar_2);
        avatar3 = (ImageView) findViewById(R.id.avatar_3);
        avatar4 = (ImageView) findViewById(R.id.avatar_4);
        line2 = (LinearLayout) findViewById(R.id.line2);
    }

    public void loadImage(String storageIds) {
        reset();
        if (storageIds == null) {
            storageIds = "";
        }
        String[] images = storageIds.split(",");
        if (images.length == 1) {
            GlideUtil.loadPicture(images[0], avatar1, R.drawable.default_image);
            avatar2.setVisibility(View.GONE);
            avatar3.setVisibility(View.GONE);
            avatar4.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
        } else if (images.length == 2) {
            GlideUtil.loadPicture(images[0], avatar1, R.drawable.ease_default_avatar);
            GlideUtil.loadPicture(images[1], avatar2, R.drawable.ease_default_avatar);
            avatar3.setVisibility(View.GONE);
            avatar4.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
        } else if (images.length == 3) {
            GlideUtil.loadPicture(images[0], avatar1, R.drawable.ease_default_avatar);
            GlideUtil.loadPicture(images[1], avatar2, R.drawable.ease_default_avatar);
            GlideUtil.loadPicture(images[2], avatar3, R.drawable.ease_default_avatar);
            avatar4.setVisibility(View.GONE);
            line2.setVisibility(View.VISIBLE);
        } else {
            GlideUtil.loadPicture(images[0], avatar1, R.drawable.ease_default_avatar);
            GlideUtil.loadPicture(images[1], avatar2, R.drawable.ease_default_avatar);
            GlideUtil.loadPicture(images[2], avatar3, R.drawable.ease_default_avatar);
            GlideUtil.loadPicture(images[3], avatar4, R.drawable.ease_default_avatar);
            line2.setVisibility(View.VISIBLE);
        }
    }

    public void loadImage(List<String> images) {
        reset();
        if (images.size() == 1) {
            GlideUtil.loadPicture(images.get(0), avatar1, R.drawable.default_image);
            avatar2.setVisibility(View.GONE);
            avatar3.setVisibility(View.GONE);
            avatar4.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
        } else if (images.size() == 2) {
            GlideUtil.loadPicture(images.get(0), avatar1, R.drawable.ease_default_avatar);
            GlideUtil.loadPicture(images.get(1), avatar2, R.drawable.ease_default_avatar);
            avatar3.setVisibility(View.GONE);
            avatar4.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
        } else if (images.size() == 3) {
            GlideUtil.loadPicture(images.get(0), avatar1, R.drawable.ease_default_avatar);
            GlideUtil.loadPicture(images.get(1), avatar2, R.drawable.ease_default_avatar);
            GlideUtil.loadPicture(images.get(2), avatar3, R.drawable.ease_default_avatar);
            avatar4.setVisibility(View.GONE);
            line2.setVisibility(View.VISIBLE);
        } else {
            GlideUtil.loadPicture(images.get(0), avatar1, R.drawable.ease_default_avatar);
            GlideUtil.loadPicture(images.get(1), avatar2, R.drawable.ease_default_avatar);
            GlideUtil.loadPicture(images.get(2), avatar3, R.drawable.ease_default_avatar);
            GlideUtil.loadPicture(images.get(3), avatar4, R.drawable.ease_default_avatar);
            line2.setVisibility(View.VISIBLE);
        }
    }

    protected void reset() {
        avatar2.setVisibility(View.VISIBLE);
        avatar3.setVisibility(View.VISIBLE);
        avatar4.setVisibility(View.VISIBLE);
        line2.setVisibility(View.VISIBLE);
    }

    public void clear() {
        avatar2.setVisibility(View.GONE);
        avatar3.setVisibility(View.GONE);
        avatar4.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);
        avatar1.setImageResource(R.drawable.ease_default_avatar);
    }

}
