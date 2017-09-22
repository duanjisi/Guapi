package com.guapi.usercenter.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ewuapp.framework.view.adapter.MBaseAdapter;
import com.guapi.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author: long
 * date: ON 2017/7/10.
 */

public class LingAdapter extends MBaseAdapter<String> {
    public LingAdapter(Context context, List<String> data) {
        super(context, data, R.layout.item_label_lings);
    }

    @Override
    protected void newView(View convertView, int position) {
        ViewHolder viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);
    }

    @Override
    protected void holderView(View convertView, String itemObject, int position) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.tvName.setText(itemObject);
    }

    class ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
