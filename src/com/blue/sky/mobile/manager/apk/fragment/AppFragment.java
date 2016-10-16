package com.blue.sky.mobile.manager.apk.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import com.blue.sky.common.component.util.PackageUtils;
import com.blue.sky.common.fragment.BaseFragment;
import com.blue.sky.common.utils.EnumUtil;
import com.blue.sky.common.utils.SharedPreferenceUtil;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.apk.adapter.AppListAdapter;
import com.blue.sky.mobile.manager.apk.model.AppInfo;
import com.blue.sky.mobile.manager.apk.utils.FileUtil;
import com.blue.sky.mobile.message.ActionManager;
import com.blue.sky.mobile.message.AppChangedReceiver;
import com.blue.sky.mobile.message.MessageCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AppFragment extends BaseFragment {

    private View rootView;
    private ListView listView;
    private List<AppInfo> appInfoList = new ArrayList<AppInfo>();
    private AppListAdapter appListAdapter;
    private AppInfo selectedAppInfo;
    private AppChangedReceiver appChangedReceiver;

    private TextView tipMessage;
    private ProgressBar progressBar;
    private LinearLayout loading;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
                    appInfoList.add((AppInfo) msg.obj);
                    appListAdapter.notifyDataSetChanged();
                    break;
                case MSG_FINISHED:
                    progressBar.setVisibility(View.GONE);
                    tipMessage.setText("扫描完成, 总共安装了" + appInfoList.size() + "个应用");
                    SharedPreferenceUtil.getInstance(getActivity()).putInt(EnumUtil.Navigation.App.toString(), appInfoList.size());
                    getActivity().sendBroadcast(new Intent(ActionManager.ACTION_FILE_CHANGED));
                    break;
                case HIDE_LOADING:
                    loading.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.sky_activity_common_list, container, false);

        loading = (LinearLayout) rootView.findViewById(R.id.loading);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        tipMessage = (TextView) rootView.findViewById(R.id.tip_message);

        listView = (ListView) rootView.findViewById(R.id.list_view);
        appListAdapter = new AppListAdapter(getActivity(), appInfoList, 1);

        listView.setAdapter(appListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedAppInfo = appInfoList.get(position);
                showWindow(view);
            }
        });

        initAppList();
        initMessage();

        return rootView;
    }

    private void initMessage() {
        appChangedReceiver = new AppChangedReceiver(new MessageCallback() {
            @Override
            public void onReceiveMessage(Context context, Intent intent) {
                String packageName = intent.getStringExtra("packageName");
                int appStatus = intent.getIntExtra("appStatus", -1);
                if (appStatus > -1) {
                    for (AppInfo app : appInfoList) {
                        if (app.getPackageName().equals(packageName)) {
                            appInfoList.remove(app);
                            appListAdapter.notifyDataSetChanged();
                            SharedPreferenceUtil.getInstance(getActivity()).putInt(EnumUtil.Navigation.App.toString(), appInfoList.size());
                            getActivity().sendBroadcast(new Intent(ActionManager.ACTION_FILE_CHANGED));
                            break;
                        }
                    }
                }
            }
        });
        getActivity().registerReceiver(appChangedReceiver, new IntentFilter(ActionManager.ACTION_MSG_APP_CHANGED));
    }

    private void initAppList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getInstalledApp();
            }
        }).start();
    }

    private void getInstalledApp() {
        List<PackageInfo> packages = getActivity().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            // 非系统应用
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                AppInfo tmpInfo = new AppInfo();
                tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString());
                tmpInfo.setPackageName(packageInfo.packageName);
                tmpInfo.setVersionCode(packageInfo.versionCode);
                tmpInfo.setVersionName(packageInfo.versionName);
                tmpInfo.setSize(FileUtil.format(new File(packageInfo.applicationInfo.publicSourceDir).length()));
                tmpInfo.setIcon(packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager()));
                sendMessage(handler, MSG_REFRESH, tmpInfo);
            }
        }
        sendMessage(handler, MSG_FINISHED, null);
        sendMessageDelayed(handler, HIDE_LOADING, null, 2000);
    }

    private PopupWindow popupWindow;

    private void showWindow(View parent) {

        int[] location = new int[2];
        //location [0]--->x坐标,location [1]--->y坐标
        parent.getLocationInWindow(location);

        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        TextView txtAppOpen, txtUnInstall, txtDetail;

        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.apk_app_list_menu_item, null);
            txtAppOpen = (TextView) view.findViewById(R.id.app_open);
            txtUnInstall = (TextView) view.findViewById(R.id.app_unInstall);
            txtDetail = (TextView) view.findViewById(R.id.app_detail);
            txtAppOpen.setOnClickListener(this);
            txtUnInstall.setOnClickListener(this);
            txtDetail.setOnClickListener(this);
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(view, windowManager.getDefaultDisplay().getWidth(), UIHelp.dip2px(getActivity(), 56));
            // 使其聚集
            popupWindow.setFocusable(true);
            // 设置允许在外点击消失
            popupWindow.setOutsideTouchable(true);
            // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
        }
        popupWindow.showAsDropDown(parent, location[0], 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_open:
                PackageUtils.startApp(getActivity(), selectedAppInfo.getPackageName());
                popupWindow.dismiss();
                break;
            case R.id.app_unInstall:
                popupWindow.dismiss();
                PackageUtils.uninstall(getActivity(), selectedAppInfo.getPackageName());
                appListAdapter.notifyDataSetChanged();
                break;
            case R.id.app_detail:
                PackageUtils.startInstalledAppDetails(getActivity(), selectedAppInfo.getPackageName());
                popupWindow.dismiss();
                break;
        }
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(appChangedReceiver);
        super.onDestroy();
    }


}