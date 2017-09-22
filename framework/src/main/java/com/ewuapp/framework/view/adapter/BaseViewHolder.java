package com.ewuapp.framework.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by longbh on 16/6/1.
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    private OnItemListener itemListener;

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemListener != null) {
                    itemListener.onItem(view, getPosition());
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemListener itemClickListener) {
        this.itemListener = itemClickListener;
    }

    //添加一个方法，当子类有可能需要集合，position
    public void build(T object, int position, List<T> datas) {

    }

    public abstract void build(T object,int position);
}
