package com.guapi.main;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ewuapp.framework.common.utils.CompatUtil;
import com.ewuapp.framework.view.widget.BaseDialog;
import com.ewuapp.framework.view.widget.HorizontalDividerItemDecoration;
import com.ewuapp.framework.view.widget.VerticalDividerItemDecoration;
import com.guapi.R;
import com.guapi.main.adapter.OnSubmitClick;
import com.guapi.main.adapter.PicShowAdapter;

import java.util.List;

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
 * @since 2017/7/10 0010
 */

public class PicDataDialog extends BaseDialog {

    @Bind(R.id.et_message)
    EditText etMessage;
    @Bind(R.id.rv_pic)
    RecyclerView rvPic;
    @Bind(R.id.tv_tip)
    TextView tvTip;
    @Bind(R.id.tv_close)
    TextView tvClose;

    private PicShowAdapter picShowAdapter;
    private List<String> picList;
    private String type;

    protected PicDataDialog(Context context, List<String> picList, String type) {
        super(context, R.style.base_dialog, true);
        this.picList = picList;
        this.type = type;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_pic;
    }

    @Override
    protected void initView() {
        super.initView();
        ButterKnife.bind(this);

        if (TextUtils.equals(type, TYPE_VIDEO)) {
            tvTip.setText("添加视频");
        }
        initPictureList();
    }

    public void refreshDailog(List<String> picList) {
//        picShowAdapter.InitDatas(picList);
        this.picList = picList;
        initPictureList();
    }

    private void initPictureList() {
        rvPic.setLayoutManager(new GridLayoutManager(getContext(), 3));
        picShowAdapter = new PicShowAdapter(picList, type);
        picShowAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    if ((TextUtils.equals(type, TYPE_PIC) && picShowAdapter.getData().size() < 10) ||
                            (TextUtils.equals(type, TYPE_VIDEO) && picShowAdapter.getData().size() < 2))
                        if (onPictureAction != null) {
                            onPictureAction.onRequestAddNewPicture();
                        }
                }
            }
        });
        picShowAdapter.setOnPicActionListener(new PicShowAdapter.OnPicActionListener() {
            @Override
            public void onRemovePic(int position) {
                if (onPictureAction != null) {
                    onPictureAction.onRequestRemovePicture(picShowAdapter.getData().get(position));
                    picShowAdapter.getData().remove(position);
                    picShowAdapter.notifyItemRemoved(position);
                }
            }
        });
        rvPic.setAdapter(picShowAdapter);

        Paint paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setColor(CompatUtil.getColor(getContext(), android.R.color.transparent));
        rvPic.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).paint(paint).build());
        rvPic.addItemDecoration(new VerticalDividerItemDecoration.Builder(getContext()).paint(paint).build());
    }

    @OnClick({R.id.btn_submit, R.id.tv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_close:
                dismiss();
                break;
            case R.id.btn_submit:
                if (picList.size() < 2) {
                    if (TextUtils.equals(type, TYPE_VIDEO)) {
                        ToastUtils.showLong("请添加视频");
                    } else {
                        ToastUtils.showLong("请添加图片");
                    }
                    return;
                }

                String message = "";
                if (!TextUtils.isEmpty(etMessage.getText())) {
                    message = etMessage.getText().toString();
                }

                if (onSubmitClick != null) {
                    onSubmitClick.onSubmitPic(this, message);
                }
                break;
        }
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
    }

    private OnSubmitClick onSubmitClick;

    public void setOnSubmitClick(OnSubmitClick onSubmitClick) {
        this.onSubmitClick = onSubmitClick;
    }

    private OnPictureAction onPictureAction;

    public void setOnPictureAction(OnPictureAction onPictureAction) {
        this.onPictureAction = onPictureAction;
    }

    public interface OnPictureAction {
        void onRequestAddNewPicture();

        void onRequestRemovePicture(String key);
    }
}
