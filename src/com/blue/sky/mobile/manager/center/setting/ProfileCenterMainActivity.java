package com.blue.sky.mobile.manager.center.setting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.blue.sky.common.AppMain;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.common.sdk.SDK;
import com.blue.sky.common.utils.*;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.center.user.LoginActivity;
import com.blue.sky.mobile.message.ActionManager;
import com.blue.sky.mobile.message.LoginBroadcastReceiver;
import com.blue.sky.mobile.message.MessageCallback;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 个人中心
 */
public class ProfileCenterMainActivity extends BaseActivity implements View.OnClickListener {

    public static QQAuth mQQAuth;
    private Tencent mTencent;
    private com.tencent.connect.UserInfo mInfo;
    private String openid;

    private ImageView userIcon;
    private TextView userName;

    private LoginBroadcastReceiver loginBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sky_activity_center);
        //initUI();
        //initUserInfo();
        initListener();
        //initService();
        UIHelp.setHeaderMenuView(this, "应用设置");
    }

    private void initService() {
        loginBroadcastReceiver = new LoginBroadcastReceiver(new MessageCallback() {
            @Override
            public void onReceiveMessage(Context context, Intent intent) {
                initUserInfo();
            }
        });
        this.registerReceiver(loginBroadcastReceiver, new IntentFilter(ActionManager.ACTION_LOGIN));
    }


    private void initUserInfo() {
        String cacheUserName = AppMain.Cookies.getUserInfo().getUserName();
        if (Strings.isNotEmpty(cacheUserName)) {
            userName.setText(cacheUserName);
        }

        String loginType = AppMain.Cookies.getUserInfo().getLoginType();
        final String cacheLogo = AppMain.Cookies.getUserInfo().getUserIcon();
        if (EnumUtil.Login.QQ.toString().equals(loginType)) {
            if (NetWorkHelper.isConnect(this)) {
                ImageLoadUtil.getInstance().displayImage(cacheLogo, userIcon);
            } else {
                userIcon.setImageResource(R.drawable.icon_qq_logo);
            }
        } else {
            ImageLoadUtil.loadImage(userIcon, cacheLogo, ImageLoadUtil.ImageStyle.USER_MEMBER);
        }
    }

    private void initUI() {

        userIcon = (ImageView) this.findViewById(R.id.setting_user_iocn);
        userName = (TextView) this.findViewById(R.id.setting_user_name);
        // 创建QQ实例
        mQQAuth = QQAuth.createInstance(SDK.QQ_APP_ID, this.getApplicationContext());
        mTencent = Tencent.createInstance(SDK.QQ_APP_ID, this);
    }

    private void initListener() {
        //this.findViewById(R.id.setting_login).setOnClickListener(this);
        //this.findViewById(R.id.setting_favorite).setOnClickListener(this);
        //this.findViewById(R.id.setting_password).setOnClickListener(this);
        //this.findViewById(R.id.setting_game).setOnClickListener(this);
        //this.findViewById(R.id.setting_software).setOnClickListener(this);
        this.findViewById(R.id.setting_soft_update).setOnClickListener(this);
        this.findViewById(R.id.setting_set).setOnClickListener(this);
        this.findViewById(R.id.setting_about).setOnClickListener(this);
        this.findViewById(R.id.setting_feedback).setOnClickListener(this);
        this.findViewById(R.id.setting_exit).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        //this.unregisterReceiver(loginBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_login:
                //loginDialog();
                break;
            case R.id.setting_soft_update:
                if (NetWorkHelper.isConnect(this)) {
                    UIHelp.showLoading(this, "正在检查,请稍后......");
//                    HttpRequestAPI.parameter(Constants.PARAM_APK_VERSION, new HttpAsyncStringResult() {
//                        @Override
//                        public void process(String response) {
//                            UIHelp.closeLoading();
//                            if (Strings.isEmpty(response) || Constants.EMPTY_TEXT.equals(response) || response.equals(SystemUtil.getApkVersionCode())) {
//                                UIHelp.showToast(ProfileCenterMainActivity.this, "当前版本已是最新版本!");
//                            } else {
//                                UIHelp.showToast(ProfileCenterMainActivity.this, "有最新版本[" + response + "]!请到豌豆荚应用市场下载最新版本!");
//                            }
//                        }
//                    });
                } else {
                    UIHelp.showToast(this, "亲,没有网络,请检查网络设置!");
                }
                break;
            case R.id.setting_password:
                if (NetWorkHelper.isConnect(this)) {
//                    Intent intent = new Intent(this, ChangePasswordActivity.class);
//                    startActivity(intent);
                } else {
                    UIHelp.showToast(this, "亲,没有网络,请检查网络设置!");
                }
                break;
            case R.id.setting_exit:
                quit();
                break;
            case R.id.setting_about:
                startActivity(AboutActivity.class);
                break;
//            case R.id.setting_favorite:
//                startActivity(FavoriteListActivity.class);
//                break;
//            case R.id.setting_money:
//
//                break;
            case R.id.setting_set:
                startActivity(MessageNoticeSettingActivity.class);
                break;
            case R.id.setting_feedback:
                startActivity(FeedbackActivity.class);
                break;
            case R.id.setting_system:

                break;
            case R.id.setting_software:

                break;
        }
    }

    private void quit() {

//        UIHelp.showConfirmDialog(this, "确定要退出登录吗？", new UIHelp.OnConfirmDialogClickListener() {
//            @Override
//            public void onOkClick() {
//
//                // 发送广播
//                Intent intent = new Intent("android.intent.action.QUITCURRENTACCOUNT");
//                this.sendBroadcast(intent);
//            }
//
//            @Override
//            public void onCancelClick() {
//            }
//        });
    }


    private void loginDialog() {
        final Dialog dialog = new Dialog(this, R.style.exit_dialog);
        dialog.setContentView(R.layout.sky_login_dialog);

        TextView lovePlay = (TextView) dialog.findViewById(R.id.love_play_login);
        lovePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.class);
                dialog.dismiss();
            }
        });

        TextView qqLogin = (TextView) dialog.findViewById(R.id.qq_login);
        qqLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogin();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void onClickLogin() {
        if (!mQQAuth.isSessionValid()) {
            IUiListener listener = new BaseUiListener() {
                @Override
                protected void doComplete(JSONObject values) {
                    updateUserInfo();
                }
            };
            //mQQAuth.login(this, "all", listener);
            mTencent.loginWithOEM(this, "all", listener, "10000144", "10000144", "xxxx");
        } else {
            mQQAuth.logout(this);
            updateUserInfo();
        }
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Log.d("login response onComplete:", response.toString());
            //Util.showResultDialog(LoginActivity.this, response.toString(), "登陆成功");
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject response) {
            try {
                openid = response.getString("openid");
            } catch (JSONException e) {

            }
        }

        @Override
        public void onError(UiError e) {
//            Util.toastMessage(this, "onError: " + e.errorDetail);
//            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
//            Util.toastMessage(this, "onCancel: ");
//            Util.dismissDialog();
        }
    }


    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                Log.d("login response:", response.toString());
                String nickName = Strings.EMPTY_STRING;
                if (response.has("nickname")) {

                    try {
                        nickName = response.getString("nickname");
                        UIHelp.showToast(ProfileCenterMainActivity.this, "QQ登陆成功!");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                String icon = Strings.EMPTY_STRING;
                if (response.has("figureurl")) {
                    try {
                        icon = response.getString("figureurl_qq_2");
                        ImageLoadUtil.loadImage(icon);
                    } catch (JSONException e) {

                    }
                }
                openLogin(openid, nickName, icon, EnumUtil.Login.QQ);

                userName.setText(nickName);
                ImageLoadUtil.getInstance().displayImage(icon, userIcon);

                AppMain.Cookies.getUserInfo().setUserName(nickName);
                AppMain.Cookies.getUserInfo().setUserEmail("QQ账号登陆");
                AppMain.Cookies.getUserInfo().setUserIcon(icon);
                AppMain.Cookies.getUserInfo().setLoginType(EnumUtil.Login.QQ);
                ProfileCenterMainActivity.this.sendBroadcast(new Intent(ActionManager.ACTION_LOGIN));
            }
        }

    };

    private void updateUserInfo() {
        if (mQQAuth != null && mQQAuth.isSessionValid()) {
            IUiListener listener = new IUiListener() {

                @Override
                public void onError(UiError e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onCancel() {
                    // TODO Auto-generated method stub

                }
            };
            mInfo = new com.tencent.connect.UserInfo(this, mQQAuth.getQQToken());
            mInfo.getUserInfo(listener);

        }
    }


    private void openLogin(String openid, String nickName, String icon, EnumUtil.Login loginType) {
//        User user = new User();
//        user.setOpenid(openid);
//        user.setNickName(nickName);
//        user.setIcon(icon);
//        user.setLoginType(loginType);
//        HttpRequestAPI.openLogin(user, new HttpAsyncStringResult() {
//            public void process(String userId) {
//                Log.d(">>>openLogin userId:", userId);
//                if (Strings.isNumeric(userId)) {
//                    MyApplication.Cookies.getUserInfo().setUserId(userId);
//                }
//            }
//        });
    }
}
