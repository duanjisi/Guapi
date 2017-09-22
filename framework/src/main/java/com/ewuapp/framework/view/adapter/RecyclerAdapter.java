package com.ewuapp.framework.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by longbh on 16/6/1.
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private List<T> datas;
    private int resource;
    protected OnItemListener itemListener;
    protected Context context;

    public RecyclerAdapter(List<T> datas, int resource) {
        super();
        this.datas = datas;
        this.resource = resource;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(
                resource, parent, false);
        BaseViewHolder holder = holder(view, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setOnItemClickListener(itemListener);
        holder.build(datas.get(position), position, datas);
        holder.build(datas.get(position), position);
    }

    public abstract BaseViewHolder holder(View view, int viewType);

    public void setOnItemClickListener(OnItemListener itemClickListener) {
        this.itemListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
