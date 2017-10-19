package com.guapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.guapi.main.base.BaseActivity;
import com.guapi.tool.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Johnny on 2017-09-25.
 */
public class ImageTestActivity extends BaseActivity {

    @Bind(R.id.iv_image)
    ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_image);
        ButterKnife.bind(this);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person);
        if (bitmap != null) {
            Bitmap bit = Utils.ImageCrop(context,bitmap, true, 200);
            if (bit != null) {
                ivImage.setImageBitmap(bit);
            }
        }
    }
}
