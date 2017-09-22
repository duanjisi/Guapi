package com.library.im.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.library.im.R;
import com.library.im.model.SelfConversation;
import com.library.im.widget.EaseConversationList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会话列表fragment
 */
public class EaseConversationListFragment extends EaseBaseFragment {
    private final static int MSG_REFRESH = 2;
    protected EditText query;
    protected RelativeLayout queryLayout;
    protected ImageButton clearSearch;
    protected boolean hidden;
    protected List<Pair<Long, SelfConversation>> conversationList = new ArrayList<Pair<Long, SelfConversation>>();
    protected EaseConversationList conversationListView;
    protected FrameLayout errorItemContainer;

    protected boolean isConflict;

    protected EMConversationListener convListener = new EMConversationListener() {

        @Override
        public void onCoversationUpdate() {
            refresh();
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ease_fragment_conversation_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //会话列表控件
        conversationListView = (EaseConversationList) getView().findViewById(R.id.list);
        queryLayout = (RelativeLayout) getView().findViewById(R.id.query_layout);
        // 搜索框
        query = (EditText) getView().findViewById(R.id.query);
        queryLayout.setVisibility(View.GONE);
        // 搜索框中清除button
        clearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
        errorItemContainer = (FrameLayout) getView().findViewById(R.id.fl_error_item);
    }

    @Override
    protected void setUpView() {
        conversationList.clear();
        try {
            conversationList.addAll(loadConversationList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        conversationListView.init(conversationList);

        if (listItemClickListener != null) {
            conversationListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Pair<Long, SelfConversation> conversation = conversationListView.getItem(position);
                    listItemClickListener.onListItemClicked(conversation.second);
                }
            });
        }

        EMClient.getInstance().addConnectionListener(connectionListener);

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                conversationListView.filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

        conversationListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
    }


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
    private EaseConversationListItemClickListener listItemClickListener;

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

    /**
     * 连接到服务器
     */
    protected void onConnectionConnected() {
        errorItemContainer.setVisibility(View.GONE);
    }

    /**
     * 连接断开
     */
    protected void onConnectionDisconnected() {
        errorItemContainer.setVisibility(View.VISIBLE);
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

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && !isConflict) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isConflict) {
            outState.putBoolean("isConflict", true);
        }
    }

    public interface EaseConversationListItemClickListener {
        /**
         * 会话listview item点击事件
         *
         * @param conversation 被点击item所对应的会话
         */
        void onListItemClicked(SelfConversation conversation);
    }

    /**
     * 设置listview item点击事件
     *
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(EaseConversationListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

}
