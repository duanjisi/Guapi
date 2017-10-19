package com.guapi.usercenter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ewuapp.framework.common.utils.CheckUtil;
import com.ewuapp.framework.presenter.Impl.BasePresenterImpl;
import com.ewuapp.framework.presenter.Impl.BaseViewPresenterImpl;
import com.ewuapp.framework.view.BaseActivity;
import com.ewuapp.framework.view.widget.ToolBarView;
import com.guapi.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author: long
 * date: ON 2017/7/28.
 */

public class ChooseLabelActivity extends BaseActivity<BasePresenterImpl, BaseViewPresenterImpl> {
    @Bind(R.id.titleBar)
    ToolBarView toolBarView;
    @Bind(R.id.cb_go_to_bed_late_man)
    CheckBox cbGoToBedLateMan;//夜猫子
    @Bind(R.id.cb_young_cynic)
    CheckBox cbYoungCynic;//愤青
    @Bind(R.id.cb_little_fresh_meat)
    CheckBox cbLittleFreshMeat;//小鲜肉
    @Bind(R.id.cb_lolita)
    CheckBox cbLolita;//萝莉
    @Bind(R.id.cb_quadratic_element)
    CheckBox cbQuadrticElement;//二次元
    @Bind(R.id.cb_other)
    CheckBox cbOther;//其他

    List<CheckBox> cbList = new ArrayList<>();
    List<String> contentList = new ArrayList<>();
    String label = "";
    String firstLabel;

    @NonNull
    @Override
    protected BasePresenterImpl getPresent() {
        return new BasePresenterImpl(getSupportFragmentManager());
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            firstLabel = bundle.getString("lable", "");
        }
    }

    @NonNull
    @Override
    protected BaseViewPresenterImpl getViewPresent() {
        return new BaseViewPresenterImpl();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_choose_label;
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        toolBarView.setTitleText("标签");
        toolBarView.setDrawable(ToolBarView.TEXT_LEFT, R.mipmap.fhan);
        toolBarView.setOnLeftClickListener(new ToolBarView.OnBarLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
        toolBarView.setRightText("确定");
        toolBarView.setTextColor(ToolBarView.TEXT_RIGHT, Color.parseColor("#ffffff"));
        toolBarView.setOnRightClickListener(new ToolBarView.OnBarRightClickListener() {
            @Override
            public void onRightClick(View v) {
                for (int i = 0; i < 6; i++) {
                    if (cbList.get(i).isChecked()) {
                        if (CheckUtil.isNull(label)) {
                            label = contentList.get(i);
                        } else {
                            label = label + "," + contentList.get(i);
                        }
                    }
                }
                if (!CheckUtil.isNull(label)) {
                    Intent intent = new Intent();
                    intent.putExtra("LABEL", label);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    showMessage("请至少选择一个标签");
                }
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        cbList.add(cbGoToBedLateMan);
        cbList.add(cbYoungCynic);
        cbList.add(cbLittleFreshMeat);
        cbList.add(cbLolita);
        cbList.add(cbQuadrticElement);
        cbList.add(cbOther);

        contentList.add("夜猫子");
        contentList.add("愤青");
        contentList.add("小鲜肉");
        contentList.add("萝莉");
        contentList.add("二次元");
        contentList.add("其他");
        if (!CheckUtil.isNull(firstLabel)) {
            cbList.get(0).setChecked(false);
            for (int i = 0; i < contentList.size(); i++) {
                if (firstLabel.contains(contentList.get(i))) {
                    cbList.get(i).setChecked(true);
                }
            }
        }
    }
}
