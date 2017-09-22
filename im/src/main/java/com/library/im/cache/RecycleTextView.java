package com.library.im.cache;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewuapp.framework.common.utils.GlideUtil;
import com.library.im.R;
import com.library.im.widget.AvatarWidget;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by longbh on 16/5/26.
 */
public class RecycleTextView extends TextView {

    private AvatarWidget avatarWidget;
    private ImageView head;

    private WeakReference<ContactsCacheUtils.EntityWorkerTask> bitmapWorkerTaskReference;

    public RecycleTextView(Context context) {
        super(context);
    }

    public RecycleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecycleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RecycleTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    public void setTast(ContactsCacheUtils.EntityWorkerTask tast) {
        bitmapWorkerTaskReference = new WeakReference<ContactsCacheUtils.EntityWorkerTask>(tast);
    }

    public ContactsCacheUtils.EntityWorkerTask get() {
        return bitmapWorkerTaskReference == null ? null : bitmapWorkerTaskReference.get();
    }

    public void loadAvatar(String name, String avatar) {
        setText(name);
        if (this.avatarWidget != null) {
            avatarWidget.loadImage(avatar);
        }
        if(this.head != null){
            GlideUtil.loadPicture(avatar, head, R.drawable.default_image);
        }
    }

    public void loadAvatar(String name, List<String> avatar) {
        setText(name);
        if (this.avatarWidget != null) {
            avatarWidget.loadImage(avatar);
        }
    }

    public void reset(){
        setText("");
        if(this.head != null){
            this.head.setImageResource(R.drawable.ease_default_avatar);
        }
        if(this.avatarWidget != null){
            avatarWidget.clear();
        }
    }

    public void setAvatar(AvatarWidget avatarWidget) {
        this.avatarWidget = avatarWidget;
    }

    public void setAvatar(ImageView imageView) {
        this.head = imageView;
    }
}
