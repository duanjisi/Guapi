package com.guapi.main.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.guapi.R;

/**
 * Created by Johnny on 2017-09-06.
 */
public class PointAdapter extends ABaseAdapter<PoiItem> {

    private Context context;

    public PointAdapter(Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected View setConvertView(int position, PoiItem entity, View convertView) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_address_point, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (entity != null) {
            holder.tvArea.setText("" + entity.getTitle());
            holder.tvAddress.setText("" + entity.getSnippet());
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvArea;
        TextView tvAddress;
        public ViewHolder(View view) {
            this.tvArea = (TextView) view.findViewById(R.id.tv_area);
            this.tvAddress = (TextView) view.findViewById(R.id.tv_address);
        }
    }
}
