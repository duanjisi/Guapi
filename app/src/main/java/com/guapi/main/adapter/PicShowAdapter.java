package com.guapi.main.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guapi.R;

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
 * @since 2017/7/10 0010
 */

public class PicShowAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private String type;

    public PicShowAdapter(@Nullable List<String> data, String type) {
        super(R.layout.item_pic_data, data);
        this.type = type;
    }

    public void InitDatas(List<String> list) {
        List<String> datas = getData();
        if (datas != null) {
            datas.clear();
            datas.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        ImageView imageView = helper.getView(R.id.iv_pic);
        if (helper.getAdapterPosition() == 0) {
            helper.setVisible(R.id.iv_play, false);
            helper.setVisible(R.id.tv_remove, false);
            Glide.with(helper.itemView.getContext()).load(R.mipmap.ic_add_pic).into(imageView);
        } else {
            helper.setVisible(R.id.tv_remove, true);
            Glide.with(helper.itemView.getContext()).load(item).into(imageView);
            if (onPicActionListener != null) {
                helper.getView(R.id.tv_remove).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPicActionListener.onRemovePic(helper.getAdapterPosition());
                    }
                });
            }
            helper.setVisible(R.id.iv_play, TextUtils.equals(type, TYPE_VIDEO));
        }
    }

    private OnPicActionListener onPicActionListener;

    public void setOnPicActionListener(OnPicActionListener onPicActionListener) {
        this.onPicActionListener = onPicActionListener;
    }

    public interface OnPicActionListener {
        void onRemovePic(int position);
    }
}
