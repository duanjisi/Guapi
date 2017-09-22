package com.guapi.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ewuapp.framework.view.widget.BaseDialog;
import com.guapi.R;
import com.guapi.util.ImageLoaderUtils;
import com.guapi.widget.scan.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zijing.sharesdk.BitmapUtil;
import com.zijing.sharesdk.ShareCallBack;
import com.zijing.sharesdk.ShareDialog;
import com.zijing.sharesdk.ShareSdkUtil;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.guapi.tool.Global.TYPE_PIC;
import static com.guapi.tool.Global.TYPE_VIDEO;

/**
 * ╭╯☆★☆★╭╯
 * 　　╰╮★☆★╭╯
 * 　　　 ╯☆╭─╯
 * 　　 ╭ ╭╯
 * 　╔╝★╚╗  ★☆╮     BUG兄，没时间了快上车    ╭☆★
 * 　║★☆★║╔═══╗　╔═══╗　╔═══╗  ╔═══╗
 * 　║☆★☆║║★　☆║　║★　☆║　║★　☆║  ║★　☆║
 * ◢◎══◎╚╝◎═◎╝═╚◎═◎╝═╚◎═◎╝═╚◎═◎╝..........
 *
 * @author Jewel
 * @version 1.0
 * @since 2017/7/9 0009
 */
public class MoneySuccessDialog extends BaseDialog {

    @Bind(R.id.iv_point)
    CircleImageView ivPoint;
    @Bind(R.id.tv_tips1)
    TextView tvTips1;
    @Bind(R.id.tv_tips2)
    TextView tvTips2;
    private File bitmap;
    Context context;
    private String WEIXIN_APP_ID = "wxb3893f7dffa1bd57";
    private IWXAPI iwxapi;
    private ImageLoader imageLoader;
    private String type;

    public MoneySuccessDialog(Context context, File bitmap, String str) {
        super(context, R.style.base_dialog, true);
        iwxapi = WXAPIFactory.createWXAPI(context, WEIXIN_APP_ID);
        iwxapi.registerApp(WEIXIN_APP_ID);
        this.context = context;
        this.bitmap = bitmap;
        this.type = str;
        imageLoader = ImageLoaderUtils.createImageLoader(context);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_money_success;
    }

    @Override
    protected void initView() {
        super.initView();
        ButterKnife.bind(this);
        if (TextUtils.equals(type, TYPE_PIC)) {
            tvTips1.setText("图片藏好了");
            tvTips2.setText("到图片所在位置，接线索扫描找图片");
        } else if (TextUtils.equals(type, TYPE_VIDEO)) {
            tvTips1.setText("视频藏好了");
            tvTips2.setText("到视频所在位置，接线索扫描找视频");
        }
        if (bitmap != null) {
            File file;
//            Glide.with(getContext()).load(bitmap).into(ivPoint);
            imageLoader.displayImage("file://" + bitmap.getAbsolutePath(), ivPoint, ImageLoaderUtils.getDisplayImageOptions());
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_cancel, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                if (onCancelClick != null) {
                    onCancelClick.onCancel();
                }
                break;
            case R.id.btn_submit:
                dismiss();
                inviteShare();
                break;
        }
    }


    private void inviteShare() {
        ShareSdkUtil.share(getContext(), new ShareCallBack() {
            @Override
            public void callBack(ShareDialog.ShareType type) {
                if (type == ShareDialog.ShareType.Wechat) {
                    WXWebpageObject wxWebpageObject = new WXWebpageObject();
                    String url = "http://120.26.94.214:8080/static/share.html";
                    wxWebpageObject.webpageUrl = url;
                    WXMediaMessage msg = new WXMediaMessage(wxWebpageObject);
                    msg.title = "瓜皮分享";
                    msg.description = "瓜皮";
                    Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
                    msg.thumbData = BitmapUtil.bitmapToBytes(thumb);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = String.valueOf(System.currentTimeMillis());
                    req.message = msg;
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                    iwxapi.sendReq(req);
                } else if (type == ShareDialog.ShareType.WechatMoment) {
                    WXWebpageObject wxWebpageObject = new WXWebpageObject();
                    String url = "http://120.26.94.214:8080/static/share.html";
                    wxWebpageObject.webpageUrl = url;
                    WXMediaMessage msg = new WXMediaMessage(wxWebpageObject);
                    msg.title = "瓜皮分享";
                    msg.description = "瓜皮";
                    Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
                    msg.thumbData = BitmapUtil.bitmapToBytes(thumb);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = String.valueOf(System.currentTimeMillis());
                    req.message = msg;
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                    iwxapi.sendReq(req);
                }
            }
        });
    }

    private OnCancelClick onCancelClick;

    public void setOnCancelClick(OnCancelClick onCancelClick) {
        this.onCancelClick = onCancelClick;
    }

    public interface OnCancelClick {
        void onCancel();
    }
}
