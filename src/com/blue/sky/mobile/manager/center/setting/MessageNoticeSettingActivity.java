package com.blue.sky.mobile.manager.center.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.blue.sky.common.AppMain;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.mobile.manager.R;


public class MessageNoticeSettingActivity extends BaseActivity implements View.OnClickListener {

	private ImageButton btnNewNotifi;
	private ImageButton btnMsgContentNotifi;
	private ImageButton btnVioceNotifi;
	private ImageButton btnShakeNotifi;
	private boolean ifNotice;
	private boolean ifWithContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity_message_notice);
		setHeader("设置",true);

		initUI();
		
	}

	private void initUI() {
		btnNewNotifi = (ImageButton) findViewById(R.id.setting_notifi_newmsg);
		btnMsgContentNotifi = (ImageButton) findViewById(R.id.setting_show_msg_content);
		btnVioceNotifi = (ImageButton) findViewById(R.id.setting_voice_notifi);
		btnShakeNotifi = (ImageButton) findViewById(R.id.setting_shake_notifi);

		btnNewNotifi.setOnClickListener(this);
		btnMsgContentNotifi.setOnClickListener(this);
		btnShakeNotifi.setOnClickListener(this);
		btnVioceNotifi.setOnClickListener(this);


        ifNotice = AppMain.Cookies.getUserSetting().isNotice();
        ifWithContent = AppMain.Cookies.getUserSetting().isNoticeWithContent();

        btnNewNotifi.setImageResource(ifNotice ? R.drawable.icon_switch_on : R.drawable.icon_switch_off);
		btnMsgContentNotifi.setImageResource(ifWithContent ? R.drawable.icon_switch_on : R.drawable.icon_switch_off);
		btnVioceNotifi.setImageResource(AppMain.Cookies.getUserSetting().isMusic() ? R.drawable.icon_switch_on
				: R.drawable.icon_switch_off);
		btnShakeNotifi.setImageResource(AppMain.Cookies.getUserSetting().isShake() ? R.drawable.icon_switch_on
				: R.drawable.icon_switch_off);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_notifi_newmsg:
			btnNewNotifi.setImageResource(!ifNotice ? R.drawable.icon_switch_on : R.drawable.icon_switch_off);
            AppMain.Cookies.getUserSetting().setNotice(!ifNotice);
            showToast("设置成功");
			break;
		case R.id.setting_show_msg_content:
			btnMsgContentNotifi.setImageResource(!ifWithContent ? R.drawable.icon_switch_on
					: R.drawable.icon_switch_off);
            AppMain.Cookies.getUserSetting().setNotice(!ifWithContent);
            showToast("设置成功");
			break;
		case R.id.setting_voice_notifi:
			boolean isMusic = AppMain.Cookies.getUserSetting().isMusic();
			btnVioceNotifi.setImageResource(!isMusic ? R.drawable.icon_switch_on : R.drawable.icon_switch_off);
            AppMain.Cookies.getUserSetting().setMusic(!isMusic);
			showToast("设置成功");
			break;
		case R.id.setting_shake_notifi:
			boolean isShake = AppMain.Cookies.getUserSetting().isShake();
			btnShakeNotifi.setImageResource(!isShake ? R.drawable.icon_switch_on : R.drawable.icon_switch_off);
			AppMain.Cookies.getUserSetting().setShake(!isShake);
			showToast("设置成功");
			break;

		}
	}
}
