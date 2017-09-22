package com.guapi.usercenter.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;

import com.ewuapp.framework.view.widget.HintDialog;
import com.guapi.auth.LoginActivity;
import com.guapi.tool.PreferenceKey;
import com.hyphenate.chat.EMClient;
import com.library.im.EaseConstant;
import com.library.im.model.SelfConversation;
import com.library.im.ui.EaseConversationListFragment;
import com.orhanobut.hawk.Hawk;

/**
 * author: long
 * date: ON 2017/7/14.
 */

public class ConversationFragment extends EaseConversationListFragment {
    private BroadcastReceiver onNewMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    };

    public static ConversationFragment getIntance() {
        Bundle bundle = new Bundle();
        ConversationFragment fragment = new ConversationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        Bundle bundle = getArguments();
        titleBar.setVisibility(View.GONE);
        queryLayout.setVisibility(View.GONE);
        conversationListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteMessage(position);
                return true;
            }
        });
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelfConversation conversation = conversationListView.getItem(position).second;
                String username = conversation.username;
                if (username.equals(EMClient.getInstance().getCurrentUser())) {
                    return;
                }
                // 进入聊天页面
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                bundle.putString(EaseConstant.EXTRA_USER_ID, username);
                bundle.putString(EaseConstant.EXTRA_USER_NICK_NAME, conversation.searchName);
                bundle.putInt("unRead", conversation.unread);
                intent.putExtras(bundle);
                (getActivity()).startActivity(intent);
            }
        });
    }

    HintDialog hintDialog;

    public void deleteMessage(int position) {
        hintDialog = new HintDialog(getActivity(), "是否要删除该条消息", new String[]{"取消", "确定"});
        hintDialog.setCallback(new HintDialog.Callback() {
            @Override
            public void callback() {
                SelfConversation conversation = conversationListView.getItem(position).second;
                String username = conversation.username;
                EMClient.getInstance().chatManager().deleteConversation(username, true);
                refresh();
                hintDialog.dismiss();
            }

            @Override
            public void cancle() {
                hintDialog.dismiss();
            }
        });
        hintDialog.show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        final String HXHELPER_ON_MESSAGE_RECEIVED = "hxhelper_on_message_received";
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onNewMessage, new IntentFilter(HXHELPER_ON_MESSAGE_RECEIVED));
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onNewMessage);
    }
}
