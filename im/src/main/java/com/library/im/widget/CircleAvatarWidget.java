package com.library.im.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.List;

/**
 * Author : zhouyx
 * Date   : 2016/9/28
 * 圆形列表头像
 */
public class CircleAvatarWidget extends AvatarWidget {

    private CircleHeadWidget circleHeadWidget;

    public CircleAvatarWidget(Context context) {
        super(context);
    }

    public CircleAvatarWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleAvatarWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        
        circleHeadWidget = new CircleHeadWidget(context);
        circleHeadWidget.setLayoutParams(params);
        addView(circleHeadWidget);
    }

    @Override
    public void loadImage(String storageIds) {
        if (storageIds == null) {
            storageIds = "";
        }
        List<String> imageList = Arrays.asList(storageIds.split(","));
        circleHeadWidget.setHeadViews(imageList);
    }

    @Override
    public void loadImage(List<String> images) {
        circleHeadWidget.setHeadViews(images);
    }

    @Override
    protected void reset() {
        circleHeadWidget.reset();
    }

    @Override
    public void clear() {
        circleHeadWidget.clear();
    }

}
