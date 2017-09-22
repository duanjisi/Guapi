package com.guapi.usercenter.chat;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.library.im.EaseConstant;
import com.library.im.ui.EaseChatFragment;
import com.library.im.widget.chatrow.EaseCustomChatRowProvider;

/**
 * 聊天会话页
 * Created by z on 2016/8/28.
 */
public class ChatFragment extends EaseChatFragment {

    private float touchX = 0, touchY = 0;
    private MessageHandleDialog dialog;
    private EaseChatFragmentListener easeChatFragmentListener = new EaseChatFragmentListener() {
        @Override
        public void onSetMessageAttributes(EMMessage message) {
        }

        @Override
        public void onEnterToChatDetails() {
        }

        @Override
        public void onAvatarClick(String username) {
        }

        @Override
        public boolean onMessageBubbleClick(EMMessage message) {
            return false;
        }

        @Override
        public void onMessageBubbleLongClick(final EMMessage message) {
            dialog.setCallback(new MessageHandleDialog.Callback() {
                @Override
                public void delete() {
                    dialog.dismiss();
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation
                            (fragmentArgs.getString(EaseConstant.EXTRA_USER_ID));
                    conversation.removeMessage(message.getMsgId());
                    messageList.refresh();
                }

                @Override
                public void copy() {
                    if (message.getType() == EMMessage.Type.TXT) {
                        dialog.dismiss();
                        clipboard.setText(message.getBody().toString().replace("txt:", "").replaceAll("\"", ""));
                        Toast.makeText(getActivity(),"已复制到剪贴板",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show(messageList, (int) touchX, (int) touchY);
        }

        @Override
        public boolean onExtendMenuItemClick(int itemId, View view) {
            return false;
        }

        @Override
        public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
            return null;
        }

        @Override
        public void onBubbleTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                touchX = event.getRawX();
                touchY = event.getRawY();
            }
        }
    };

    @Override
    protected void setUpView() {
        super.setUpView();
        hideTitleBar();
        search.setVisibility(View.GONE);
        messageList.setShowUserNick(true);
        setChatFragmentListener(easeChatFragmentListener);
        dialog = new MessageHandleDialog(getActivity());
    }

}
