package com.library.im.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.ewuapp.framework.common.utils.GlideUtil;
import com.ewuapp.framework.view.widget.CircleImageView;
import com.library.im.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : zhouyx
 * Date   : 2016/9/27
 * 圆形列表头像
 */
public class CircleHeadWidget extends FrameLayout {

    private View headViewOne, headViewTwo, headViewThree;
    private FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT);

    public CircleHeadWidget(Context context) {
        this(context, null, 0);
    }

    public CircleHeadWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleHeadWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        headViewOne = LayoutInflater.from(context).inflate(R.layout.widget_circle_head_view_one, this, false);
        headViewTwo = LayoutInflater.from(context).inflate(R.layout.widget_circle_head_view_two, this, false);
        headViewThree = LayoutInflater.from(context).inflate(R.layout.widget_circle_head_view_three, this,
                false);
    }

    public void setHeadView(String imageUrl) {
        if ("".equals(imageUrl) || "null".equals(imageUrl)) {
            removeAllViews();
            setDefaultHeadView();
            return;
        }
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add(imageUrl);
        setHeadViews(imageUrls);
    }

    public void setHeadViews(List<String> imageUrls) {
        removeAllViews();
        if (imageUrls == null || imageUrls.size() == 0) {
            setDefaultHeadView();
            return;
        }
        int size = imageUrls.size();
        switch (size) {
            case 1:
                setHeadViewOne(imageUrls);
                break;
            case 2:
                setHeadViewTwo(imageUrls);
                break;
            case 3:
                setHeadViewThree(imageUrls);
                break;
        }
    }

    private void setDefaultHeadView() {
        CircleImageView view = (CircleImageView) headViewOne.findViewById(R.id.avatar);
        GlideUtil.loadPicture("", view);
        addView(headViewOne, params);
    }

    private void setHeadViewOne(List<String> imageUrls) {
        CircleImageView view = (CircleImageView) headViewOne.findViewById(R.id.avatar);
        GlideUtil.loadPicture(imageUrls.get(0), view);
        addView(headViewOne, params);
    }

    private void setHeadViewTwo(List<String> imageUrls) {
        CircleImageView view = (CircleImageView) headViewTwo.findViewById(R.id.avatar);
        CircleImageView viewTwo = (CircleImageView) headViewTwo.findViewById(R.id.avatar_two);
        GlideUtil.loadPicture(imageUrls.get(0), view);
        GlideUtil.loadPicture(imageUrls.get(1), viewTwo);
        addView(headViewTwo, params);
    }

    private void setHeadViewThree(List<String> imageUrls) {
        CircleImageView view = (CircleImageView) headViewThree.findViewById(R.id.avatar);
        CircleImageView viewTwo = (CircleImageView) headViewThree.findViewById(R.id.avatar_two);
        CircleImageView viewThree = (CircleImageView) headViewThree.findViewById(R.id.avatar_three);
        GlideUtil.loadPicture(imageUrls.get(0), view);
        GlideUtil.loadPicture(imageUrls.get(1), viewTwo);
        GlideUtil.loadPicture(imageUrls.get(0), viewThree);
        addView(headViewThree, params);
    }

    public void reset() {
        removeAllViews();
        setDefaultHeadView();
    }

    public void clear() {
        removeAllViews();
        setDefaultHeadView();
    }

}
