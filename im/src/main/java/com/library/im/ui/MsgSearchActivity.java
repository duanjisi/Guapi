package com.library.im.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ewuapp.framework.common.utils.CheckUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.library.im.EaseConstant;
import com.library.im.R;
import com.library.im.adapter.EaseSearchAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longbh on 16/6/5.
 */
public class MsgSearchActivity extends EaseBaseActivity {

    protected ListView listView;
    protected String toChatUsername;
    protected EMConversation conversation;
    private EditText queryText;
    private TextView cancel;

    private int chatType;
    List<EMMessage> messages;
    EaseSearchAdapter adapter;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_message_search);
        initActivity();
    }


    private void initActivity() {
        messages = new ArrayList<>();
        listView = (ListView) findViewById(R.id.message_list);
        queryText = (EditText) findViewById(R.id.query);
        cancel = (TextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        queryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (CheckUtil.isNull(editable)) {
                    messages.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    messages.clear();
                    List<EMMessage> datas = conversation.searchMsgFromDB(editable+"", -1, -1, null, EMConversation.EMSearchDirection.UP);
                    messages.addAll(datas);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            toChatUsername = bundle.getString("hid");
            chatType = bundle.getInt("chat_type");
        }
        if(chatType == EaseConstant.CHATTYPE_SINGLE){
            conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EMConversation.EMConversationType.Chat, true);
        }else if(chatType == EaseConstant.CHATTYPE_GROUP){
            conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EMConversation.EMConversationType.GroupChat, true);
        }else{
            conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EMConversation.EMConversationType.ChatRoom, true);
        }

        adapter = new EaseSearchAdapter(this, toChatUsername, chatType, messages);
        listView.setAdapter(adapter);
    }
}
