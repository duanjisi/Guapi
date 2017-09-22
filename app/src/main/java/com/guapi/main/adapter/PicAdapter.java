package com.guapi.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guapi.R;
import com.guapi.main.ImagePagerActivity;
import com.guapi.main.player.VideoPlayerActivity;
import com.guapi.model.PicEntity;
import com.guapi.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.guapi.tool.Global.TYPE_VIDEO;

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
 * @since 2017/7/12 0012
 */

public class PicAdapter extends BaseQuickAdapter<PicEntity, BaseViewHolder> {

    private String type;
    private ImageLoader imageLoader;

//    public PicAdapter(@Nullable List<String> data, String type) {
//        super(R.layout.item_pic_data, data);
//        this.type = type;
//        imageLoader = ImageLoaderUtils.createImageLoader(mContext);
//    }

    public PicAdapter(Context context, @Nullable List<PicEntity> data, String type) {
        super(R.layout.item_pic_data, data);
        this.type = type;
        imageLoader = ImageLoaderUtils.createImageLoader(context);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final PicEntity item) {
        ImageView imageView = helper.getView(R.id.iv_pic);
        ImageView player = helper.getView(R.id.iv_play);
        helper.setVisible(R.id.tv_remove, false)
                .setVisible(R.id.iv_play, TextUtils.equals(type, TYPE_VIDEO));
//        Glide.with(helper.itemView.getContext()).load(item).placeholder(R.mipmap.logo).error(R.mipmap.logo).fallback(R.mipmap.logo).into(imageView);
        imageLoader.displayImage(item.getPic(), imageView, ImageLoaderUtils.getDisplayImageOptions());
        if (!TextUtils.equals(type, TYPE_VIDEO)) {
            player.setVisibility(View.GONE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                    List<PicEntity> entities = getData();
                    int position = 0;
                    for (int i = 0; i < entities.size(); i++) {
                        if (item.getPic().equals(entities.get(i).getPic())) {
                            position = i;
                        }
                    }
                    ImagePagerActivity.startImagePagerActivity(mContext, getUrls(), position, imageSize, false);
                }
            });
        } else {
            player.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(item.getVideo())) {
                        Intent intent = new Intent(mContext, VideoPlayerActivity.class);
                        intent.putExtra("videoPath", item.getVideo());
//                        intent.putExtra("imgurl", url_img);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }

    private List<String> getUrls() {
        List<PicEntity> pices = getData();
        List<String> urls = new ArrayList<>();
        if (pices != null && pices.size() != 0) {
            for (int i = 0; i < pices.size(); i++) {
                urls.add(pices.get(i).getPic());
            }
        }
        return urls;
    }
}

