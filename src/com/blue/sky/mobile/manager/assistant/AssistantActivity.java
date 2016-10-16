package com.blue.sky.mobile.manager.assistant;

import android.os.Bundle;
import android.view.View;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.common.utils.EnumUtil;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.assistant.bus.BusMainActivity;
import com.blue.sky.mobile.manager.assistant.calculator.CalculatorActivity;
import com.blue.sky.mobile.manager.assistant.metro.MetroMainActivity;
import com.blue.sky.mobile.qrcode.CaptureActivity;


public class AssistantActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assistant_activity_main);
        UIHelp.setHeaderMenuView(this, "生活助手");

        initEvent();
    }

    private void initEvent(){
        initUIView(R.id.assistant_qr_code).setOnClickListener(this);
        initUIView(R.id.assistant_bus).setOnClickListener(this);
        initUIView(R.id.assistant_metro).setOnClickListener(this);
        initUIView(R.id.assistant_foot).setOnClickListener(this);
        initUIView(R.id.assistant_ktv).setOnClickListener(this);
        initUIView(R.id.assistant_calculator).setOnClickListener(this);

        setIntCache(EnumUtil.Navigation.Assistant.toString(), 3);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.assistant_qr_code:
                startActivity(CaptureActivity.class);
                break;
            case R.id.assistant_bus:
                startActivity(BusMainActivity.class);
                break;
            case R.id.assistant_metro:
                startActivity(MetroMainActivity.class);
                break;
            case R.id.assistant_calculator:
                startActivity(CalculatorActivity.class);
                break;
        }
    }
}
