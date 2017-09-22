package com.guapi.main;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ewuapp.framework.common.utils.CompatUtil;
import com.ewuapp.framework.common.utils.StringFormat;
import com.ewuapp.framework.view.widget.BaseDialog;
import com.guapi.R;
import com.guapi.main.adapter.OnSubmitClick;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
 * @since 2017/7/8 0008
 */

public class MoneyDataDialog extends BaseDialog {
    @Bind(R.id.et_money)
    EditText etMoney;
    @Bind(R.id.et_count)
    EditText etCount;
    @Bind(R.id.et_message)
    EditText etMessage;
    @Bind(R.id.tv_wallet)
    TextView tvWallet;
    @Bind(R.id.tv_all)
    TextView tvAll;
    @Bind(R.id.tv_friend)
    TextView tvFriend;

    private int selectedPayType = 0;
    private int selectedShowType = 0;

    @Override
    protected int getWidth() {
        return super.getWidth();
    }

    protected MoneyDataDialog(Context context) {
        super(context, R.style.base_dialog, true);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_money;
    }

    @Override
    protected void initView() {
        super.initView();
        ButterKnife.bind(this);
        etMoney.requestFocus();
        tvWallet.setText(StringFormat.formatForRes(R.string.wallet_money, "0"));
    }

    @OnClick({R.id.tv_close, R.id.tv_all, R.id.tv_friend, R.id.tv_wx, R.id.tv_alipay, R.id.tv_wallet, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_close:
                dismiss();
                break;
            case R.id.tv_wx:
                break;
            case R.id.tv_alipay:
                break;
            case R.id.tv_wallet:
                break;
            case R.id.btn_submit:
                submit();
                break;
            case R.id.tv_all:
                selectedShowType = 1;
                tvAll.setBackgroundResource(R.drawable.bg_red_stroke);
                tvFriend.setBackgroundResource(R.drawable.bg_black_stroke);
                tvAll.setTextColor(CompatUtil.getColor(getContext(), R.color.md_red_A400));
                tvFriend.setTextColor(CompatUtil.getColor(getContext(), R.color.B_black));
                break;
            case R.id.tv_friend:
                selectedShowType = 2;
                tvAll.setBackgroundResource(R.drawable.bg_black_stroke);
                tvFriend.setBackgroundResource(R.drawable.bg_red_stroke);
                tvAll.setTextColor(CompatUtil.getColor(getContext(), R.color.B_black));
                tvFriend.setTextColor(CompatUtil.getColor(getContext(), R.color.md_red_A400));
                break;
        }
    }

    private void submit() {
        if(TextUtils.isEmpty(etMoney.getText())) {
            ToastUtils.showLong("请输入红包额度");
            return;
        }
        if(TextUtils.isEmpty(etCount.getText())) {
            ToastUtils.showLong("请输入红包个数");
            return;
        }
        String money = etMoney.getText().toString();
        String count = etCount.getText().toString();
        String showType = "" + selectedShowType;
        String message = "";
        if(!TextUtils.isEmpty(etMessage.getText())) {
            message = etMessage.getText().toString();
        }
        if(onSubmitClick != null) {
            onSubmitClick.onSubmitHB(this, money, count, showType, message);
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
}
