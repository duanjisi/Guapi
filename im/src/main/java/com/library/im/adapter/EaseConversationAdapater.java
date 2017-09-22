package com.library.im.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;
import com.library.im.R;
import com.library.im.cache.ContactsCacheUtils;
import com.library.im.cache.RecycleTextView;
import com.library.im.model.SelfConversation;
import com.library.im.utils.EaseCommonUtils;
import com.library.im.utils.EaseSmileUtils;
import com.library.im.widget.CircleAvatarWidget;

import java.util.Date;
import java.util.List;

/**
 * 会话列表adapter
 */
public class EaseConversationAdapater extends ArrayAdapter<Pair<Long, SelfConversation>> {
    private static final String TAG = "ChatAllHistoryAdapter";
    private List<Pair<Long, SelfConversation>> conversationList;

    public EaseConversationAdapater(Context context, int resource,
                                    List<Pair<Long, SelfConversation>> objects) {
        super(context, resource, objects);
        conversationList = objects;
    }

    @Override
    public int getCount() {
        return conversationList.size();
    }

    @Override
    public Pair<Long, SelfConversation> getItem(int arg0) {
        if (arg0 < conversationList.size()) {
            return conversationList.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ease_row_chat_history, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.name = (RecycleTextView) convertView.findViewById(R.id.name);
            holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.avatar = (CircleAvatarWidget) convertView.findViewById(R.id.avatar);
            holder.msgState = convertView.findViewById(R.id.msg_state);
            holder.list_itease_layout = (RelativeLayout) convertView.findViewById(R.id.list_itease_layout);
            holder.name.setAvatar(holder.avatar);
            convertView.setTag(holder);
        }
//        holder.list_itease_layout.setBackgroundResource(R.drawable.thread_selector);

        // 获取与此用户/群组的会话
        Pair<Long, SelfConversation> pairConver = getItem(position);
        // 获取用户username或者群组groupid
        SelfConversation conversation = pairConver.second;
        ContactsCacheUtils.getInstance(getContext()).loadText(conversation.conversationid, holder.name);
        if (conversation.unread > 0) {
            // 显示与此用户的消息未读数
            holder.unreadLabel.setText(String.valueOf(conversation.unread));
            holder.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            holder.unreadLabel.setVisibility(View.INVISIBLE);
        }

        EMMessage lastMessage = conversation.message;
        holder.message.setText(EaseSmileUtils.getSmiledText(getContext(), EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext()))),
                BufferType.SPANNABLE);

//        holder.message.setText(EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext())));

        holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
        if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
            holder.msgState.setVisibility(View.VISIBLE);
        } else {
            holder.msgState.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        /**
         * 和谁的聊天记录
         */
        RecycleTextView name;
        /**
         * 消息未读数
         */
        TextView unreadLabel;
        /**
         * 最后一条消息的内容
         */
        TextView message;
        /**
         * 最后一条消息的时间
         */
        TextView time;
        /**
         * 用户头像
         */
        CircleAvatarWidget avatar;
        /**
         * 最后一条消息的发送状态
         */
        View msgState;
        /**
         * 整个list中每一行总布局
         */
        RelativeLayout list_itease_layout;

    }
}

