package com.library.im.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.library.im.R;
import com.library.im.adapter.EaseConversationAdapater;
import com.library.im.model.SelfConversation;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Pair;
import android.widget.ListView;

public class EaseConversationList extends ListView {

    protected final int MSG_REFRESH_ADAPTER_DATA = 0;
    
    protected Context context;
    protected EaseConversationAdapater adapter;
    protected List<Pair<Long, SelfConversation>> conversations = new ArrayList<>();
    protected List<Pair<Long, SelfConversation>> passedListRef = null;
    
    
    public EaseConversationList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    
    public EaseConversationList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    
    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseConversationList);
        ta.recycle();
    }
    
    public void init(List<Pair<Long, SelfConversation>> conversationList){
    	passedListRef = conversationList;
        conversations.addAll(conversationList);
        
        adapter = new EaseConversationAdapater(context, 0, conversations);
        setAdapter(adapter);
    }
    
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
            case MSG_REFRESH_ADAPTER_DATA:
                if (adapter != null) {
                	adapter.clear();
                    conversations.clear();
                    conversations.addAll(passedListRef);
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
            }
        }
    };
    
    public Pair<Long, SelfConversation> getItem(int position) {
        return (Pair<Long, SelfConversation>)adapter.getItem(position);
    }
    
    public void refresh() {
    	if(!handler.hasMessages(MSG_REFRESH_ADAPTER_DATA)){
    		handler.sendEmptyMessage(MSG_REFRESH_ADAPTER_DATA);
    	}
    }
    
    public void filter(CharSequence str) {
        adapter.getFilter().filter(str);
    }
    
    
    
}
