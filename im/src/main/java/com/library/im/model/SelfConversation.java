package com.library.im.model;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

/**
 * Created by longbh on 16/7/1.
 */
public class SelfConversation {
    public String conversationid;
    public String username;
    public int unread = 0;
    public EMMessage message;
    public EMConversation.EMConversationType type;
    public String searchName;
    public boolean isGroup = false;

    public SelfConversation(String conversationid,String username,int unread,EMMessage message,EMConversation.EMConversationType type){
        this.conversationid = conversationid;
        this.username = username;
        this.unread = unread;
        this.message = message;
        this.type = type;
    }

}
