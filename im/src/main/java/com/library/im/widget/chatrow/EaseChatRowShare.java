package com.library.im.widget.chatrow;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.ewuapp.framework.common.utils.GlideUtil;
import com.ewuapp.framework.common.utils.JsonUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.exceptions.HyphenateException;
import com.library.im.R;
import com.library.im.controller.HxHelper;
import com.library.im.utils.EaseSmileBackUtils;

import java.util.Map;

public class EaseChatRowShare extends EaseChatRow{

	private TextView contentView;
    private ImageView image,play;
    private RelativeLayout image_layout;

    public EaseChatRowShare(Context context, EMMessage message, int position, BaseAdapter adapter) {
		super(context, message, position, adapter);
	}

	@Override
	protected void onInflatView() {
		inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
				R.layout.ease_row_received_share : R.layout.ease_row_sent_share, this);
	}

	@Override
	protected void onFindViewById() {
		contentView = (TextView) findViewById(R.id.content);
        image = (ImageView) findViewById(R.id.image1);
        play = (ImageView) findViewById(R.id.img_play);
        image_layout = (RelativeLayout) findViewById(R.id.image_layout);
	}

    @Override
    public void onSetUpView() {
        String content = message.getStringAttribute("content","");
        if(content == null){
            play.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            contentView.setText(message.getBody().toString());
            findViewById(R.id.image_layout).setVisibility(View.GONE);
        }else{
            String type = message.getStringAttribute("type","");
            if("2".equals(type)){
                play.setVisibility(View.VISIBLE);
                image.setVisibility(View.VISIBLE);
                GlideUtil.loadPicture(message.getStringAttribute("image",""),image,R.drawable.default_image);
            }else if("1".equals(type)){
                play.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
                GlideUtil.loadPicture(message.getStringAttribute("image",""),image,R.drawable.default_image);
            }else{
                play.setVisibility(View.GONE);
                image.setVisibility(View.GONE);
                image_layout.setVisibility(View.GONE);
            }
            Spannable span = EaseSmileBackUtils.getSmiledText(context, message.getStringAttribute("content",""));
            contentView.setText(span, BufferType.SPANNABLE);
        }
        handleTextMessage();
    }

    protected void handleTextMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
            case CREATE: 
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.VISIBLE);
                // 发送消息
//                sendMsgInBackground(message);
                break;
            case SUCCESS: // 发送成功
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.GONE);
                break;
            case FAIL: // 发送失败
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.VISIBLE);
                break;
            case INPROGRESS: // 发送中
                progressBar.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                break;
            default:
               break;
            }
        }else{
            if(!message.isAcked() && message.getChatType() == ChatType.Chat){
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        final Map<String,String> data = JsonUtil.fromJson(message.getStringAttribute("content",""),Map.class);
        if(data != null){

            HxHelper.getInstance().startActivity(activity,data.get("id"));
        }
    }

}
