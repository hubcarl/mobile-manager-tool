package com.blue.sky.mobile.manager.apk.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.apk.adapter.AppListAdapter;
import com.blue.sky.mobile.manager.apk.model.AppInfo;
import com.blue.sky.mobile.manager.apk.utils.AppUtil;
import com.blue.sky.mobile.message.ActionManager;
import com.blue.sky.mobile.message.AppChangedReceiver;
import com.blue.sky.mobile.message.MessageCallback;

import java.util.ArrayList;
import java.util.List;


public class RunningAppFragment extends BaseFragment {

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
                    tipMessage.setText("扫描完成, 总共有" + appInfoList.size() + "个应用正在运行");
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
        appListAdapter = new AppListAdapter(getActivity(), appInfoList, 2);

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
                getRunningApp();
            }
        }).start();
    }

    private void getRunningApp() {

        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processList = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runProcess : processList) {
            if (runProcess.processName.equals("system")) {
                continue;
            }
            String[] pkgList = runProcess.pkgList;
            // 输出所有应用程序的包名
            for (int i = 0; i < pkgList.length; i++) {
                String pkgName = pkgList[i];
                PackageInfo packageInfo = null;
                try {
                    packageInfo = getActivity().getPackageManager().getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        if (!"com.blue.sky.mobile.manager".equals(pkgName)) {
                            AppInfo runApp = AppUtil.getAppInfoByPackageName(getActivity(), pkgName);
                            if (runApp != null) {
                                sendMessage(handler, MSG_REFRESH, runApp);
                            }
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
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