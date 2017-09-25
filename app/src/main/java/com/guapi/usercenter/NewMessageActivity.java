package com.guapi.usercenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewuapp.framework.common.http.CallBack;
import com.ewuapp.framework.common.http.Constants;
import com.ewuapp.framework.common.utils.AppManager;
import com.ewuapp.framework.common.utils.CheckUtil;
import com.ewuapp.framework.common.utils.IntentUtil;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.CircleImageView;
import com.ewuapp.framework.view.widget.HintDialog;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.guapi.MainActivity;
import com.guapi.R;
import com.guapi.auth.LoginActivity;
import com.guapi.http.Http;
import com.guapi.model.response.RefreshOneMessageResponse;
import com.guapi.usercenter.chat.ChatActivity;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.library.im.EaseConstant;
import com.library.im.model.SelfConversation;
import com.library.im.widget.EaseConversationList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by long on 2017/9/13.
 */

public class NewMessageActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.list)
    EaseConversationList conversationListView;

    private final static int MSG_REFRESH = 2;
    protected List<Pair<Long, SelfConversation>> conversationList = new ArrayList<Pair<Long, SelfConversation>>();
    protected InputMethodManager inputMethodManager;
    protected boolean isConflict;

    @NonNull
    @Override
    protected BasePresenterImpl getPresent() {
        return new BasePresenterImpl(getSupportFragmentManager());
    }

    @NonNull
    @Override
    protected BaseViewPresenterImpl getViewPresent() {
        return new BaseViewPresenterImpl();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_new_message;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String HXHELPER_ON_MESSAGE_RECEIVED = "hxhelper_on_message_received";
        LocalBroadcastManager.getInstance(context).registerReceiver(onNewMessage, new IntentFilter(HXHELPER_ON_MESSAGE_RECEIVED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(onNewMessage);
    }

    private BroadcastReceiver onNewMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    };

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        toolBarView.setTitleText("消息中心");
        toolBarView.setVisibility(View.VISIBLE);
        toolBarView.setDrawable(ToolBarView.TEXT_LEFT, R.mipmap.fhan);
        toolBarView.setOnLeftClickListener(new ToolBarView.OnBarLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

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
                if (position == 0) {
                    Log.e("Long", "0000");
                    return;
                }
                SelfConversation conversation = conversationListView.getItem(position - 1).second;
                String username = conversation.username;
                if (username.equals(EMClient.getInstance().getCurrentUser())) {
                    return;
                }
                // 进入聊天页面
                Intent intent = new Intent(context, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                bundle.putString(EaseConstant.EXTRA_USER_ID, username);
                bundle.putString(EaseConstant.EXTRA_USER_NICK_NAME, conversation.searchName);
                bundle.putInt("unRead", conversation.unread);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        setUpView();
        View headerView = LayoutInflater.from(context).inflate(R.layout.header_message_view, null);
        ivGp = (CircleImageView) headerView.findViewById(R.id.iv_guapi);
        tvGContent = (TextView) headerView.findViewById(R.id.tv_guapi_content);
        tvGTime = (TextView) headerView.findViewById(R.id.tv_guapi_time);
        ivDongtai = (CircleImageView) headerView.findViewById(R.id.iv_dongtai);
        tvDongTaiContent = (TextView) headerView.findViewById(R.id.tv_dongtai_content);
        tvDongTaiTime = (TextView) headerView.findViewById(R.id.tv_dongtai_time);
        conversationListView.addHeaderView(headerView);
        llDongTai = (LinearLayout) headerView.findViewById(R.id.ll_dongtai);
        llGuaPi = (LinearLayout) headerView.findViewById(R.id.ll_guapi);
        llDongTai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("msg_type", "7");
                startActivity(bundle, SystemMessageActivity.class);
            }
        });
        llGuaPi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("msg_type", "1");
                startActivity(bundle, SystemMessageActivity.class);
            }
        });
        loadData();//动态消息
    }

    LinearLayout llDongTai, llGuaPi;
    CircleImageView ivGp;
    TextView tvGContent;
    TextView tvGTime;
    TextView tvDongTaiContent;
    TextView tvDongTaiTime;
    CircleImageView ivDongtai;

    private void setUpView() {
        conversationList.clear();
        try {
            conversationList.addAll(loadConversationList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        conversationListView.init(conversationList);
        if (listItemClickListener != null) {
            conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Pair<Long, SelfConversation> conversation = conversationListView.getItem(position);
                    listItemClickListener.onListItemClicked(conversation.second);
                }
            });
        }
        EMClient.getInstance().addConnectionListener(connectionListener);
        conversationListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
    }

    /**
     * 连接到服务器
     */
    protected void onConnectionConnected() {
//        errorItemContainer.setVisibility(View.GONE);
    }

    /**
     * 连接断开
     */
    protected void onConnectionDisconnected() {
//        errorItemContainer.setVisibility(View.VISIBLE);
    }

    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;
                case MSG_REFRESH: {
                    conversationList.clear();
                    try {
                        conversationList.addAll(loadConversationList());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conversationListView.refresh();
                    break;
                }
                default:
                    break;
            }
        }
    };

    protected EMConnectionListener connectionListener = new EMConnectionListener() {
        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                isConflict = true;
            } else {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };

    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 设置listview item点击事件
     *
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(EaseConversationListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    private EaseConversationListItemClickListener listItemClickListener;

    public interface EaseConversationListItemClickListener {
        /**
         * 会话listview item点击事件
         *
         * @param conversation 被点击item所对应的会话
         */
        void onListItemClicked(SelfConversation conversation);
    }

    HintDialog hintDialog;

    public void deleteMessage(int position) {
        hintDialog = new HintDialog(this, "是否要删除该条消息", new String[]{"取消", "确定"});
        hintDialog.setCallback(new HintDialog.Callback() {
            @Override
            public void callback() {
                SelfConversation conversation = conversationListView.getItem(position - 1).second;
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

    /**
     * 刷新页面
     */
    public void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    /**
     * 获取会话列表
     *
     * @return +
     */
    protected List<Pair<Long, SelfConversation>> loadConversationList() {
        Map<String, EMConversation> conversations = null;
        // 获取所有会话，包括陌生人
        List<Pair<Long, SelfConversation>> list = null;
        try {
            conversations = EMClient.getInstance().chatManager().getAllConversations();
            // 过滤掉messages size为0的conversation
            /**
             * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
             * 影响排序过程，Collection.sort会产生异常
             * 保证Conversation在Sort过程中最后一条消息的时间不变
             * 避免并发问题
             */
            HashMap<String, Pair<Long, SelfConversation>> sortList = new HashMap<>();
            synchronized (conversations) {
                for (EMConversation conversation : conversations.values()) {
                    if (conversation.getAllMessages().size() != 0) {
                        //if(conversation.getType() != EMConversationType.ChatRoom){
                        //    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                        //}
                        //合并群消息
                        if (conversation.isGroup()) {
                            EMGroup group = EMClient.getInstance().groupManager().getGroup(conversation.getUserName());
                            if (group == null) {
                                continue;
                            }
                            //                        Log.e("group", group.getGroupName());
                            String groupName = group.getGroupName().split("_")[0];
                            Pair<Long, SelfConversation> pairMap = sortList.get(groupName);
                            if (pairMap != null) {
                                pairMap.second.unread = pairMap.second.unread + conversation.getUnreadMsgCount();
                            } else {
                                SelfConversation selfConversation = new SelfConversation(conversation.conversationId(), conversation.getUserName(),
                                        conversation.getUnreadMsgCount(), conversation.getLastMessage(), conversation.getType());
                                selfConversation.searchName = group.getGroupName();
                                selfConversation.isGroup = conversation.isGroup();
                                sortList.put(groupName, new Pair<Long, SelfConversation>(conversation.getLastMessage().getMsgTime(), selfConversation));
                            }
                        } else {
                            SelfConversation selfConversation = new SelfConversation(conversation.conversationId(), conversation.getUserName(),
                                    conversation.getUnreadMsgCount(), conversation.getLastMessage(), conversation.getType());
                            selfConversation.searchName = conversation.getUserName();
                            sortList.put(conversation.getUserName(), new Pair<Long, SelfConversation>(conversation.getLastMessage().getMsgTime(), selfConversation));
                        }
                    }
                }
            }
            list = new ArrayList<>(sortList.values());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private void loadData() {
        addDisposable(Http.refreshOne(context, "", new CallBack<RefreshOneMessageResponse>() {
            @Override
            public void handlerSuccess(RefreshOneMessageResponse data) {
                if (data != null && data.getMsListBeen().size() > 0) {
                    for (int i = 0; i < data.getMsListBeen().size(); i++) {
                        if (data.getMsListBeen().get(i).getMs_type().equals("1")) {
                            if (CheckUtil.isNull(data.getMsListBeen().get(i).getMs_content())) {
                                tvGContent.setText("暂无消息");
                            } else {
                                tvGContent.setText(data.getMsListBeen().get(i).getMs_content());
                            }
                            tvGTime.setText(data.getMsListBeen().get(i).getMs_time());
                        } else if (data.getMsListBeen().get(i).getMs_type().equals("7")||data.getMsListBeen().get(i).getMs_type().equals("2")||data.getMsListBeen().get(i).getMs_type().equals("3")||data.getMsListBeen().get(i).getMs_type().equals("4")||data.getMsListBeen().get(i).getMs_type().equals("5")||data.getMsListBeen().get(i).getMs_type().equals("6")) {
                            if (CheckUtil.isNull(data.getMsListBeen().get(i).getMs_content())) {
                                tvDongTaiContent.setText("暂无消息");
                            } else {
                                tvDongTaiContent.setText(data.getMsListBeen().get(i).getMs_content());
                            }
                            tvDongTaiTime.setText(data.getMsListBeen().get(i).getMs_time());
                        }
                    }
                }
            }

            @Override
            public void fail(int code, String message) {
                if (code == Constants.NET_CODE_NEED_LOGIN) {
                    AppManager.getInstance().finishAll();
                    startActivity(null,LoginActivity.class);
                } else {
                    showMessage(message);
                }
            }
        }));
    }

    /**
     * 根据最后一条消息的时间排序
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, SelfConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, SelfConversation>>() {
            @Override
            public int compare(final Pair<Long, SelfConversation> con1, final Pair<Long, SelfConversation> con2) {
                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }
}
