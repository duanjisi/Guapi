package com.guapi.usercenter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

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
    @Bind(R.id.tv_go_to_bed_late_man)
    TextView tvGoToBedLateMan;//夜猫子
    @Bind(R.id.tv_young_cynic)
    TextView tvYoungCynic;//愤青
    @Bind(R.id.tv_little_fresh_meat)
    TextView tvLittleFreshMeat;//小鲜肉
    @Bind(R.id.tv_lolita)
    TextView tvLolita;//萝莉
    @Bind(R.id.tv_quadratic_element)
    TextView tvQuadrticElement;//二次元
    @Bind(R.id.tv_other)
    TextView tvOther;//其他

    List<TextView> tvList = new ArrayList<>();

    @NonNull
    @Override
    protected BasePresenterImpl getPresent() {
        return new BasePresenterImpl(getSupportFragmentManager());
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
                Intent intent = new Intent();
                intent.putExtra("LABEL", label);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvList.add(tvGoToBedLateMan);
        tvList.add(tvYoungCynic);
        tvList.add(tvLittleFreshMeat);
        tvList.add(tvLolita);
        tvList.add(tvQuadrticElement);
        tvList.add(tvOther);
        setColor(tvGoToBedLateMan, "夜猫子");
    }

    String label="";

    @OnClick({R.id.tv_go_to_bed_late_man, R.id.tv_young_cynic, R.id.tv_little_fresh_meat, R.id.tv_lolita, R.id.tv_quadratic_element, R.id.tv_other})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_go_to_bed_late_man:
                setColor(tvGoToBedLateMan, "夜猫子");
                break;
            case R.id.tv_young_cynic:
                setColor(tvYoungCynic, "愤青");
                break;
            case R.id.tv_little_fresh_meat:
                setColor(tvLittleFreshMeat, "小鲜肉");
                break;
            case R.id.tv_lolita:
                setColor(tvLolita, "萝莉");
                break;
            case R.id.tv_quadratic_element:
                setColor(tvQuadrticElement, "二次元");
                break;
            case R.id.tv_other:
                setColor(tvOther, "其他");
                break;
        }
    }

    public void setColor(TextView tv, String str) {
        for (int i = 0; i < tvList.size(); i++) {
            tvList.get(i).setTextColor(Color.parseColor("#dbdbdb"));
        }
        tv.setTextColor(Color.parseColor("#264c55"));
        label=str;
    }

}
