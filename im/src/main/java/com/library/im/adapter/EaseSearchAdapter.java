/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.library.im.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.library.im.EaseConstant;
import com.library.im.utils.EaseCommonUtils;
import com.library.im.widget.EaseChatMessageList.MessageListItemClickListener;
import com.library.im.widget.chatrow.EaseChatRow;
import com.library.im.widget.chatrow.EaseChatRowBigExpression;
import com.library.im.widget.chatrow.EaseChatRowFile;
import com.library.im.widget.chatrow.EaseChatRowImage;
import com.library.im.widget.chatrow.EaseChatRowLocation;
import com.library.im.widget.chatrow.EaseChatRowText;
import com.library.im.widget.chatrow.EaseChatRowVideo;
import com.library.im.widget.chatrow.EaseChatRowVoice;
import com.library.im.widget.chatrow.EaseCustomChatRowProvider;

import java.util.List;

public class EaseSearchAdapter extends BaseAdapter{

    private final static String TAG = "msg";

    private Context context;

    private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
    private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
    private static final int HANDLER_MESSAGE_SEEK_TO = 2;

    private static final int MESSAGE_TYPE_RECV_TXT = 0;
    private static final int MESSAGE_TYPE_SENT_TXT = 1;
    private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
    private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
    private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
    private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
    private static final int MESSAGE_TYPE_SENT_VOICE = 6;
    private static final int MESSAGE_TYPE_RECV_VOICE = 7;
    private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
    private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
    private static final int MESSAGE_TYPE_SENT_FILE = 10;
    private static final int MESSAGE_TYPE_RECV_FILE = 11;
    private static final int MESSAGE_TYPE_SENT_EXPRESSION = 12;
    private static final int MESSAGE_TYPE_RECV_EXPRESSION = 13;


    public int itemTypeCount;

    private String toChatUsername;

    private MessageListItemClickListener itemClickListener;
    private EaseCustomChatRowProvider customRowProvider;


    private List<EMMessage> datas;
    private int chatType;


    public EaseSearchAdapter(Context context, String username, int chatType, List<EMMessage> datas) {
        this.context = context;
        this.datas = datas;
        toChatUsername = username;
        this.chatType = chatType;
    }

    public EMMessage getItem(int position) {
        return datas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * 获取item数
     */
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    /**
     * 获取item类型数
     */
    public int getViewTypeCount() {
        if(customRowProvider != null && customRowProvider.getCustomChatRowTypeCount() > 0){
            return customRowProvider.getCustomChatRowTypeCount() + 14;
        }
        return 14;
    }


    /**
     * 获取item类型
     */
    public int getItemViewType(int position) {
        EMMessage message = getItem(position);
        if (message == null) {
            return -1;
        }

        if(customRowProvider != null && customRowProvider.getCustomChatRowType(message) > 0){
            return customRowProvider.getCustomChatRowType(message) + 13;
        }

        if (message.getType() == EMMessage.Type.TXT) {
            if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
                return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_EXPRESSION : MESSAGE_TYPE_SENT_EXPRESSION;
            }
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
        }
        if (message.getType() == EMMessage.Type.IMAGE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;

        }
        if (message.getType() == EMMessage.Type.LOCATION) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
        }
        if (message.getType() == EMMessage.Type.VOICE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
        }
        if (message.getType() == EMMessage.Type.VIDEO) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO : MESSAGE_TYPE_SENT_VIDEO;
        }
        if (message.getType() == EMMessage.Type.FILE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
        }

        return -1;// invalid
    }

    protected EaseChatRow createChatRow(Context context, EMMessage message, int position) {
        EaseChatRow chatRow = null;
        if(customRowProvider != null && customRowProvider.getCustomChatRow(message, position, this) != null){
            return customRowProvider.getCustomChatRow(message, position, this);
        }
        switch (message.getType()) {
            case TXT:
                if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
                    chatRow = new EaseChatRowBigExpression(context, message, position, this);
                }else{
                    chatRow = new EaseChatRowText(context, message, position, this);
                }
                break;
            case LOCATION:
                chatRow = new EaseChatRowLocation(context, message, position, this);
                break;
            case FILE:
                chatRow = new EaseChatRowFile(context, message, position, this);
                break;
            case IMAGE:
                chatRow = new EaseChatRowImage(context, message, position, this);
                break;
            case VOICE:
                chatRow = new EaseChatRowVoice(context, message, position, this);
                break;
            case VIDEO:
                chatRow = new EaseChatRowVideo(context, message, position, this);
                break;
            default:
                break;
        }

        return chatRow;
    }


    @SuppressLint("NewApi")
    public View getView(final int position, View convertView, ViewGroup parent) {
        EMMessage message = getItem(position);
        if(convertView == null){
            convertView = createChatRow(context, message, position);
        }
        //缓存的view的message很可能不是当前item的，传入当前message和position更新ui
        ((EaseChatRow)convertView).setUpView(message, position, itemClickListener);

        return convertView;
    }

}
