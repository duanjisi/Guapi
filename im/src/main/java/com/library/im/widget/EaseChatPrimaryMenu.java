package com.library.im.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ewuapp.framework.common.utils.CheckUtil;
import com.library.im.R;

/**
 * 聊天输入栏主菜单栏
 *
 */
public class EaseChatPrimaryMenu extends EaseChatPrimaryMenuBase implements OnClickListener {
    private EditText editText;
//    private View buttonSetModeKeyboard;
    private LinearLayout edittext_layout;
//    private View buttonSetModeVoice;
    private View buttonSend;
//    private View buttonPressToSpeak;
    private ImageView faceNormal;
    private ImageView faceChecked;
//    private ImageButton buttonMore;
    private RelativeLayout faceLayout;
    private Context context;
    private EaseVoiceRecorderView voiceRecorderView;

    public EaseChatPrimaryMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public EaseChatPrimaryMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EaseChatPrimaryMenu(Context context) {
        super(context);
        init(context, null);
    }

    private void init(final Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.ease_widget_chat_primary_menu, this);
        editText = (EditText) findViewById(R.id.et_sendmessage);
//        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        edittext_layout = (LinearLayout) findViewById(R.id.edittext_layout);
//        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
        buttonSend = findViewById(R.id.btn_send);
//        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        faceNormal = (ImageView) findViewById(R.id.iv_face_normal);
        faceChecked = (ImageView) findViewById(R.id.iv_face_checked);
        faceLayout = (RelativeLayout) findViewById(R.id.rl_face);
//        buttonMore = (ImageButton) findViewById(R.id.btn_more);
        //edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_normal);
        
        buttonSend.setOnClickListener(this);
//        buttonSetModeKeyboard.setOnClickListener(this);
//        buttonSetModeVoice.setOnClickListener(this);
//        buttonMore.setOnClickListener(this);
        faceLayout.setOnClickListener(this);
        editText.setOnClickListener(this);
        editText.requestFocus();
        
//        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_active);
//                } else {
//                    edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_normal);
//                }
//
//            }
//        });
        // 监听文字框
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
//                    buttonMore.setVisibility(GONE);
//                    buttonSend.setVisibility(VISIBLE);
                } else {
//                    buttonMore.setVisibility(VISIBLE);
//                    buttonSend.setVisibility(GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        
        
//        buttonPressToSpeak.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(listener != null){
//                    return listener.onPressToSpeakBtnTouch(v, event);
//                }
//                return false;
//            }
//        });
    }
    
    /**
     * 设置长按说话录制控件
     * @param voiceRecorderView
     */
    public void setPressToSpeakRecorderView(EaseVoiceRecorderView voiceRecorderView){
        this.voiceRecorderView = voiceRecorderView;
    }

    /**
     * 表情输入
     * @param emojiContent
     */
    public void onEmojiconInputEvent(CharSequence emojiContent){
        editText.append(emojiContent);
    }
    
    /**
     * 表情删除
     */
    public void onEmojiconDeleteEvent(){
        if (!TextUtils.isEmpty(editText.getText())) {
            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            editText.dispatchKeyEvent(event);
        }
    }
    
    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view){
        int id = view.getId();
        if (id == R.id.btn_send) {
            if(listener != null){
                String s = editText.getText().toString();
                if(!CheckUtil.isNull(s)){
                    editText.setText("");
                    listener.onSendBtnClicked(s);
                }
            }
        } else if (id == R.id.btn_set_mode_voice) {
            setModeVoice();
            showNormalFaceImage();
            if(listener != null)
                listener.onToggleVoiceBtnClicked();
        } else if (id == R.id.btn_set_mode_keyboard) {
            setModeKeyboard();
            showNormalFaceImage();
            if(listener != null)
                listener.onToggleVoiceBtnClicked();
        } else if (id == R.id.btn_more) {
//            buttonSetModeVoice.setVisibility(VISIBLE);
//            buttonSetModeKeyboard.setVisibility(GONE);
            edittext_layout.setVisibility(VISIBLE);
//            buttonPressToSpeak.setVisibility(GONE);
            showNormalFaceImage();
            if(listener != null)
                listener.onToggleExtendClicked();
        } else if (id == R.id.et_sendmessage) {
            //edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_active);
            faceNormal.setVisibility(VISIBLE);
            faceChecked.setVisibility(INVISIBLE);
            if(listener != null)
                listener.onEditTextClicked();
        } else if (id == R.id.rl_face) {
            toggleFaceImage();
            if(listener != null){
                listener.onToggleEmojiconClicked();
            }
        } else {
        }
    }
    
    
    /**
     * 显示语音图标按钮
     * 
     */
    protected void setModeVoice() {
        hideKeyboard();
        edittext_layout.setVisibility(GONE);
//        buttonSetModeVoice.setVisibility(GONE);
//        buttonSetModeKeyboard.setVisibility(VISIBLE);
        buttonSend.setVisibility(GONE);
//        buttonMore.setVisibility(VISIBLE);
//        buttonPressToSpeak.setVisibility(VISIBLE);
        faceNormal.setVisibility(VISIBLE);
        faceChecked.setVisibility(INVISIBLE);

    }

    /**
     * 显示键盘图标
     */
    protected void setModeKeyboard() {
        edittext_layout.setVisibility(VISIBLE);
//        buttonSetModeKeyboard.setVisibility(GONE);
//        buttonSetModeVoice.setVisibility(VISIBLE);
        // mEditTextContent.setVisibility(View.VISIBLE);
        editText.requestFocus();
        // buttonSend.setVisibility(View.VISIBLE);
//        buttonPressToSpeak.setVisibility(GONE);
        if (TextUtils.isEmpty(editText.getText())) {
//            buttonMore.setVisibility(VISIBLE);
//            buttonSend.setVisibility(GONE);
        } else {
//            buttonMore.setVisibility(GONE);
//            buttonSend.setVisibility(VISIBLE);
        }

    }
    
    
    protected void toggleFaceImage(){
        if(faceNormal.getVisibility() == VISIBLE){
            showSelectedFaceImage();
        }else{
            showNormalFaceImage();
        }
    }
    
    private void showNormalFaceImage(){
        faceNormal.setVisibility(VISIBLE);
        faceChecked.setVisibility(INVISIBLE);
    }
    
    private void showSelectedFaceImage(){
        faceNormal.setVisibility(INVISIBLE);
        faceChecked.setVisibility(VISIBLE);
    }
    

    @Override
    public void onExtendMenuContainerHide() {
        showNormalFaceImage();
    }

}
